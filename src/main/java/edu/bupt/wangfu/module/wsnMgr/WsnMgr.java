package edu.bupt.wangfu.module.wsnMgr;

import edu.bupt.wangfu.config.ControllerConfig;
import edu.bupt.wangfu.info.device.Controller;
import edu.bupt.wangfu.info.device.User;
import edu.bupt.wangfu.module.topicTreeMgr.topicTree.EncodeTopicTree;
import edu.bupt.wangfu.module.util.MultiHandler;
import edu.bupt.wangfu.module.util.store.LocalSubPub;
import edu.bupt.wangfu.module.wsnMgr.reactor.*;
import edu.bupt.wangfu.module.wsnMgr.util.WsnReceive;
import edu.bupt.wangfu.module.wsnMgr.util.soap.wsn.PublishNotificationProcessImpl;
import edu.bupt.wangfu.module.wsnMgr.util.soap.wsn.WsnNotificationProcessImpl;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import javax.xml.ws.Endpoint;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import static edu.bupt.wangfu.module.util.Constant.PUBLISH;
import static edu.bupt.wangfu.module.util.Constant.wsnAddr;
import static edu.bupt.wangfu.module.wsnMgr.util.GenPubAddress.getPubAddress;

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
 *
 *      发布注册、订阅、用户配置 -- wsnAddr，使用线程池管理接收消息后发布的线程；同时在本地保存
 *                           主题线程的监听，取消订阅后直接关闭线程
 *      发布消息 -- 单独的 PublishAddr，防止发布消息过多造成负载过大
 * </p>
 *
 * 技术演变：由于消息的发送量过大，如果每个消息到来时单开线程处理，对机器负载压力过大
 *      1. 消息 --> 线程
 *      2. 消息 --> 线程池，方便管理工作者线程的生命周期
 *      3. 生产者消费者模型，针对用户构建线程，消息封装为任务存在阻塞队列中
 *          registerSub : 新用户生成对应的消费者并保存在 allConsumers
 *          addListener、MessageReceiver : 添加对应主题的监听，消息到来时封装为任务推入对应消费者的阻塞队列中
 *
 * @see WsnReceive
 */

@Data
@Component
public class WsnMgr {
    @Autowired
    LocalSubPub localSubPub;

    //编码主题树，接收自控制器
    EncodeTopicTree encodeTopicTree = new EncodeTopicTree();

    @Autowired
    WsnReceive wsnReceive;

    @Autowired
    WsnNotificationProcessImpl wsnNotificationProcess;

    @Autowired
    Controller controller;

    //针对主题的监听
    Map<String, Thread> topicListeners = new HashMap<>();

    @Autowired
    Dispatcher dispatcher;

    @Autowired
    Selector selector;

    public void start() {
        new Thread(wsnReceive, "wsnReceive监听").start();
        //开启发布订阅注册服务
        Endpoint endpint = Endpoint.publish(wsnAddr, wsnNotificationProcess);
    }

    /**
     * 根据用户信息更新本地订阅表
     * 如果是新用户则生成用户对应的消费者，并保存起来
     */
    public void registerSub(User user, String topic) {
        Map<User, List<String>> localSubMap = localSubPub.getLocalSubMap();
        List<String> subList = localSubMap.get(user);
        if (subList == null) {
            //当前用户未注册，则添加至本地订阅表，并生成新的handle
            subList = new LinkedList<>();
            Handle handle = new UserHandle(user);
            dispatcher.registerHandle(user, handle);
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
                String listenerName = topic + "Listener";
                Thread thread = new Thread(messageReceiver, listenerName);
                //将新增的主题监听线程保存，集中管理监听线程的生命周期
                topicListeners.put(listenerName, thread);
                thread.start();
            }else {
                System.out.println(topic + " 该主题已监听，请勿重复订阅");
                return "";
            }
        }
        return address;
    }

    /**
     * 取消主题对应的监听
     * 使用线程的 stop 方法
     * @param topic
     */
    public void calListener(String topic) {
        String listenerName = topic + "Listener";
        for (String name : topicListeners.keySet()) {
            if (name.equals(listenerName)) {
                System.out.println("停止主题 " + topic + " 对应的监听！");
                Thread thread = topicListeners.get(name);
                thread.stop();
            }
        }
    }

    //发布注册
    public String registerPub(User user, String topic) {
        Map<User, List<String>> localPubMap = localSubPub.getLocalPubMap();
        String address = encodeTopicTree.getAddress(topic);
        String publishAddress  = getPubAddress(localPubMap.keySet(), user);
        if (address == null || address.equals("")) {
            System.out.println("主题 " + topic + " 对应的编码不存在，注册失败！");
        }else {
            if (isNewSubPub(topic, localPubMap)) {
                //新的发布注册请求，向控制器上报发布信息
                wsnNotificationProcess.send2controller(topic, user, address, PUBLISH);
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

            if (!publishAddress.equals("")) {
                System.out.println("新增发布监听: " + publishAddress);
                PublishNotificationProcessImpl impl = new PublishNotificationProcessImpl(encodeTopicTree, controller);
                Endpoint endpoint = Endpoint.publish(publishAddress, impl);
            }
        }
        return publishAddress;
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

    public class MessageReceiver implements Runnable{

        String topic;

        int topicPort;

        String address;

        @Override
        public void run() {
            System.out.println("监听新主题：" + topic + "\t地址：" + address);
            MultiHandler handler = new MultiHandler(topicPort, address);
            while (true) {
                Object msg = handler.v6Receive();
                onMsgReceive(msg);
            }
        }

        /**
         * 收到主题消息，分发给订阅该主题的用户
         * 采用线程池的方式，多线程传输减少阻塞
         */
        public void onMsgReceive(Object msg) {
            System.out.println("收到主题 " + topic + " 消息：" + msg);
            Task task = new Task();
            task.setTopic(topic);
            task.setMsg(msg);
            selector.addTask(task);
        }
    }

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ControllerConfig.class);
        WsnMgr wsnMgr = (WsnMgr) context.getBean("wsnMgr");
        wsnMgr.start();
    }
}
