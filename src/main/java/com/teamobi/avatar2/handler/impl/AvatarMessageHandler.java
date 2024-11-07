package com.teamobi.avatar2.handler.impl;

import com.teamobi.avatar2.constant.Cmd;
import com.teamobi.avatar2.network.IMessage;
import com.teamobi.avatar2.network.ISession;
import com.teamobi.avatar2.service.IAvatarService;
import com.teamobi.avatar2.service.impl.AvatarService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AvatarMessageHandler extends MessageHandler {

    private final IAvatarService avatarService;

    public AvatarMessageHandler(ISession session) {
        super(session);
        this.avatarService = new AvatarService(session);
    }

    @Override
    public void onMessage(IMessage message) {
        try {
            switch (message.getCommand()) {
                case Cmd.SET_AGENT -> avatarService.getAgent(message);

                case Cmd.REQUEST_EXPICE_PET -> avatarService.checkExpiredPet(message);

                case Cmd.NUMBER_SUPPORT -> avatarService.getNumber(message);

                case Cmd.SET_PROVIDER -> avatarService.getProvider(message);

                case Cmd.SET_BIG -> avatarService.getBig(message);

                case Cmd.LOGIN -> avatarService.login(message);

                default -> super.onMessage(message);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
