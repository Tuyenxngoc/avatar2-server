package com.teamobi.avatar2.config.impl;

import com.teamobi.avatar2.config.IServerConfig;

/**
 * @author tuyen
 */
public class ServerConfig implements IServerConfig {

    public ServerConfig(String avatar2Properties) {

    }

    @Override
    public int getPort() {
        return 19128;
    }

    @Override
    public int getMaxClients() {
        return 10;
    }

}