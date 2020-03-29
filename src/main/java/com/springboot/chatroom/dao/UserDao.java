package com.springboot.chatroom.dao;

import com.springboot.chatroom.model.po.User;

public interface UserDao {
    void loadUser();
    User getByUsername(String username);
    User getByUserId(String userId);
}
