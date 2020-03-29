package com.springboot.chatroom.service;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;

public interface ChatService {
    void register(JSONObject param, ChannelHandlerContext ctx);
    void singleSend(JSONObject param,ChannelHandlerContext ctx);
    void groupSend(JSONObject param,ChannelHandlerContext ctx);
    void fileMsgSingleSend(JSONObject param,ChannelHandlerContext ctx);
    void fileMsgGroupSend(JSONObject param,ChannelHandlerContext ctx);
    void remove(ChannelHandlerContext ctx);
    void typeError(ChannelHandlerContext ctx);
}
