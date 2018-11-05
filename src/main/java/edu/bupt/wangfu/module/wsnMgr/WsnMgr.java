package edu.bupt.wangfu.module.wsnMgr;

import edu.bupt.wangfu.config.ControllerConfig;
import edu.bupt.wangfu.info.device.Controller;
import edu.bupt.wangfu.info.device.User;
import edu.bupt.wangfu.module.topicTreeMgr.topicTree.EncodeTopicTree;
import edu.bupt.wangfu.module.util.MultiHandler;
import edu.bupt.wangfu.module.util.store.LocalSubPub;
import edu.bupt.wangfu.module.wsnMgr.util.WsnReceive;
import edu.bupt.wangfu.module.wsnMgr.util.soap.SendWSNCommandWSSyn;
import edu.bupt.wangfu.module.wsnMgr.util.soap.wsn.WsnNotificationProcessImpl;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import javax.xml.ws.Endpoint;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static edu.bupt.wangfu.module.util.Constant.PUBLISH;

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
 * <p>
 *     订阅：
 *          接收用户数据，向控制器发送订阅请求，由控制器下发主题路径
 *     发布：
 *          1. 注册：向控制器发送发布注册请求，由控制器下发主题路径
 *          2. 传输：向用户主题地址发送数据
 * </p>
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

    private static final String wsnAddr = "http://192.168.10.101:9010/wsn-core";

    public void start() {
        new Thread(wsnReceive, "wsnReceive监听").start();
        Endpoint endpint = Endpoint.publish(wsnAddr, implementor);// 开启接收服务
    }

    /**
     * 根据用户信息更新本地订阅表
     */
    public void registerSub(User user, String topic) {
        Map<User, List<String>> localSubMap = localSubPub.getLocalSubMap();
        List<String> subList = localSubMap.get(user);
        if (subList == null) {
            subList = new LinkedList<>();
        }
        if (!subList.contains(topic)) {
            subList.add(topic);
        }
        localSubMap.put(user, subList);
    }

    /**
     * 每一个新增订阅都需要添加新的监听
     */
    public String addListener(String topic) {
        String address = encodeTopicTree.getAddress(topic);
        Map<User, List<String>> localSubMap = localSubPub.getLocalSubMap();
        if (address == null || address.equals("")) {
            System.out.println("主题 " + topic + " 对应的编码不存在，订阅失败！");
        }else {
            if (isNewSubPub(topic, localSubMap)) {
                MessageReceiver messageReceiver = new MessageReceiver();
                messageReceiver.topic = topic;
                messageReceiver.topicPort = controller.getTopicPort();
                messageReceiver.address = address;
                new Thread(messageReceiver, topic + "Listener").start();
            }else {
                System.out.println(topic + " 该主题已监听，请勿重复订阅");
                return "";
            }
        }
        return address;
    }

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

        //收到主题消息，分发给订阅该主题的用户
        public void onMsgReceive(Object msg) {
            System.out.println("收到主题 " + topic + " 消息：" + msg);
            Map<User, List<String>> localSubMap = localSubPub.getLocalSubMap();
            for (User user : localSubMap.keySet()) {
                if (localSubMap.get(user).contains(topic)) {
                    String userAddress = user.getAddress();
                    SendWSNCommandWSSyn send = new SendWSNCommandWSSyn("", userAddress);
                    send.notify(topic, String.valueOf(msg));
                }
            }
        }
    }

    //发布注册
    public String registerPub(User user, String topic) {
        Map<User, List<String>> localPubMap = localSubPub.getLocalPubMap();
        String address = encodeTopicTree.getAddress(topic);
        if (address == null || address.equals("")) {
            System.out.println("主题 " + topic + " 对应的编码不存在，注册失败！");
        }else {
            if (isNewSubPub(topic, localPubMap)) {
                //新的发布注册请求，向控制器上报发布信息
                implementor.send2controller(topic, user, address, PUBLISH);
                //添加至本地发布表中
                List<String> pubList = localPubMap.get(user);
                if (pubList == null) {
                    pubList = new LinkedList<>();
                }
                if (!pubList.contains(topic)) {
                    pubList.add(topic);
                }
                localPubMap.put(user, pubList);
            }else {
                System.out.println(topic + " 该主题已注册，请勿重复发布");
            }
        }
        return address;
    }

    /**
     * 判断主题是否为集群内新增订阅，是则需要添加单独的监听
     */
    public boolean isNewSubPub(String topic, Map<User, List<String>> map) {
        for (User user : map.keySet()) {
            List<String> list = map.get(user);
            if (list!= null && list.contains(topic)) {
                return false;
            }
        }
        return true;
    }

    public User findUser(String id) {
        for (User user : localSubPub.getLocalSubMap().keySet()) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ControllerConfig.class);
        WsnMgr wsnMgr = (WsnMgr) context.getBean("wsnMgr");
        wsnMgr.start();
    }
}
