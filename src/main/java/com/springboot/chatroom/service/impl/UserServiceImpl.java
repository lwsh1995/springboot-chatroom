package com.springboot.chatroom.service.impl;

import com.springboot.chatroom.dao.UserDao;
import com.springboot.chatroom.model.po.User;
import com.springboot.chatroom.model.vo.ResponseJson;
import com.springboot.chatroom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userDao;
    @Override
    public ResponseJson getByUserId(String userId) {
        User user = userDao.getByUserId(userId);
        return new ResponseJson().success().setData("userInfo",user);
    }
}
