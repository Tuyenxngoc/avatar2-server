package com.teamobi.avatar2.server;

import com.teamobi.avatar2.config.IServerConfig;
import com.teamobi.avatar2.config.impl.ServerConfig;
import com.teamobi.avatar2.constant.CommonConstant;
import com.teamobi.avatar2.database.HikariCPManager;
import com.teamobi.avatar2.network.IMessage;
import com.teamobi.avatar2.network.ISession;
import com.teamobi.avatar2.network.impl.Session;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * @author tuyen
 */
@Slf4j
public class ServerManager {

    private static volatile ServerManager instance;

    private final IServerConfig config;
    private ServerSocket server;
    private long countClients;
    private boolean isStart;
    private final ArrayList<ISession> users = new ArrayList<>();

    public ServerManager() {
        this.config = new ServerConfig(CommonConstant.AVATAR_2_PROPERTIES);
    }

    public static ServerManager getInstance() {
        if (instance == null) {
            synchronized (ServerManager.class) {
                if (instance == null) {
                    instance = new ServerManager();
                }
            }
        }
        return instance;
    }

    public void init() {
        initServerData();
        setCache();
    }

    private void initServerData() {
    }

    private void setCache() {
    }

    public void start() {
        log.info("Start server!");
        isStart = true;
        try {
            server = new ServerSocket(config.getPort());
            log.info("Server start at port: {}", config.getPort());
            while (isStart) {
                if (users.size() < config.getMaxClients()) {
                    try {
                        Socket socket = server.accept();
                        ISession session = new Session(++countClients, socket);
                        users.add(session);
                        log.info("Accept socket client {} done!", countClients);
                    } catch (Exception ignored) {
                    }
                } else {
                    try {
                        log.warn("Maximum number of players reached. Waiting for a slot to be free.");
                        Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                        log.error(e.getMessage(), e);
                    }
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void stop() {
        log.info("Stop server!");
        isStart = false;
        try {
            while (!users.isEmpty()) {
                ISession session = users.getFirst();
                session.close();
            }
            if (server != null) {
                server.close();
            }
            HikariCPManager.getInstance().closeDataSource();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void disconnect(Session session) {
        users.remove(session);
    }

    public void sendToServer(IMessage ms) {
        for (ISession session : users) {
            session.sendMessage(ms);
        }
    }
}
