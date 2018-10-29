package edu.bupt.wangfu.module.wsnMgr.util;

import edu.bupt.wangfu.module.util.MultiHandler;
import lombok.Data;

/**
 * wsn 每一个新增订阅都需要添加对应主题编码的监听
 */
@Data
public class MessageReceiver implements Runnable{
    String topic;
    int topicPort;
    String address;

    @Override
    public void run() {
        System.out.println("监听新主题：" + topic + "\t地址：" + address);
        while (true) {
            MultiHandler handler = new MultiHandler(topicPort, address);
            Object msg = handler.v6Receive();
            onMsgReceive(msg);
        }
    }

    public void onMsgReceive(Object msg) {
        System.out.println("收到主题 " + topic + " 消息：" + msg);
    }

}
