package com.teamobi.avatar2.dao;

import com.teamobi.avatar2.model.User;

/**
 * @author tuyen
 */
public interface IUserDao {

    boolean isValidUser(String username, String password);

    User getUserByUsername(String username);

}
