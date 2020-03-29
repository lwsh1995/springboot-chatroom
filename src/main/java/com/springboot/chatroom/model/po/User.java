package com.springboot.chatroom.model.po;

import lombok.Data;

import java.util.List;

@Data
public class User {
    private String userId;
    private String username;
    private String password;
    private String avatarUrl;
    private List<User> friendList;
    private List<Group> groupList;

    public User(String userId, String username, String password, String avatarUrl) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.avatarUrl = avatarUrl;
    }
}
