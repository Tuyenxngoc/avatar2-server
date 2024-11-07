package com.teamobi.avatar2.config;

/**
 * @author tuyen
 */
public interface IDatabaseConfig {

    String getJdbcUrl();

    String getUsername();

    String getPassword();

    int getMaxPoolSize();

    int getMinIdle();

    int getConnectionTimeout();

}
