package com.springboot.chatroom.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * netty websocket 服务器使用独立线程启动
 */
@Component
@Slf4j
public class WebSocketServer implements Runnable {

    @Autowired
    private EventLoopGroup bossGroup;

    @Autowired
    private EventLoopGroup workerGroup;

    @Autowired
    private ServerBootstrap serverBootstrap;

    @Resource(name = "webSocketChildChannelHandler")
    private ChannelHandler childChannelHandler;

    @Value("${websocket.port}")
    private int port;

    private ChannelFuture serverChannelFuture;

    @Override
    public void run() {
        build();
    }

    public void build(){
        try {
            long begin = System.currentTimeMillis();
            serverBootstrap.group(bossGroup,workerGroup)//boss建立客户端tcp连接请求，worker负责与客户端的读写操作
                    .channel(NioServerSocketChannel.class)//配置客户端的channel类型
                    .option(ChannelOption.SO_BACKLOG,1024)//配置tcp握手字符串长度设置
                    .option(ChannelOption.TCP_NODELAY,true)//TCP_NODELAY算法，尽可能发送大块数据，减少充斥的小块数据
                    .childOption(ChannelOption.SO_KEEPALIVE,true)//开启心跳，ESTABLEISHED状态，超过2小时没交流，机制被启动
                    .childOption(ChannelOption.RCVBUF_ALLOCATOR,new FixedRecvByteBufAllocator(592048))//配置固定长度接受缓存区分配器
                    .childHandler(childChannelHandler);//绑定I/O事件的处理类，WebSocketChildChannelHandler定义
            long end = System.currentTimeMillis();
            log.info("Netty Websocket服务器启动完成，耗时 "+(end -begin) +" ms, 绑定端口 "+port);
            serverChannelFuture = serverBootstrap.bind(port).sync();
        }catch (Exception e){
            log.error(e.getMessage());
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    /**
     * 关闭netty websocket服务器，释放连接。连接包括：
     * 服务器连接serverChannel
     * 客户端TCP处理连接bossGroup
     * 客户端I/O操作连接workerGroup
     * 只使用bossGroup.shutdownGracefully() workerGroup.shutdownGracefully()会导致内存泄漏
     */
    public void close(){
        serverChannelFuture.channel().close();
        Future<?> bossGroupFuture = bossGroup.shutdownGracefully();
        Future<?> workerGroupFuture = workerGroup.shutdownGracefully();
        try {
            bossGroupFuture.await();
            workerGroupFuture.await();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
