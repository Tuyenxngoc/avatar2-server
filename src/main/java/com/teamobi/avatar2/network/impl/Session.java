package com.teamobi.avatar2.network.impl;

import com.teamobi.avatar2.constant.Cmd;
import com.teamobi.avatar2.constant.GameConstants;
import com.teamobi.avatar2.handler.IMessageHandler;
import com.teamobi.avatar2.handler.impl.MessageHandler;
import com.teamobi.avatar2.model.User;
import com.teamobi.avatar2.network.IMessage;
import com.teamobi.avatar2.network.ISession;
import com.teamobi.avatar2.server.ServerManager;
import lombok.extern.slf4j.Slf4j;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.SecureRandom;
import java.util.ArrayList;

/**
 * @author tuyen
 */
@Slf4j
public class Session implements ISession {

    private static final int TIMEOUT_DURATION = 180_000;

    private final byte[] sessionKey;
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;

    private boolean sendKeyComplete;
    private byte curR;
    private byte curW;

    private final Sender sender = new Sender();
    private IMessageHandler messageHandler;

    private Thread collectorThread;
    private Thread sendThread;

    private final long sessionId;
    private final String IPAddress;
    private String platform;
    private String version;
    private byte provider;
    private User user;
    private String resourcePath = GameConstants.RESOURCE_MEDIUM_PATH;

    public Session(long sessionId, Socket socket) throws IOException {
        this.sessionId = sessionId;
        this.socket = socket;
        this.dis = new DataInputStream(socket.getInputStream());
        this.dos = new DataOutputStream(socket.getOutputStream());
        this.IPAddress = socket.getInetAddress().getHostName();

        this.setHandler(new MessageHandler(this));

        this.sessionKey = generateSessionKey();
        initializeThreads();
    }

    private void initializeThreads() {
        this.sendThread = new Thread(sender, sessionId + "_send");
        this.collectorThread = new Thread(new MessageCollector(), sessionId + "_collector");
        this.collectorThread.start();
    }

    private byte[] generateSessionKey() {
        byte[] key = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(key);
        return key;
    }

    private boolean isSendKeyComplete() {
        return sendKeyComplete;
    }

    @Override
    public void setHandler(IMessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    @Override
    public void sendMessage(IMessage message) {
        sender.addMessage(message);
    }

    @Override
    public void close() {
        try {
            ServerManager.getInstance().disconnect(this);
            cleanNetwork();
            log.info("Close {}", this);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public String getIPAddress() {
        return IPAddress;
    }

    @Override
    public String getPlatform() {
        return platform;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public byte getProvider() {
        return provider;
    }

    @Override
    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public void setPlatform(String platform) {
        this.platform = platform;
    }

    @Override
    public void setProvider(byte provider) {
        this.provider = provider;
    }

    @Override
    public String getResourcePath() {
        return resourcePath;
    }

    @Override
    public void setResourcePath(String path) {
        resourcePath = path;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void sendKeys() {
        try {
            IMessage ms = new Message(-27);
            DataOutputStream ds = ms.writer();
            ds.writeByte(sessionKey.length);
            ds.writeByte(sessionKey[0]);
            for (int i = 1; i < sessionKey.length; i++) {
                ds.writeByte(sessionKey[i] ^ sessionKey[i - 1]);
            }
            ds.flush();
            doSendMessage(ms);
            sendKeyComplete = true;
            sendThread.start();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        return "Client " + sessionId;
    }

    protected synchronized void doSendMessage(IMessage m) {
        byte[] data = m.getData();
        try {
            if (sendKeyComplete) {
                dos.writeByte(writeKey(m.getCommand()));
            } else {
                dos.writeByte(m.getCommand());
            }
            if (data != null) {
                int size = data.length;
                if (m.getCommand() == 90) {
                    dos.writeInt(size);
                } else {
                    if (sendKeyComplete) {
                        dos.writeByte(writeKey((byte) (size >> 8)));
                        dos.writeByte(writeKey((byte) (size & 0xFF)));
                    } else {
                        dos.writeShort(size);
                    }
                    if (sendKeyComplete) {
                        for (int i = 0; i < data.length; i++) {
                            data[i] = writeKey(data[i]);
                        }
                    }
                }
                dos.write(data);
            } else {
                dos.writeShort(0);
            }
            dos.flush();
            m.cleanup();
        } catch (Exception e) {
            closeMessage();
        }
    }

    private byte readKey(byte b) {
        byte i = (byte) ((sessionKey[curR++] & 0xff) ^ (b & 0xff));
        if (curR >= sessionKey.length) {
            curR %= (byte) sessionKey.length;
        }
        return i;
    }

    private byte writeKey(byte b) {
        byte i = (byte) ((sessionKey[curW++] & 0xff) ^ (b & 0xff));
        if (curW >= sessionKey.length) {
            curW %= (byte) sessionKey.length;
        }
        return i;
    }

    private void cleanNetwork() {
        curR = 0;
        curW = 0;
        try {
            sendKeyComplete = false;
            if (socket != null) {
                socket.close();
                socket = null;
            }
            if (dos != null) {
                dos.close();
                dos = null;
            }
            if (dis != null) {
                dis.close();
                dis = null;
            }
            sendThread = null;
            collectorThread = null;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public void closeMessage() {
        if (isSendKeyComplete()) {
            close();
        }
    }

    private class Sender implements Runnable {

        private final ArrayList<IMessage> sendingMessage = new ArrayList<>();

        public void addMessage(IMessage message) {
            sendingMessage.add(message);
        }

        @Override
        public void run() {
            try {
                while (Session.this.isSendKeyComplete()) {
                    while (!sendingMessage.isEmpty() && Session.this.dis != null) {
                        IMessage message = sendingMessage.removeFirst();
                        log.info("Send mss {} to {}", Cmd.getCmdNameByValue(message.getCommand()), Session.this);
                        Session.this.doSendMessage(message);
                    }
                    try {
                        Thread.sleep(10L);
                    } catch (InterruptedException e) {
                        log.error(e.getMessage(), e);
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    class MessageCollector implements Runnable {

        @Override
        public void run() {
            try {
                while (Session.this.dis != null) {
                    Session.this.socket.setSoTimeout(TIMEOUT_DURATION);
                    IMessage message = readMessage();
                    if (message == null) {
                        break;
                    }
                    log.info("{} send mss {}", Session.this, Cmd.getCmdNameByValue(message.getCommand()));
                    Session.this.messageHandler.onMessage(message);
                    message.cleanup();
                }
                closeMessage();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }

        private IMessage readMessage() {
            try {
                byte cmd = Session.this.dis.readByte();
                if (Session.this.sendKeyComplete) {
                    cmd = Session.this.readKey(cmd);
                }
                int size;
                if (Session.this.sendKeyComplete) {
                    byte b1 = Session.this.dis.readByte();
                    byte b2 = Session.this.dis.readByte();
                    size = ((Session.this.readKey(b1) & 0xff) << 8) | (Session.this.readKey(b2) & 0xff);
                } else {
                    size = Session.this.dis.readUnsignedShort();
                }
                byte[] data = new byte[size];
                int len = 0;
                int byteRead = 0;
                while (len != -1 && byteRead < size) {
                    len = Session.this.dis.read(data, byteRead, size - byteRead);
                    if (len > 0) {
                        byteRead += len;
                    }
                }
                if (Session.this.sendKeyComplete) {
                    for (int i = 0; i < data.length; i++) {
                        data[i] = readKey(data[i]);
                    }
                }
                return new Message(cmd, data);
            } catch (Exception e) {
                return null;
            }
        }
    }
}
