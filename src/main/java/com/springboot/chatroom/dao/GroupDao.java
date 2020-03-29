package com.springboot.chatroom.dao;

import com.springboot.chatroom.model.po.Group;

public interface GroupDao {
    void loadGroup();

    Group getByGroupId(String groupId);
}
