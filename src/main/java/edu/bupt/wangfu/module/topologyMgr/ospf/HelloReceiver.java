package edu.bupt.wangfu.module.topologyMgr.ospf;

import edu.bupt.wangfu.info.device.Controller;
import edu.bupt.wangfu.info.device.Flow;
import edu.bupt.wangfu.info.device.Switch;
import edu.bupt.wangfu.info.message.system.HelloMsg;
import edu.bupt.wangfu.module.routeMgr.RouteMgr;
import edu.bupt.wangfu.module.routeMgr.util.Node;
import edu.bupt.wangfu.module.routeMgr.util.RouteUtil;
import edu.bupt.wangfu.module.switchMgr.odl.OvsProcess;
import edu.bupt.wangfu.module.topicTreeMgr.TopicTreeMgr;
import edu.bupt.wangfu.module.routeMgr.util.BuildTopology;
import edu.bupt.wangfu.module.topologyMgr.util.Lsa;
import edu.bupt.wangfu.module.topologyMgr.util.Lsdb;
import edu.bupt.wangfu.module.util.MultiHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static edu.bupt.wangfu.module.util.Constant.*;

/**
 * 监听Hello消息，被动接受来自其他集群的连接建立请求
 */
@Component
public class HelloReceiver implements Runnable {
    @Autowired
    private Controller controller;

    @Autowired
    RouteMgr routeMgr;

    @Autowired
    TopicTreeMgr topicTreeMgr;

    @Autowired
    OvsProcess ovsProcess;

    @Autowired
    Lsdb lsdb;

    @Autowired
    Lsa localLsa;

    @Override
    public void run() {
        int sysPort = controller.getSysPort();
        String address = controller.getSysV6Addr();
        MultiHandler handler = new MultiHandler(sysPort, address);
        System.out.println("OSPF 拓扑发现，port：" + sysPort + "\taddress：" +address);
        //持续监听，等待Hello消息
        while (true) {
            HelloMsg msg = (HelloMsg) handler.v6Receive();
            onMsgReceive(msg);
        }
    }

    /**
     * 当收到ospf的Hello消息后，需要根据消息状态，区分处理
     *
     * @param msg
     */
    public void onMsgReceive(HelloMsg msg) {
        if (!msg.getStartGroup().equals(controller.getLocalGroupName())) {
            //第一次握手，收到 Hello 消息，返回 ReHello
            if (msg.getEndGroup() == null && msg.getState() == State.down && msg.getType().equals(HELLO)) {
                System.out.println("收到 Hello 消息，第一次握手~~ " + msg);
                if (isValid(msg.getSendTime(), HELLO)) {
                    new Thread(new OnHelloClass(msg)).start();
                }
            }else if (msg.getEndGroup().equals(controller.getLocalGroupName()) && msg.getState() == State.init
                    && msg.getType().equals(RE_HELLO)) {
                //第二次握手，收到 ReHello 消息，返回 FinalHello，本地保存邻居集群信息
//                if (isValid(msg.getSendTime(), RE_HELLO)) {
                    System.out.println("收到 ReHello 消息，第二次握手！！" + msg);
                    update(msg);
                    new Thread(new OnReHelloClass(msg)).start();
//                }
            }else if (msg.getEndGroup().equals(controller.getLocalGroupName()) && msg.getState() == State.two_way
                    && msg.getType().equals(FINAL_HELLO)) {
                System.out.println("收到 FinalHello 消息，第三次握手=。= " + msg);
                update(msg);
                onFinalMsgReceive(msg);
            }else if (msg.getEndGroup().equals(controller.getLocalGroupName()) && msg.getState() == State.two_way
                    && msg.getType().equals(SUB_PUB_NOTIFY)) {
                onNotifyReceive(msg);
            }else if (msg.getEndGroup().equals(controller.getLocalGroupName()) && msg.getState() == State.two_way
                    && msg.getType().equals(HEART)) {
                System.out.println("收到心跳消息");
                onHeartReceive(msg);
            }
        }
    }

    /**
     * 处理 Hello 消息的方法
     * 发送 ReHello，需要删除对应的流表
     */
    private class OnHelloClass implements Runnable {
        HelloMsg re_hello;

        OnHelloClass(HelloMsg msg) {
            re_hello = new HelloMsg();
            re_hello.setLocalGroupName(controller.getLocalGroupName());
            re_hello.setStartGroup(controller.getLocalGroupName());
            re_hello.setEndGroup(msg.getStartGroup());
            re_hello.setEndBorderSwtId(msg.getStartBorderSwtId());
            re_hello.setEndOutPort(msg.getStartOutPort());
            re_hello.setLsa(localLsa);
            re_hello.setType(RE_HELLO);
            re_hello.setRole(controller.getRole());
            re_hello.setState(State.init);
        }

        @Override
        public void run() {
            for (Switch swt : controller.getOutSwitches().values()) {
                for (String out : swt.getOutPorts().values()) {
                    //只需要控制对外端口
                    Flow flow = RouteUtil.downSysRtFlows(controller, controller.getLocalSwtId(),
                            String.valueOf(controller.getSwitchPort()), out, controller.getSysV6Addr(), ovsProcess);
                    //把re_hello发送到每一个outPort
                    MultiHandler handler = new MultiHandler(controller.getSysPort(), controller.getSysV6Addr());
                    re_hello.setStartBorderSwtId(swt.getId());
                    re_hello.setStartOutPort(out);
                    re_hello.setSendTime(System.currentTimeMillis());
                    handler.v6Send(re_hello);
                    RouteUtil.delRouteFlow(flow, ovsProcess);
                }
            }
        }
    }

