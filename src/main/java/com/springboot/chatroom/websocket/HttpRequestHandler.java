package com.springboot.chatroom.websocket;

import com.springboot.chatroom.util.Constant;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;
import org.springframework.stereotype.Component;

@Component
@ChannelHandler.Sharable
public class HttpRequestHandler extends SimpleChannelInboundHandler<Object> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest){
            handleHttpRequest(ctx,(FullHttpRequest)msg);
        }else if (msg instanceof WebSocketFrame){
            ctx.fireChannelRead((WebSocketFrame)((WebSocketFrame) msg).retain());
        }
    }

    /**
     * 处理HTTP请求，完成HTTP协议到WebSocket协议的升级
     */
    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest request){
        if (!request.decoderResult().isSuccess()){
            sendHttpResponse(ctx,request,new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory("ws:/" + ctx.channel() + "/websocket", null, false);
        WebSocketServerHandshaker handshaker = wsFactory.newHandshaker(request);
        Constant.webSocketServerHandshakerMap.put(ctx.channel().id().asLongText(),handshaker);
        if (handshaker==null){
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        }else {
            handshaker.handshake(ctx.channel(),request);
        }
    }



    private void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest request, DefaultFullHttpResponse res){
        // 返回客户端
        if (res.status().code()!=200){
            ByteBuf byteBuf = Unpooled.copiedBuffer(res.status().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(byteBuf);
            byteBuf.release();
        }
        // 如果是非KeepAlive，关闭连接
        boolean keepAlive = HttpUtil.isKeepAlive(request);
        ChannelFuture future = ctx.channel().writeAndFlush(res);
        if (!keepAlive){
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    /**
     * 异常处理关闭channel
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
