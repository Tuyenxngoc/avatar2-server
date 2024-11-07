package com.teamobi.avatar2.database;

import com.teamobi.avatar2.config.impl.HikariCPConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author tuyen
 */
public class HikariCPManager {

    private static volatile HikariCPManager instance;
    private final HikariDataSource dataSource;

    private HikariCPManager() {
        HikariCPConfig config = new HikariCPConfig();
        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setJdbcUrl(config.getJdbcUrl());
        hikariConfig.setUsername(config.getUsername());
        hikariConfig.setPassword(config.getPassword());

        hikariConfig.setMaximumPoolSize(config.getMaxPoolSize());
        hikariConfig.setMinimumIdle(config.getMinIdle());
        hikariConfig.setConnectionTimeout(config.getConnectionTimeout());

        dataSource = new HikariDataSource(hikariConfig);
    }

    public static HikariCPManager getInstance() {
        if (instance == null) {
            synchronized (HikariCPManager.class) {
                if (instance == null) {
                    instance = new HikariCPManager();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void closeDataSource() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}
