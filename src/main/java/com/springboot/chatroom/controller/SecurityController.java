package com.springboot.chatroom.controller;

import com.springboot.chatroom.model.vo.ResponseJson;
import com.springboot.chatroom.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class SecurityController {
    @Autowired
    SecurityService securityService;
    @GetMapping(value = {"login","/"})
    public String toLogin(){
        return "login";
    }

    @PostMapping("login")
    @ResponseBody
    public ResponseJson login(HttpSession session,
                              @RequestParam String username,
                              @RequestParam String password){
        return securityService.login(username,password,session);
    }

    @PostMapping("/logout")
    @ResponseBody
    public ResponseJson logout(HttpSession session){
        return securityService.logout(session);
    }
}
