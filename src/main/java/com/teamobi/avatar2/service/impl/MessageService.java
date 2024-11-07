package com.teamobi.avatar2.service.impl;

import com.teamobi.avatar2.constant.Cmd;
import com.teamobi.avatar2.constant.GameConstants;
import com.teamobi.avatar2.handler.impl.AvatarMessageHandler;
import com.teamobi.avatar2.handler.impl.FarmMessageHandler;
import com.teamobi.avatar2.handler.impl.HomeMessageHandler;
import com.teamobi.avatar2.handler.impl.ParkMessageHandler;
import com.teamobi.avatar2.network.IMessage;
import com.teamobi.avatar2.network.ISession;
import com.teamobi.avatar2.network.impl.Message;
import com.teamobi.avatar2.service.IMessageService;
import com.teamobi.avatar2.util.Utils;
import lombok.extern.slf4j.Slf4j;

import java.io.DataOutputStream;
import java.io.IOException;

@Slf4j
public class MessageService extends Service implements IMessageService {

    public MessageService(ISession session) {
        super(session);
    }

    @Override
    public void sendImageIcon(IMessage message) {
        try {
            short imageId = message.reader().readShort();
            String folder = session.getResourcePath();
            byte[] data = Utils.getFile(String.format(GameConstants.RESOURCE_OBJECT_PATH, folder, imageId));
            if (data == null) {
                return;
            }
            message = new Message(Cmd.GET_IMG_ICON);
            DataOutputStream ds = message.writer();
            ds.writeShort(imageId);
            ds.writeShort(data.length);
            ds.write(data);
            ds.flush();
            sendMessage(message);
        } catch (IOException ignored) {
        }
    }

    @Override
    public void setSessionHandler(IMessage ms) {
        try {
            byte index = ms.reader().readByte();

            switch (index) {
                case 8 -> {
                    log.info("Switched to AvatarMessageHandler for session.");
                    session.setHandler(new AvatarMessageHandler(session));
                }
                case 9 -> {
                    log.info("Switched to ParkMessageHandler for session.");
                    session.setHandler(new ParkMessageHandler(session));
                }
                case 10 -> {
                    log.info("Switched to FarmMessageHandler for session.");
                    session.setHandler(new FarmMessageHandler(session));
                }
                case 11 -> {
                    log.info("Switched to HomeMessageHandler for session.");
                    session.setHandler(new HomeMessageHandler(session));
                }
            }

        } catch (IOException ignored) {

        }
    }
}
