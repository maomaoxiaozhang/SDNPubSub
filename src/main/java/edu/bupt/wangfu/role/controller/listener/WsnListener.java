package edu.bupt.wangfu.role.controller.listener;

import edu.bupt.wangfu.info.device.Controller;
import edu.bupt.wangfu.info.device.User;
import edu.bupt.wangfu.info.message.system.HelloMsg;
import edu.bupt.wangfu.info.message.wsn.SubPubMsg;
import edu.bupt.wangfu.info.message.wsn.WsnMessage;
import edu.bupt.wangfu.module.routeMgr.RouteMgr;
import edu.bupt.wangfu.module.routeMgr.util.BuildTopology;
import edu.bupt.wangfu.module.routeMgr.util.Node;
import edu.bupt.wangfu.module.routeMgr.util.RouteUtil;
import edu.bupt.wangfu.module.switchMgr.odl.OvsProcess;
import edu.bupt.wangfu.module.topologyMgr.ospf.State;
import edu.bupt.wangfu.module.topologyMgr.util.Lsa;
import edu.bupt.wangfu.module.topologyMgr.util.Lsdb;
import edu.bupt.wangfu.module.util.MultiHandler;
import edu.bupt.wangfu.module.wsnMgr.util.WsnReceive;
import edu.bupt.wangfu.role.controller.ControllerStart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.*;

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

    @Autowired
    Lsdb lsdb;

    @Autowired
    Lsa localLsa;

    @Autowired
    RouteMgr routeMgr;

    @Autowired
    OvsProcess ovsProcess;

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
                if (subPubMsg.getGroup().equals(controller.getLocalGroupName())) {
                    updateSubPubMap(subPubMsg);
                }
            }
        }
    }

    /**
     * 更新本地发布订阅表信息、LSDB
     * 向邻居集群发送 LSDB 消息
     * @param msg
     */
    public void updateSubPubMap(SubPubMsg msg) {
        String type = msg.getType();
        String topic = msg.getTopic();
        User user = msg.getUser();
        String encodeAddress = msg.getEncodeAddress();
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
                topicList = localSubMap.get(user);
                if (topicList == null) {
                    topicList = new LinkedList<>();
                }
                if (topicList.contains(topic)) {
                    System.out.println("集群 " + controller.getLocalGroupName() + " 内 " + topic + " 主题已订阅");
                }else {
                    System.out.println("集群 " + groupName + " 新增订阅：user -- " + user.getAddress() + "\ttopic: " + topic);
                    topicList.add(topic);
                    localSubMap.put(user, topicList);
                    for (User user1 : controllerStart.getLocalSubPub().getLocalSubMap().keySet()) {
                        System.out.println("user: " + user.getAddress() + "\ttopicList: " +
                                controllerStart.getLocalSubPub().getLocalSubMap().get(user1));
                    }
                    List<String> subTopics = localLsa.getSubsTopics();
                    subTopics.add(topic);
                    sendLsa(topic, encodeAddress);

                    //添加当前节点为订阅该主题的节点
                    Set<Node> subNodes = routeMgr.getAllSubNodes().get(topic);
                    if (subNodes == null) {
                        subNodes = new HashSet<>();
                    }
                    Node node = BuildTopology.find(controller.getLocalGroupName(), routeMgr.getAllNodes());
                    subNodes.add(node);
                    routeMgr.getAllSubNodes().put(topic, subNodes);

                    //下发主题路径
                    RouteUtil.downTopicRtFlows(routeMgr.getAllNodes(), routeMgr.getAllSubNodes().get(topic),
                            controller, encodeAddress, ovsProcess);
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

    //主动向邻居集群发送更新的本地LSA
    public void sendLsa(String topic, String encodeAddress) {
        String groupName = controller.getLocalGroupName();
        HelloMsg hello = new HelloMsg();
        hello.setLocalGroupName(groupName);
        hello.setStartGroup(groupName);
        hello.setLsa(localLsa);
        hello.getLsa().setTopic(topic);
        hello.getLsa().setEncodeAddress(encodeAddress);
        hello.setState(State.two_way);
        hello.setType(SUB_PUB_NOTIFY);
        for (Lsa lsa : lsdb.getLSDB().values()) {
            String endGroup = lsa.getGroupName();
            if (!endGroup.equals(groupName)) {
                hello.setEndGroup(endGroup);
                String address = lsa.getAddress();
                int port = lsa.getPort();
                MultiHandler handler = new MultiHandler(port, address);
                Long time = System.currentTimeMillis();
                hello.setSendTime(time);
                hello.getLsa().setSendTime(time);
                handler.v6Send(hello);
            }
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
