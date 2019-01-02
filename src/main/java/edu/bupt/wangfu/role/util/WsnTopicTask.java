package edu.bupt.wangfu.role.util;

import edu.bupt.wangfu.info.device.Controller;
import edu.bupt.wangfu.info.message.wsn.TopicEncodeMsg;
import edu.bupt.wangfu.module.topicTreeMgr.topicTree.EncodeTopicTree;
import edu.bupt.wangfu.module.util.MultiHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.TimerTask;

@Component
public class WsnTopicTask extends TimerTask {
    @Autowired
    Controller controller;

    @Autowired
    EncodeTopicTree encodeTopicTree;

    @Override
    public void run() {
        int wsnPort = controller.getWsnPort();
        String address = controller.getWsnV6Addr();
        MultiHandler handler = new MultiHandler(wsnPort, address);
        TopicEncodeMsg msg = new TopicEncodeMsg();
        msg.setTopicTree(encodeTopicTree);
        handler.v6Send(msg);
        System.out.println("向wsnReceiver发送消息，大小：" + encodeTopicTree.getSize());
    }
}
