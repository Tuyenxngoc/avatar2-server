package com.teamobi.avatar2.network;

import com.teamobi.avatar2.handler.IMessageHandler;

/**
 * @author tuyen
 */
public interface ISession {

    void setHandler(IMessageHandler messageHandler);

    void sendMessage(IMessage message);

    void sendKeys();

    void close();

    String getIPAddress();

    String getPlatform();

    String getVersion();

    byte getProvider();

    void setVersion(String version);

    void setPlatform(String platform);

    void setProvider(byte provider);
}
