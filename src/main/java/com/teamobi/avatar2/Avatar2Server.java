package com.teamobi.avatar2;

import com.teamobi.avatar2.server.ServerManager;

/**
 * @author tuyen
 */
public class Avatar2Server {

    public static void main(String[] args) {
        ServerManager serverManager = ServerManager.getInstance();
        Runtime.getRuntime().addShutdownHook(new Thread(serverManager::stop, "serverShutdownHook"));
        serverManager.init();
        serverManager.start();
    }

}
