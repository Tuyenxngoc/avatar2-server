package com.teamobi.avatar2.service.impl;

import com.teamobi.avatar2.constant.Cmd;
import com.teamobi.avatar2.constant.GameConstants;
import com.teamobi.avatar2.dao.IUserDao;
import com.teamobi.avatar2.dao.impl.UserDao;
import com.teamobi.avatar2.model.User;
import com.teamobi.avatar2.network.IMessage;
import com.teamobi.avatar2.network.ISession;
import com.teamobi.avatar2.network.impl.Message;
import com.teamobi.avatar2.service.IAvatarService;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

public class AvatarService extends Service implements IAvatarService {

    private final IUserDao userDao;

    public AvatarService(ISession session) {
        super(session);
        this.userDao = new UserDao();
    }

    @Override
    public void getProvider(IMessage message) {
        try {
            DataInputStream dis = message.reader();
            dis.readByte();
            dis.readInt();
            dis.readUTF();
            dis.readInt();
            dis.readInt();
            dis.readInt();
            dis.readBoolean();
            byte resourceType = dis.readByte();
            if (resourceType == 1) {
                session.setResourcePath(GameConstants.RESOURCE_HD_PATH);
            } else {
                session.setResourcePath(GameConstants.RESOURCE_MEDIUM_PATH);
            }
        } catch (IOException ignored) {
        }
    }

    @Override
    public void getAgent(IMessage message) {
        try {
            String agent = message.reader().readUTF();
            System.out.println("agentInfo: " + agent);
        } catch (IOException ignored) {
        }
    }

    @Override
    public void getNumber(IMessage message) {
        try {
            int i = message.reader().readInt();
            System.out.println("Number: " + i);
        } catch (IOException ignored) {
        }
    }

    @Override
    public void login(IMessage message) {
        try {
            DataInputStream dis = message.reader();

            String username = dis.readUTF().trim();
            String password = dis.readUTF().trim();
            String version = dis.readUTF().trim();
            if (userDao.isValidUser(username, password)) {

                User user = userDao.getUserByUsername(username);
                session.setUser(user);
                onLoginSuccess();
            } else {

            }
        } catch (IOException ignored) {
        }
    }

    @Override
    public void checkExpiredPet(IMessage message) {

    }

    @Override
    public void getBig(IMessage message) {
        try {
            File file = new File(session.getResourcePath() + "/big/");
            File[] listFiles = file.listFiles();
            if (listFiles == null) {
                return;
            }

            Message ms = new Message(Cmd.SET_BIG);
            DataOutputStream ds = ms.writer();
            ds.writeByte(listFiles.length);
            for (File f : listFiles) {
                String name = f.getName().split("\\.")[0];
                int id = Integer.parseInt(name);
                int size = (int) f.length();
                ds.writeShort(id);
                ds.writeShort(size);
            }
            ds.writeShort(1);
            ds.writeShort(1);
            ds.writeShort(1);
            ds.writeShort(1);
            ds.writeShort(1);
            ds.writeByte(0);
            ds.writeInt(1);
            ds.flush();
            sendMessage(ms);
        } catch (IOException ignored) {
        }
    }

    public void onLoginSuccess() {
        try {
            User us = session.getUser();
            IMessage message = new Message(Cmd.LOGIN_SUCESS);
            DataOutputStream ds = message.writer();
            ds.writeInt(us.getUserId());
            ds.writeByte(0);
//            for () {
//                ds.writeShort(0);
//            }
            ds.writeByte(1);
            ds.writeByte(1);
            ds.writeByte(1);
            ds.writeInt(us.getXu());
            ds.writeByte(1);
            ds.writeByte(1);
            ds.writeByte(1);
            ds.writeByte(1);
            ds.writeByte(100);
            ds.writeInt(us.getLuong());
            ds.writeByte(0);
//            for (Item itm : wearing) {
//                ds.writeByte(itm.reliability());
//                ds.writeUTF(itm.expiredString());
//            }
            ds.writeShort(0);
            ds.writeByte(0);
//            for (Command cmd : listCmd) {
//                ds.writeUTF(cmd.getName());
//                ds.writeShort(cmd.getIcon());
//            }
            ds.writeByte(0);
//            for (Command cmd : listCmdRotate) {
//                ds.writeShort(cmd.getAnthor());
//                ds.writeUTF(cmd.getName());
//                ds.writeShort(cmd.getIcon());
//            }
            ds.writeBoolean(true);// isTour
//            for (Command cmd : listCmdRotate) {
//                ds.writeByte(cmd.getType());
//            }
            ds.writeByte(1);
            ds.writeShort(1);
            ds.writeShort(-1);
            ds.writeBoolean(false);//new version
//            if (session.isNewVersion()) {
//                ds.writeInt(us.getXeng());
//            }
            int m = 4;
            ds.writeByte((byte) m);
            short[] IDAction = {103, 102, 104, 107};
            String[] actionName = new String[]{"Tặng Hoa Violet", "Hôn", "Tặng cánh hoa",
                    "Tặng Hoa Tuyết"};
            short[] IDIcon = {1124, 1188, 1187, 1173};
            int[] money = {20000, 2000, 10000, 5};
            byte[] typeMoney = {0, 0, 0, 1};
            for (int i2 = 0; i2 < m; ++i2) {
                ds.writeShort(IDAction[i2]);
                ds.writeUTF(actionName[i2]);
                ds.writeShort(IDIcon[i2]);
                ds.writeInt(money[i2]);
                ds.writeByte(typeMoney[i2]);
            }
            ds.writeInt(us.getLuong());
            ds.writeInt(1);
            ds.writeByte(1);
            ds.writeUTF(us.getUsername());
            ds.flush();
            sendMessage(message);
        } catch (IOException ignored) {
        }
    }

}

