package com.springboot.chatroom.common;

import com.springboot.chatroom.dao.GroupDao;
import com.springboot.chatroom.dao.UserDao;
import com.springboot.chatroom.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
@Slf4j
public class AppContext {

    @Autowired
    private WebSocketServer webSocketServer;

    @Autowired
    private UserDao userDao;

    @Autowired
    private GroupDao groupDao;

    private Thread nettyThread;

    @PostConstruct
    public void init(){
        nettyThread= new Thread(webSocketServer);
        log.info("启动netty websocket服务器...");
        nettyThread.start();
        log.info("加载用户数据...");
        userDao.loadUser();
        log.info("加载群组数据...");
        groupDao.loadGroup();
    }

    @PreDestroy
    public void close(){
        log.info("释放netty websockt 连接");
        webSocketServer.close();
        log.info("关闭netty websocket 线程");
        nettyThread.stop();
    }
}
