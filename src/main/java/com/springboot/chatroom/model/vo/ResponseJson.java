package com.springboot.chatroom.model.vo;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpStatus;

import java.util.HashMap;

public class ResponseJson extends HashMap<String,Object> {
    private static final Integer SUCCESS=200;
    private static final Integer ERROR=-1;
    private static final String SUCCESS_MSG="正常";

    public ResponseJson(){
    }

    public ResponseJson(int code){
        setStatus(code);
    }

    public ResponseJson(HttpStatus status){
        setStatus(status.value());
        setMsg(status.getReasonPhrase());
    }

    public ResponseJson success(){
        put("msg",SUCCESS_MSG);
        put("status",SUCCESS);
        return this;
    }

    public ResponseJson error(String msg){
        put("msg",msg);
        put("status",ERROR);
        return this;
    }
    public ResponseJson setData(String key,Object obj){
        HashMap<String, Object> data = (HashMap<String, Object>) get("data");
        if (data==null){
            data=new HashMap<>();
            put("data",data);
        }
        data.put(key,obj);
        return this;
    }
    public ResponseJson setStatus(int status){
        put("status",status);
        return this;
    }

    public ResponseJson setMsg(String msg){
        put("msg",msg);
        return this;
    }

    public String toString(){
        return JSONObject.toJSONString(this);
    }
}
