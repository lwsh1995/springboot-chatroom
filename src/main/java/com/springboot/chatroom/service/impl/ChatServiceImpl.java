package com.springboot.chatroom.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.springboot.chatroom.dao.GroupDao;
import com.springboot.chatroom.model.po.Group;
import com.springboot.chatroom.model.vo.ResponseJson;
import com.springboot.chatroom.service.ChatService;
import com.springboot.chatroom.util.ChatType;
import com.springboot.chatroom.util.Constant;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Map;

@Service
@Slf4j
public class ChatServiceImpl implements ChatService {
    @Autowired
    private GroupDao groupDao;

    @Override
    public void register(JSONObject param, ChannelHandlerContext ctx) {
        String userId = param.getString("userId");
        Constant.onlineUserMap.put(userId,ctx);
        String resp = new ResponseJson().success().setData("type", ChatType.REGISTER).toString();
        sendMessage(ctx,resp);
        log.info(MessageFormat.format("userId {0} 用户登录，当前在线人数：{1}",userId,Constant.onlineUserMap.size()));
    }

    @Override
    public void singleSend(JSONObject param, ChannelHandlerContext ctx) {
        String fromUserId = param.getString("fromUserId");
        String toUserId = param.getString("toUserId");
        String content = param.getString("content");
        ChannelHandlerContext toCtx = Constant.onlineUserMap.get(toUserId);
        if (toCtx==null){
            String error = new ResponseJson().error(MessageFormat.format("userId {0} 用户没有登录", toUserId)).toString();
            sendMessage(ctx,error);
        }else {
            String resp = new ResponseJson().success().setData("fromUserId", fromUserId)
                    .setData("content", content)
                    .setData("type", ChatType.SINGLE_SENDING)
                    .toString();
            sendMessage(toCtx,resp);

        }
    }

    @Override
    public void groupSend(JSONObject param, ChannelHandlerContext ctx) {
        String fromUserId = param.getString("fromUserId");
        String toGroupId = param.getString("toGroupId");
        String content = param.getString("content");
        Group group = groupDao.getByGroupId(toGroupId);
        if (group==null){
            String error = new ResponseJson().error("该群id不存在").toString();
            sendMessage(ctx,error);
        }else {
            String resp = new ResponseJson().success()
                    .setData("fromUserId", fromUserId)
                    .setData("content", content)
                    .setData("toGroupId", toGroupId)
                    .setData("type", ChatType.GROUP_SENDING)
                    .toString();
            group.getMembers().stream().forEach(member ->{
                ChannelHandlerContext toCtx = Constant.onlineUserMap.get(member.getUserId());
                if (toCtx!=null&&!member.getUserId().equals(fromUserId)){
                    sendMessage(toCtx,resp);

                }

            });
        }
    }

    @Override
    public void fileMsgSingleSend(JSONObject param, ChannelHandlerContext ctx) {
        String fromUserId = param.getString("fromUserId");
        String toUserId = param.getString("toUserId");
        String originalFilename = param.getString("originalFilename");
        String fileSize = param.getString("fileSize");
        String fileUrl = param.getString("fileUrl");
        ChannelHandlerContext toCtx = Constant.onlineUserMap.get(toUserId);
        if (toCtx==null){
            String error = new ResponseJson().error(MessageFormat.format("userId {0} 没有登录", toUserId)).toString();
            sendMessage(ctx,error);
        }else {
            String resp = new ResponseJson().success()
                    .setData("fromUserId", fromUserId)
                    .setData("originalFileName", originalFilename)
                    .setData("fileSize", fileSize)
                    .setData("fileUrl", fileUrl)
                    .setData("type", ChatType.FILE_MSG_SINGLE_SENDING)
                    .toString();
            sendMessage(toCtx,resp);
        }
    }

    @Override
    public void fileMsgGroupSend(JSONObject param, ChannelHandlerContext ctx) {
        String fromUserId = param.getString("fromUserId");
        String toGroupId = param.getString("toGroupId");
        String originalFilename = param.getString("originalFilename");
        String fileSize = param.getString("fileSize");
        String fileUrl = param.getString("fileUrl");
        Group group = groupDao.getByGroupId(toGroupId);
        if (group==null){
            String error = new ResponseJson().error("该群不存在").toString();
            sendMessage(ctx,error);
        }else {
            String resp = new ResponseJson().success()
                    .setData("fromUserId", fromUserId)
                    .setData("toGroupId",toGroupId)
                    .setData("originalFileName", originalFilename)
                    .setData("fileSize", fileSize)
                    .setData("fileUrl", fileUrl)
                    .setData("type", ChatType.FILE_MSG_GROUP_SENDING)
                    .toString();
            group.getMembers().stream().forEach(member -> {
                ChannelHandlerContext toCtx = Constant.onlineUserMap.get(member.getUserId());
                if (toCtx!=null&&!member.getUserId().equals(fromUserId)){
                    sendMessage(toCtx,resp);
                }
            });
        }
    }

    @Override
    public void remove(ChannelHandlerContext ctx) {
        Iterator<Map.Entry<String, ChannelHandlerContext>> iterator =
                Constant.onlineUserMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, ChannelHandlerContext> next = iterator.next();
            if (next.getValue()==ctx){
                log.info("正在移除握手实例...");
                Constant.webSocketServerHandshakerMap.remove(ctx.channel().id().asLongText());
                log.info(MessageFormat.format("已移除实例，当前实例总数: {0}",Constant.webSocketServerHandshakerMap.size()));
                iterator.remove();
                log.info(MessageFormat.format("userId {0} 用户退出聊天，当前在线人数：{1}",next.getKey(),Constant.onlineUserMap.size()));
                break;
            }
        }
    }

    @Override
    public void typeError(ChannelHandlerContext ctx) {
        String resp = new ResponseJson().error("该类型不存在").toString();
        sendMessage(ctx,resp);
    }

    private void sendMessage(ChannelHandlerContext ctx,String message){
        ctx.channel().writeAndFlush(new TextWebSocketFrame(message));
    }
}
