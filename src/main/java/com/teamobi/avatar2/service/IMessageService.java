package com.teamobi.avatar2.service;

import com.teamobi.avatar2.network.IMessage;

public interface IMessageService {
    void sendImageIcon(IMessage message);

    void setSessionHandler(IMessage ms);
}
