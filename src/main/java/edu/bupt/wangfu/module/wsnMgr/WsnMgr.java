package edu.bupt.wangfu.module.wsnMgr;

import edu.bupt.wangfu.config.ControllerConfig;
import edu.bupt.wangfu.info.device.Controller;
import edu.bupt.wangfu.info.device.User;
import edu.bupt.wangfu.module.topicTreeMgr.topicTree.EncodeTopicTree;
import edu.bupt.wangfu.module.util.store.LocalSubPub;
import edu.bupt.wangfu.module.wsnMgr.util.MessageReceiver;
import edu.bupt.wangfu.module.wsnMgr.util.WsnReceive;
import edu.bupt.wangfu.module.wsnMgr.util.soap.wsn.WsnNotificationProcessImpl;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import javax.xml.ws.Endpoint;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 在用户端使用
 * wsn管理层，负责两部分内容：
 *     1. wsnMgr -- controller
 *        1.1 controller -> wsn
 *            接收并保存主题树编码
 *        1.2 wsn -> controller
 *            发送本地订阅情况
 *     2. user -> wsn
 *        接收并保存本地所有用户及其对应的主题发布、订阅情况
 *
 * @see WsnReceive
 */

@Data
@Component
public class WsnMgr {
    LocalSubPub localSubPub = new LocalSubPub();

    //编码主题树，接收自控制器
    EncodeTopicTree encodeTopicTree = new EncodeTopicTree();

    @Autowired
    WsnReceive wsnReceive;

    @Autowired
    WsnNotificationProcessImpl implementor;

    @Autowired
    Controller controller;

    private static final String wsnAddr = "http://192.168.10.100:9010/wsn-core";

    public void start() {
        new Thread(wsnReceive, "wsnReceive监听").start();
        Endpoint endpint = Endpoint.publish(wsnAddr, implementor);// 开启接收服务
    }

    /**
     * 根据用户信息更新本地订阅表
     * 每一个新增订阅都需要添加新的监听
     */
    public String updateSubPubMap(User user, String topic) {
        Map<User, List<String>> localSubMap = localSubPub.getLocalSubMap();
        String address = encodeTopicTree.getAddress(topic);
        if (address == null || address.equals("")) {
            System.out.println("主题 " + topic + " 对应的编码不存在，订阅失败！");
        }else {
            if (isNewSub(topic, localSubMap)) {
                MessageReceiver messageReceiver = new MessageReceiver();
                messageReceiver.setTopic(topic);
                messageReceiver.setTopicPort(controller.getTopicPort());
                messageReceiver.setAddress(address);
                new Thread(messageReceiver, topic + "Listener").start();
            }else {
                System.out.println(topic + " 该主题已监听，请勿重复订阅");
            }
            List<String> subList = localSubMap.get(user);
            if (subList == null) {
                subList = new LinkedList<>();
            }
            if (!subList.contains(topic)) {
                subList.add(topic);
            }
            localSubMap.put(user, subList);
        }
        return address;
    }

    /**
     * 判断主题是否为集群内新增订阅，是则需要添加单独的监听
     * @param topic
     * @param localSubMap
     * @return
     */
    public boolean isNewSub(String topic, Map<User, List<String>> localSubMap) {
        for (User user : localSubMap.keySet()) {
            List<String> list = localSubMap.get(user);
            if (list!= null && list.contains(topic)) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ControllerConfig.class);
        WsnMgr wsnMgr = (WsnMgr) context.getBean("wsnMgr");
        wsnMgr.start();
    }
}
