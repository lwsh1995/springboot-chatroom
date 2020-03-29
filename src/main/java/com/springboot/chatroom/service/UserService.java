package com.springboot.chatroom.service;

import com.springboot.chatroom.model.vo.ResponseJson;

public interface UserService {
    ResponseJson getByUserId(String userId);
}
