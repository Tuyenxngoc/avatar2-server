package com.teamobi.avatar2.handler.impl;

import com.teamobi.avatar2.constant.Cmd;
import com.teamobi.avatar2.handler.IMessageHandler;
import com.teamobi.avatar2.network.IMessage;
import com.teamobi.avatar2.network.ISession;
import com.teamobi.avatar2.service.IMessageService;
import com.teamobi.avatar2.service.impl.MessageService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author tuyen
 */
@Slf4j
public class MessageHandler implements IMessageHandler {

    private final IMessageService messageService;

    private final ISession session;

    public MessageHandler(ISession session) {
        this.session = session;
        this.messageService = new MessageService(session);
    }

    @Override
    public void onMessage(IMessage ms) {
        try {
            switch (ms.getCommand()) {
                case Cmd.GET_IMG_ICON -> messageService.sendImageIcon(ms);

                case Cmd.GET_KEY -> session.sendKeys();

                case Cmd.GET_HANDLER -> messageService.setSessionHandler(ms);

                default -> log.warn("Command {} is not supported", ms.getCommand());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}
