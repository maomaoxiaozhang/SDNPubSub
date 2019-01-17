package edu.bupt.wangfu.module.wsnMgr.util;


import edu.bupt.wangfu.info.device.Controller;
import edu.bupt.wangfu.info.message.wsn.SubPubMsg;
import edu.bupt.wangfu.info.message.wsn.TopicEncodeMsg;
import edu.bupt.wangfu.info.message.wsn.WsnMessage;
import edu.bupt.wangfu.module.util.MultiHandler;
import edu.bupt.wangfu.module.wsnMgr.WsnMgr;
import edu.bupt.wangfu.role.controller.listener.WsnListener;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * wsnMgr 接收消息，保存收到的主题编码情况
 * 区别于wsnListener，负责接收本集群发布订阅情况
 *
 * @see WsnMessage
 * @see SubPubMsg
 * @see TopicEncodeMsg
 * @see WsnListener
 */

@Data
@Component
public class WsnReceive implements Runnable{
    @Autowired
    Controller controller;

    @Autowired
    @Lazy
    WsnMgr wsnMgr;

    @Override
    public void run() {
        int wsnPort = controller.getWsnPort();
        String address = controller.getWsnV6Addr();
        while (true) {
            MultiHandler handler = new MultiHandler(wsnPort, address);
            Object msg = handler.v6Receive();
            onMsgReceive(msg);
        }
    }

    /**
     * 消息处理
     * @param msg
     */
    public void onMsgReceive(Object msg) {
        System.out.println("wsnReceive 收到消息");
        if (msg instanceof WsnMessage) {
            if (msg instanceof TopicEncodeMsg) {
                TopicEncodeMsg encodeMsg = (TopicEncodeMsg) msg;
                wsnMgr.setEncodeTopicTree(encodeMsg.getTopicTree());
//                System.out.println("主题树：\n" + msg);
            }
        }
    }
}
