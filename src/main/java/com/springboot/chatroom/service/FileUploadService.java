package com.springboot.chatroom.service;

import com.springboot.chatroom.model.vo.ResponseJson;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface FileUploadService {
    ResponseJson upload(MultipartFile file, HttpServletRequest request);
}
