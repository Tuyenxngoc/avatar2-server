package com.teamobi.avatar2.handler.impl;

import com.teamobi.avatar2.network.IMessage;
import com.teamobi.avatar2.network.ISession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ParkMessageHandler extends MessageHandler {

    public ParkMessageHandler(ISession session) {
        super(session);
    }

    @Override
    public void onMessage(IMessage message) {
        try {
            switch (message.getCommand()) {
                default -> super.onMessage(message);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
