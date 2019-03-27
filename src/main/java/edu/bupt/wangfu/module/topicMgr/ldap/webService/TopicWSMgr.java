package edu.bupt.wangfu.module.topicMgr.ldap.webService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.ws.Endpoint;

@Component
public class TopicWSMgr {
    @Autowired
    TopicRequestProcessImpl topicRequestProcess;
    private String localAddr = "http://localhost:55555/topicMgr";
    public void start() {
        //开启发布订阅注册服务
        Endpoint endpint = Endpoint.publish(localAddr, topicRequestProcess);
    }
}

