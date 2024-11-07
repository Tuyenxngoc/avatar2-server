package com.teamobi.avatar2.handler.impl;

import com.teamobi.avatar2.network.IMessage;
import com.teamobi.avatar2.network.ISession;
import com.teamobi.avatar2.service.IFarmService;
import com.teamobi.avatar2.service.impl.FarmService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FarmMessageHandler extends MessageHandler {

    private final IFarmService farmService;

    public FarmMessageHandler(ISession session) {
        super(session);
        this.farmService = new FarmService(session);
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
