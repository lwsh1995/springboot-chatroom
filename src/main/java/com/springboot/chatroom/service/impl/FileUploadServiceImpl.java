package com.springboot.chatroom.service.impl;

import com.springboot.chatroom.model.vo.ResponseJson;
import com.springboot.chatroom.service.FileUploadService;
import com.springboot.chatroom.util.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Slf4j
public class FileUploadServiceImpl implements FileUploadService {
    private final static String SERVER_URL_PREFIX="http://localhost:8080/WebSocket/";
    private final static String FILE_STORE_PATH="UploadFile";

    @Override
    public ResponseJson upload(MultipartFile file, HttpServletRequest request) {
        String filename = UUID.randomUUID().toString().replace("-", "");
        String suffix="";
        String originalFilename=file.getOriginalFilename();
        String formatSize = FileUtils.getFormatSize(file.getSize());
        if (originalFilename.contains(".")){
            suffix=originalFilename.substring((originalFilename.lastIndexOf(".")));
        }
        filename=filename+suffix;
        String prefix = request.getSession().getServletContext().getRealPath("/") + FILE_STORE_PATH;
        log.info("存储路径为："+prefix+"\\"+filename);
        Path filePath = Paths.get(prefix, filename);
        try{
            Files.copy(file.getInputStream(),filePath);
        }catch (IOException e){
            e.printStackTrace();
            return new ResponseJson().error("文件上传错误");
        }
        return new ResponseJson().success()
                .setData("originalFileName",originalFilename)
                .setData("fileSize",formatSize)
                .setData("fileUrl",SERVER_URL_PREFIX+FILE_STORE_PATH+"\\"+filename);
    }
}
