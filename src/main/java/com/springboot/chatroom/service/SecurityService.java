package com.springboot.chatroom.service;

import com.springboot.chatroom.model.vo.ResponseJson;

import javax.servlet.http.HttpSession;

public interface SecurityService {
    ResponseJson login(String username,String password,HttpSession session);
    ResponseJson logout(HttpSession session);
}
