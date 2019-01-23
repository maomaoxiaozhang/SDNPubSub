package edu.bupt.wangfu.module.topicMgr.ldap.webService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.ws.Endpoint;

@Component
public class TopicWSMgr {
    @Autowired
    TopicRequestProcessImpl topicRequestProcess;
    private String localAddr = "http://10.108.166.57:55555/topicMgr";
    public void start() {
        //开启发布订阅注册服务
        Endpoint endpint = Endpoint.publish(localAddr, topicRequestProcess);
    }

//    public static void main(String[] args) {
//        TopicWSMgr topicWSMgr = new TopicWSMgr();
//        topicWSMgr.start();
//    }
}