    /**
     * 收到RE_Hello 消息，需要更新LSDB 并发送 Final_Hello
     */
    private class OnReHelloClass implements Runnable {
        HelloMsg final_hello;

        OnReHelloClass(HelloMsg msg) {
            final_hello = new HelloMsg();
            final_hello.setLocalGroupName(controller.getLocalGroupName());
            final_hello.setStartGroup(controller.getLocalGroupName());
            final_hello.setEndGroup(msg.getStartGroup());
            final_hello.setStartBorderSwtId(msg.getEndBorderSwtId());
            final_hello.setEndBorderSwtId(msg.getStartBorderSwtId());
            final_hello.setStartOutPort(msg.getEndOutPort());
            final_hello.setEndOutPort(msg.getStartOutPort());
            final_hello.setLsa(localLsa);
            final_hello.setType(FINAL_HELLO);
            final_hello.setRole(controller.getRole());
            final_hello.setState(State.two_way);
        }

        @Override
        public void run() {
            //Final_Hello 消息下发的路径需要保存
            String out = final_hello.getStartOutPort();
            Flow flow = RouteUtil.downSysRtFlows(controller, controller.getLocalSwtId(),
                    String.valueOf(controller.getSwitchPort()), out, controller.getSysV6Addr(), ovsProcess);
            MultiHandler handler = new MultiHandler(controller.getSysPort(), controller.getSysV6Addr());
            final_hello.setSendTime(System.currentTimeMillis());
            handler.v6Send(final_hello);
        }
    }

    //收到Final_Hello 消息，更新LSDB
    private void onFinalMsgReceive(HelloMsg msg) {
        String id = msg.getEndBorderSwtId();
        String out = msg.getEndOutPort();
        //收到Final_Hello 消息需要下发对应的流表
        Flow flow = RouteUtil.downSysRtFlows(controller, controller.getLocalSwtId(),
                String.valueOf(controller.getSwitchPort()), out, controller.getSysV6Addr(), ovsProcess);
    }

    //收到LSDB 广播消息，该集群新增订阅，需要重新计算该主题的组播路径
    private void onNotifyReceive(HelloMsg msg) {
        //更新本地lsdb
        String group = msg.getStartGroup();
        Lsa lsa = msg.getLsa();
        lsdb.getLSDB().put(group, lsa);
        System.out.println("收到 " + group + " LSDB广播，更新LSDB：" + lsa);

        //添加订阅节点
        String topic = lsa.getTopic();
        String encodeAddress = lsa.getEncodeAddress();
        Set<Node> subNodes = routeMgr.getAllSubNodes().get(topic);
        if (subNodes == null) {
            subNodes = new HashSet<>();
        }
        Node node = BuildTopology.find(group, routeMgr.getAllNodes());
        subNodes.add(node);
        routeMgr.getAllSubNodes().put(topic, subNodes);

        //下发主题路径
        RouteUtil.downTopicRtFlows(routeMgr.getAllNodes(), routeMgr.getAllSubNodes().get(topic),
                controller, encodeAddress, ovsProcess);
    }

    private void onHeartReceive(HelloMsg msg) {
        String group = msg.getStartGroup();
        Lsa lsa = msg.getLsa();
        lsdb.getLSDB().put(group, lsa);
        System.out.println("收到 " + group + " 心跳消息，更新LSDB：" + lsa);
    }

    /**
     * 计算当前时间与发送时间差，判断消息是否有效
     *
     * @param sendTime
     *          发送时间
     * @param type
     *          消息类型
     * @return
     */
    public boolean isValid(Long sendTime, String type) {
        switch (type) {
            case HELLO :
                return System.currentTimeMillis() - sendTime < controller.getHelloAliveTime();
            case RE_HELLO :
                return System.currentTimeMillis() -sendTime < controller.getReHelloAliveTime();
            case FINAL_HELLO :
                return System.currentTimeMillis() -sendTime < controller.getFinalHelloAliveTime();
            default:
                break;
        }
        return false;
    }

    /**
     * 1. 更新本地 lsa、lsdb 信息
     * 2. 生成对应的node 节点
     * 3. 判断如果加入了管理员节点，则自动生成管理路径
     * 4. 保存当前交换机与邻居交换机的端口对应情况，供下发流表使用
     *
     * @param msg
     */
    public void update(HelloMsg msg) {
        //更新localLsa、lsdb
        String neiGroupName = msg.getLocalGroupName();
        int distance = DISTANCE;
        Lsa neiLsa = msg.getLsa();
        neiLsa.getDist2NbrGrps().put(controller.getLocalGroupName(), distance);
        lsdb.getLSDB().put(neiGroupName, neiLsa);
        localLsa.getDist2NbrGrps().put(neiGroupName, distance);

        //添加新的node 节点
        BuildTopology.add(msg.getLsa(), routeMgr.getAllNodes(), routeMgr.getAllEdges());

        //当出现管理节点时开始计算管理路径
        Node node = BuildTopology.find(neiGroupName, routeMgr.getAllNodes());
        if (routeMgr.getRoot() != null) {
            routeMgr.addAdminTree(node);
        }else if (msg.getRole().equals(ADMIN)) {
            routeMgr.setRoot(node);
            routeMgr.buildAdminTree();
        }

        //保存端口对应情况
        List<String> portList = controller.getPort2nei().get(neiGroupName);
        if (portList == null) {
            portList = new LinkedList<>();
        }
        portList.add(msg.getEndOutPort());
        controller.getPort2nei().put(neiGroupName, portList);
    }
}
