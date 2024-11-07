package com.teamobi.avatar2.dao.impl;

import com.teamobi.avatar2.dao.IUserDao;
import lombok.extern.slf4j.Slf4j;

/**
 * @author tuyen
 */
@Slf4j
public class UserDao implements IUserDao {

    @Override
    public boolean isValidUser(String username, String password) {
        return false;
    }

}
