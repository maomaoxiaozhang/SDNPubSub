package edu.bupt.wangfu.module.wsnMgr.util.soap.wsn;

import edu.bupt.wangfu.info.device.Controller;
import edu.bupt.wangfu.module.topicTreeMgr.topicTree.EncodeTopicTree;
import edu.bupt.wangfu.module.util.MultiHandler;
import edu.bupt.wangfu.module.wsnMgr.WsnMgr;
import edu.bupt.wangfu.module.wsnMgr.util.soap.INotificationProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.jws.WebService;

/**
 * 负责消息的发布
 */
@WebService(endpointInterface= "edu.bupt.wangfu.module.wsnMgr.util.soap.INotificationProcess",
        serviceName="WsnNotificationProcessImpl")
public class PublishNotificationProcessImpl implements INotificationProcess{

    public PublishNotificationProcessImpl() {
    }

    EncodeTopicTree encodeTopicTree;

    Controller controller;

    public PublishNotificationProcessImpl(EncodeTopicTree encodeTopicTree, Controller controller) {
        this.encodeTopicTree = encodeTopicTree;
        this.controller = controller;
    }

    //发布
    @Override
    public void notificationProcess(String notification) {
        String message = splitString(notification, "<message>", "</message>");
        String topic = splitString(notification, "<topic>", "</topic>");
        //发布主题已经注册，直接传输message
        String encodeAddress = encodeTopicTree.getAddress(topic);
        if (encodeAddress == null) {
            System.out.println("该主题未找到编码，无法传输！");
        }else {
            send2user(encodeAddress, message);
        }
    }

    public String splitString(String string, String start, String end)
    {
        int from = string.indexOf(start) + start.length();
        int to = string.indexOf(end);
        return string.substring(from, to);
    }

    //直接发送给用户
    public void send2user(String address, String msg) {
        int port = controller.getTopicPort();
        MultiHandler handler = new MultiHandler(port, address);
        handler.v6Send(msg);
//        System.out.println("向用户发消息：" + msg);
    }
}
