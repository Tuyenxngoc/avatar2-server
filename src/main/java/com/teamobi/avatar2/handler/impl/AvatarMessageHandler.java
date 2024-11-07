package com.teamobi.avatar2.handler.impl;

import com.teamobi.avatar2.constant.Cmd;
import com.teamobi.avatar2.handler.IMessageHandler;
import com.teamobi.avatar2.network.IMessage;
import com.teamobi.avatar2.network.ISession;
import com.teamobi.avatar2.service.IAvatarService;
import com.teamobi.avatar2.service.impl.AvatarService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AvatarMessageHandler implements IMessageHandler {

    private final ISession session;

    private final IAvatarService avatarService;

    public AvatarMessageHandler(ISession session) {
        this.session = session;
        this.avatarService = new AvatarService(session);
    }

    @Override
    public void onMessage(IMessage message) {
        try {
            switch (message.getCommand()) {
                case Cmd.SET_AGENT -> avatarService.getAgent(message);

                case Cmd.NUMBER_SUPPORT -> avatarService.getNumber(message);

                case Cmd.SET_PROVIDER -> avatarService.getProvider(message);

                case Cmd.LOGIN -> avatarService.login(message);

                default -> log.warn("Command {} is not supported", message.getCommand());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
