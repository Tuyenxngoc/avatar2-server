package com.teamobi.avatar2.config.impl;

import com.teamobi.avatar2.config.IServerConfig;
import com.teamobi.avatar2.constant.CommonConstant;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * @author tuyen
 */
public class ServerConfig implements IServerConfig {
    private final Properties configMap;
    private short port;
    private int maxClients;

    public ServerConfig(String resourceName) {
        configMap = new Properties();
        try (FileInputStream fis = new FileInputStream(CommonConstant.RESOURCES_PATH + resourceName);
             InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8)
        ) {
            configMap.load(isr);
            initConfigProperties();
            validateConfigProperties();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void initConfigProperties() {
        try {
            port = Short.parseShort(configMap.getProperty("port", "8122"));
            maxClients = Integer.parseInt(configMap.getProperty("max_clients", "1000"));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void validateConfigProperties() {
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public int getMaxClients() {
        return maxClients;
    }

}