package com.teamobi.avatar2.service.impl;

import com.teamobi.avatar2.constant.Cmd;
import com.teamobi.avatar2.network.IMessage;
import com.teamobi.avatar2.network.ISession;
import com.teamobi.avatar2.network.impl.Message;

import java.io.DataOutputStream;
import java.io.IOException;

public class Service {
    protected final ISession session;

    public Service(ISession session) {
        this.session = session;
    }

    protected void sendMessage(IMessage message) {
        session.sendMessage(message);
    }

    protected void serverDialog(String message) {
        try {
            IMessage ms = new Message(Cmd.SET_MONEY_ERROR);
            DataOutputStream ds = ms.writer();
            ds.writeUTF(message);
            ds.flush();
            sendMessage(ms);
        } catch (IOException ignored) {
        }
    }

    protected void serverMessage(String message) {
        try {
            IMessage ms = new Message(Cmd.SERVER_MESSAGE);
            DataOutputStream ds = ms.writer();
            ds.writeUTF(message);
            ds.flush();
            sendMessage(ms);
        } catch (IOException ignored) {
        }
    }
}
