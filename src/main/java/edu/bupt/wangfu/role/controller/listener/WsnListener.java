package edu.bupt.wangfu.role.controller.listener;

import edu.bupt.wangfu.info.device.Controller;
import edu.bupt.wangfu.info.device.User;
import edu.bupt.wangfu.info.message.wsn.SubPubMsg;
import edu.bupt.wangfu.info.message.wsn.WsnMessage;
import edu.bupt.wangfu.module.util.MultiHandler;
import edu.bupt.wangfu.module.wsnMgr.util.WsnReceive;
import edu.bupt.wangfu.role.controller.ControllerStart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static edu.bupt.wangfu.module.util.Constant.*;

/**
 * 控制器的wsn监听，负责接收本集群的发布订阅消息
 *
 * @see WsnReceive
 */
@Component
public class WsnListener implements Runnable{
    @Autowired
    Controller controller;

    @Autowired
    @Lazy
    ControllerStart controllerStart;

    @Override
    public void run() {
        System.out.println("wsnListener start~~");
        int wsnPort = controller.getWsnPort();
        String address = controller.getLocalAddr();
        MultiHandler handler = new MultiHandler(wsnPort, address);
        while (true) {
            Object msg = handler.v6Receive();
            onMsgReceive(msg);
        }
    }

    /**
     *
     * @param msg
     */
    public void onMsgReceive(Object msg) {
        if (msg instanceof WsnMessage) {
            if (msg instanceof SubPubMsg) {
                SubPubMsg subPubMsg = (SubPubMsg) msg;
                updateSubPubMap(subPubMsg);
            }
        }
    }

    /**
     * 更新本地发布订阅表信息
     * @param msg
     */
    public void updateSubPubMap(SubPubMsg msg) {
        String type = msg.getType();
        String topic = msg.getTopic();
        User user = msg.getUser();
        String groupName = controller.getLocalGroupName();
        List<String> topicList;
        Map<User, List<String>> localSubMap = controllerStart.getLocalSubPub().getLocalSubMap();
        Map<User, List<String>> localPubMap = controllerStart.getLocalSubPub().getLocalPubMap();
        List<String> subList = controllerStart.getGlobalSubPub().getGlobalSubMap().get(groupName);
        List<String> pubList = controllerStart.getGlobalSubPub().getGlobalPubMap().get(groupName);
        switch (type) {
            case PUBLISH:
                topicList = localPubMap.get(user);
                topicList.add(topic);
                break;
            case SUBSCRIBE:
                System.out.println("集群 " + groupName + " 新增订阅：user -- " + user.getAddress() + "\ttopic: " + topic);
                topicList = localSubMap.get(user);
                if (topicList == null) {
                    topicList = new LinkedList<>();
                }
                topicList.add(topic);
                localSubMap.put(user, topicList);
                for (User user1 : controllerStart.getLocalSubPub().getLocalSubMap().keySet()) {
                    System.out.println("user: " + user.getAddress() + "\ttopicList: " +
                            controllerStart.getLocalSubPub().getLocalSubMap().get(user1));
                }
                break;
            case CAL_PUBLISH:
                if (isAlone(topic, localPubMap)) {
                    pubList.remove(topic);
                }
                topicList = localPubMap.get(user);
                if (topicList.size() != 0) {
                    topicList.remove(topic);
                }
                break;
            case CAL_SUBSCRIBE:
                if (isAlone(topic, localSubMap)) {
                    subList.remove(topic);
                }
                topicList = localSubMap.get(user);
                if (topicList.size() != 0) {
                    topicList.remove(topic);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 判断主题是否只出现一次
     * @param topic
     * @param notifyMap
     * @return
     */
    public boolean isAlone(String topic, Map<User, List<String>> notifyMap) {
        int count = 0;
        for (List<String> list : notifyMap.values()) {
            if (list.contains(topic)) {
                count++;
            }
        }
        return count == 1;
    }
}
