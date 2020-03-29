package com.springboot.chatroom.websocket;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class WebSocketChildChannelHandler extends ChannelInitializer<SocketChannel> {
    @Resource(name = "webSocketHandler")
    private ChannelHandler webSocketServerHandler;

    @Resource(name = "httpRequestHandler")
    private ChannelHandler httpRequestHandler;
    @Override
    protected void initChannel(SocketChannel sc) throws Exception {
        sc.pipeline().addLast("http-codec",new HttpServerCodec());// http 编码解码器
        sc.pipeline().addLast("aggregator",new HttpObjectAggregator(65535)); //http头、http体拼成完整的http请求
        sc.pipeline().addLast("http-chunked",new ChunkedWriteHandler());//方便大文件传输，实质上都是短的文本数据
        sc.pipeline().addLast("http-handler",httpRequestHandler);
        sc.pipeline().addLast("websocket-handler",webSocketServerHandler);
    }
}
