package com.teamobi.avatar2.service;

import com.teamobi.avatar2.network.IMessage;

public interface IAvatarService {
    void getProvider(IMessage message);

    void getAgent(IMessage message);

    void getNumber(IMessage message);

    void login(IMessage message);
}
