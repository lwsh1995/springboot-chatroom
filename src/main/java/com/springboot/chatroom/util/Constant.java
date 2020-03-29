package com.springboot.chatroom.util;

import com.springboot.chatroom.model.po.Group;
import com.springboot.chatroom.model.po.User;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Constant {
    public static final String USER_TOKEN="userId";

    public static Map<String, WebSocketServerHandshaker> webSocketServerHandshakerMap=
            new ConcurrentHashMap<>();

    public static Map<String, ChannelHandlerContext> onlineUserMap=
            new ConcurrentHashMap<>();

    public static Map<String, Group> groupMap =
            new ConcurrentHashMap<>();

    public static Map<String, User> userMap=
            new HashMap<>();

}
