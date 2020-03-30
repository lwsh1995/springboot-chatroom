package com.springboot.chatroom.config;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NettyConfigurer {

    @Bean("bossGroup")
    NioEventLoopGroup bossGroup(){
        return new NioEventLoopGroup();
    }


    @Bean("workerGroup")
    NioEventLoopGroup workerGroup(){
        return new NioEventLoopGroup();
    }

    @Bean("serverBootstrap")
    ServerBootstrap serverBootstrap(){
        return new ServerBootstrap();
    }
}
