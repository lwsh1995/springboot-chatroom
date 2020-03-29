package com.springboot.chatroom.service.impl;

import com.springboot.chatroom.dao.UserDao;
import com.springboot.chatroom.model.po.User;
import com.springboot.chatroom.model.vo.ResponseJson;
import com.springboot.chatroom.service.SecurityService;
import com.springboot.chatroom.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
@Slf4j
public class SecurityServiceImpl implements SecurityService {

    @Autowired
    private UserDao userDao;
    @Override
    public ResponseJson login(String username, String password, HttpSession session) {
        User user = userDao.getByUsername(username);
        if(user==null){
            return new ResponseJson().error("不存在该用户");
        }
        if (!user.getPassword().equals(password)){
            return new ResponseJson().error("密码不正确");
        }
        session.setAttribute(Constant.USER_TOKEN,user.getUserId());
        return new ResponseJson().success();
    }

    @Override
    public ResponseJson logout(HttpSession session) {
        Object userId = session.getAttribute(Constant.USER_TOKEN);
        if (userId==null){
            return new ResponseJson().error("请先登录");
        }
        session.removeAttribute(Constant.USER_TOKEN);
        log.info("userId {} 用户已注销登录",userId);
        return new ResponseJson().success();
    }
}
