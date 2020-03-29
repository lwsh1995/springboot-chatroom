package com.springboot.chatroom.controller;

import com.springboot.chatroom.model.vo.ResponseJson;
import com.springboot.chatroom.service.UserService;
import com.springboot.chatroom.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/chatroom")
public class ChatroomController {
    @Autowired
    UserService userService;

    /**
     * 页面跳转
     */
    @GetMapping
    public String toChatroom(){
        return "chatroom";
    }

    @PostMapping(value = "/get_userinfo")
    @ResponseBody
    public ResponseJson getUser(HttpSession httpSession){
        String userId =(String) httpSession.getAttribute(Constant.USER_TOKEN);
        return userService.getByUserId(userId);

    }
}
