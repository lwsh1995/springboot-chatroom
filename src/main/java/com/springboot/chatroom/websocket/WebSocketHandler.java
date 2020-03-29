package com.springboot.chatroom.websocket;

import com.alibaba.fastjson.JSONObject;
import com.springboot.chatroom.model.vo.ResponseJson;
import com.springboot.chatroom.service.ChatService;
import com.springboot.chatroom.util.Constant;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ChannelHandler.Sharable
public class WebSocketHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
    @Autowired
    ChatService chatService;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, WebSocketFrame webSocketFrame) throws Exception {
        handlerWebSocketFrame(channelHandlerContext,webSocketFrame);
    }

    /**
     * 处理WebSocketFrame
     */
    private void handlerWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
        //关闭请求
        if (frame instanceof CloseWebSocketFrame) {
            WebSocketServerHandshaker handshaker = Constant.webSocketServerHandshakerMap.get(ctx.channel().id().asLongText());
            if (handshaker == null) {
                sendErrorMessage(ctx, "不存在客户端连接！");
            } else {
                handshaker.close(ctx.channel(), ((CloseWebSocketFrame) frame).retain());
            }
            return;
        }
        //ping请求
        if (frame instanceof PingWebSocketFrame) {
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        //支持文本格式消息
        if (!(frame instanceof TextWebSocketFrame)) {
            sendErrorMessage(ctx, "仅支持text文本类型");
        }

        //客户端的消息
        String request = ((TextWebSocketFrame) frame).text();
        JSONObject param = null;
        try {
            param = JSONObject.parseObject(request);
        } catch (Exception e) {
            sendErrorMessage(ctx, "JSON转换出错");
            e.printStackTrace();
        }
        if (param == null) {
            sendErrorMessage(ctx, "参数为空");
            return;
        }
        String type = param.getString("type");
        switch (type) {
            case "REGISTER":
                chatService.register(param, ctx);
                break;
            case "SINGLE_SENDING":
                chatService.singleSend(param, ctx);
                break;
            case "GROUP_SENDING":
                chatService.groupSend(param, ctx);
                break;
            case "FILE_MSG_SINGLE_SENDING":
                chatService.fileMsgSingleSend(param,ctx);
                break;
            case "FILE_MSG_GROUP_SENDING":
                chatService.fileMsgGroupSend(param,ctx);
                break;
            default:
                chatService.typeError(ctx);
                break;
        }

    }

    /**
     * 客户端断开连接
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        chatService.remove(ctx);
    }

    /**
     * 异常处理：关闭channel
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    private void sendErrorMessage(ChannelHandlerContext ctx, String errorMsg) {
        String resp = new ResponseJson().error(errorMsg).toString();
        ctx.channel().writeAndFlush(new TextWebSocketFrame(resp));
    }
}
