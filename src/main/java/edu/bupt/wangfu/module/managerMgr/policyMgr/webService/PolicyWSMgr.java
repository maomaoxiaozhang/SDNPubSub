package edu.bupt.wangfu.module.managerMgr.policyMgr.webService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.ws.Endpoint;

@Component
public class PolicyWSMgr {
    @Autowired
    PolicyRequestProcessImpl policyRequestProcess;
    private String localAddr = "http://10.108.166.57:55556/policyMgr";
    public void start() {
        //开启发布订阅注册服务
        Endpoint endpint = Endpoint.publish(localAddr, policyRequestProcess);
    }

//    public static void main(String[] args) {
//        PolicyWSMgr topicWSMgr = new PolicyWSMgr();
//        topicWSMgr.start();
//    }
}
