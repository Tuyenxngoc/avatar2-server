package com.teamobi.avatar2.service.impl;

import com.teamobi.avatar2.handler.impl.AvatarMessageHandler;
import com.teamobi.avatar2.handler.impl.FarmMessageHandler;
import com.teamobi.avatar2.handler.impl.HomeMessageHandler;
import com.teamobi.avatar2.handler.impl.ParkMessageHandler;
import com.teamobi.avatar2.network.IMessage;
import com.teamobi.avatar2.network.ISession;
import com.teamobi.avatar2.service.IMessageService;

import java.io.IOException;

public class MessageService implements IMessageService {
    private final ISession session;

    public MessageService(ISession session) {
        this.session = session;
    }

    @Override
    public void setSessionHandler(IMessage ms) {
        try {
            byte index = ms.reader().readByte();

            switch (index) {
                case 8 -> session.setHandler(new AvatarMessageHandler(session));
                case 9 -> session.setHandler(new ParkMessageHandler(session));
                case 10 -> session.setHandler(new FarmMessageHandler(session));
                case 11 -> session.setHandler(new HomeMessageHandler(session));

            }

        } catch (IOException ignored) {

        }
    }
}
