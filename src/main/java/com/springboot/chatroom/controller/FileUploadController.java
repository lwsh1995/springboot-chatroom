package com.springboot.chatroom.controller;

import com.springboot.chatroom.model.vo.ResponseJson;
import com.springboot.chatroom.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/chatroom")
public class FileUploadController {
    @Autowired
    private FileUploadService fileUploadService;

    @PostMapping("/upload")
    @ResponseBody
    public ResponseJson upload(@RequestParam("flie")MultipartFile file, HttpServletRequest request){

        return fileUploadService.upload(file,request);
    }
}
