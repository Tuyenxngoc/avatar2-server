package com.teamobi.avatar2.service.impl;

import com.teamobi.avatar2.dao.IUserDao;
import com.teamobi.avatar2.dao.impl.UserDao;
import com.teamobi.avatar2.network.IMessage;
import com.teamobi.avatar2.network.ISession;
import com.teamobi.avatar2.service.IAvatarService;

import java.io.DataInputStream;
import java.io.IOException;

public class AvatarService implements IAvatarService {

    private final ISession session;
    private final IUserDao userDao;

    public AvatarService(ISession session) {
        this.session = session;
        this.userDao = new UserDao();
    }

    @Override
    public void getProvider(IMessage message) {
        try {
            DataInputStream dis = message.reader();
            dis.readByte();
            dis.readInt();
            dis.readUTF();
            dis.readInt();
            dis.readInt();
            dis.readInt();
            dis.readBoolean();
            dis.readByte();
        } catch (IOException ignored) {
        }
    }

    @Override
    public void getAgent(IMessage message) {
        try {
            String agent = message.reader().readUTF();
            System.out.println("agentInfo: " + agent);
        } catch (IOException ignored) {
        }
    }

    @Override
    public void getNumber(IMessage message) {
        try {
            int i = message.reader().readInt();
            System.out.println("Number: " + i);
        } catch (IOException ignored) {
        }
    }

    @Override
    public void login(IMessage message) {
        try {
            DataInputStream dis = message.reader();

            String username = dis.readUTF().trim();
            String password = dis.readUTF().trim();
            String version = dis.readUTF().trim();

            if (userDao.isValidUser(username, password)) {
            }
            System.out.println(username + " " + password + " " + version);
        } catch (IOException ignored) {
        }
    }
}

