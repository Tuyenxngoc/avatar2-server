package com.teamobi.avatar2.handler.impl;

import com.teamobi.avatar2.handler.IMessageHandler;
import com.teamobi.avatar2.network.IMessage;
import com.teamobi.avatar2.network.ISession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ParkMessageHandler implements IMessageHandler {

    private final ISession session;

    public ParkMessageHandler(ISession session) {
        this.session = session;
    }

    @Override
    public void onMessage(IMessage message) {
        try {
            switch (message.getCommand()) {
                default -> log.warn("Command {} is not supported", message.getCommand());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
