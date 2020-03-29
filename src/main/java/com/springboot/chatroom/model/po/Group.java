package com.springboot.chatroom.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Group {
    private String groupId;
    private String groupName;
    private String groupAvatarUrl;
    private List<User> members;


}
