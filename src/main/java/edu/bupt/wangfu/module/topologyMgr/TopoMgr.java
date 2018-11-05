package edu.bupt.wangfu.module.topologyMgr;

import edu.bupt.wangfu.info.device.Controller;
import edu.bupt.wangfu.info.device.Flow;
import edu.bupt.wangfu.info.device.Switch;
import edu.bupt.wangfu.info.message.system.HelloMsg;
import edu.bupt.wangfu.module.routeMgr.RouteMgr;
import edu.bupt.wangfu.module.routeMgr.util.BuildTopology;
import edu.bupt.wangfu.module.routeMgr.util.Node;
import edu.bupt.wangfu.module.routeMgr.util.RouteUtil;
import edu.bupt.wangfu.module.switchMgr.odl.OvsProcess;
import edu.bupt.wangfu.module.topicTreeMgr.TopicTreeMgr;
import edu.bupt.wangfu.module.topologyMgr.util.Lsa;
import edu.bupt.wangfu.module.topologyMgr.util.Lsdb;
import edu.bupt.wangfu.module.util.MultiHandler;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import edu.bupt.wangfu.module.topologyMgr.ospf.*;

import java.util.*;

import static edu.bupt.wangfu.module.util.Constant.*;

/**
 * <p>
 *     实现功能：
 *         1. 拓扑发现及维护过程：遍历所有对外端口，邻居集群间定时发送心跳消息，并交换LSDB
 *         2. lsdb 到 topology 的转换，构建全网拓扑
 * </p>
 *
 * <p>
 *     拓扑建立过程，参考OSPF协议，以集群为单位：
 *         1. 集群控制器刚接入拓扑，需要对外发送Hello消息，并监听对外端口
 *         2. 收到Hello消息则将对面集群添加至本地邻居表，封装本地信息并返回ReHello消息
 *         3. 本地集群收到ReHello消息，连接建立

 *     消息通信格式：
 *     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *     *     *       Hello       *      ReHello      *    Final_Hello    *
 *     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *     *     * startGroup -- G1  *                   * startGroup -- G1  *
 *     *  G1 * endGroup -- null  *                   * endGroup -- G2    *
 *     *     * state -- down     *                   * state -- two_way  *
 *     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *     *     *                   * startGroup -- G2  *                   *
 *     *  G2 *                   * endGroup -- G1    *                   *
 *     *     *                   * state -- init     *                   *
 *     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 *     G1：发送 Hello、Final_Hello 消息，收到 ReHello 消息后在本地保存 G2 集群信息
 *     G2：发送 ReHello 消息，收到 Final_Hello 消息后在本地保存 G1 集群信息
 * </p>
 *
 * @see State
 * @see HelloReceiver
 *
 * @see RouteMgr
 */

@Component
@Data
public class TopoMgr{
    @Autowired
    private Controller controller;

    //保存全局LSDB
    @Autowired
    private Lsdb lsdb;

    @Autowired
    private Lsa localLsa;

    @Autowired
    TopicTreeMgr topicTreeMgr;

    @Autowired
    RouteMgr routeMgr;

    @Autowired
    HelloReceiver helloReceiver;

    @Autowired
    OvsProcess ovsProcess;

    public void start() {
        preStart();
        new Thread(helloReceiver, "拓扑发现").start();
        HeartTask heartTask = new HeartTask();
        new Timer().schedule(heartTask, 100, 200000);
    }

    //心跳任务，定时向所有对外端口发送hello消息
    public class HeartTask extends TimerTask{

        @Override
        public void run() {
            for (Switch swt : controller.getOutSwitches().values()) {
                for (String out : swt.getOutPorts().values()) {
                    Flow flow = RouteUtil.downSysRtFlows(controller, controller.getLocalSwtId(),
                            String.valueOf(controller.getSwitchPort()), out, controller.getSysV6Addr(), ovsProcess);
                    sendHello(out, swt.getId());
                    //Hello 消息需删除对应流表
                    RouteUtil.delRouteFlow(flow, ovsProcess);
                }
            }
        }
    }

    /**
     * 发送 Hello 消息
     * @param out
     *          交换机出端口
     * @param swtId
     *          交换机id
     */
    private void sendHello(String out, String swtId) {
        HelloMsg hello = new HelloMsg();
        MultiHandler handler = new MultiHandler(controller.getSysPort(), controller.getSysV6Addr());
        hello.setStartGroup(controller.getLocalGroupName());
        hello.setEndGroup(null);
        hello.setStartBorderSwtId(swtId);
        hello.setEndBorderSwtId(null);
        hello.setStartOutPort(out);
        hello.setEndOutPort(null);
        hello.setLsa(localLsa);
        hello.setState(State.down);
        hello.setType(HELLO);
        hello.setRole(controller.getRole());
        hello.setSendTime(System.currentTimeMillis());
        handler.v6Send(hello);
    }

    /**
     * 1. 预处理，将自己信息保存为node 节点形式
     * 2. 更新localLsa
     */
    private void preStart() {
        //将本地信息生成对应的node 节点
        Node node = BuildTopology.build(controller.getLocalGroupName());
        routeMgr.getAllNodes().add(node);
        if (controller.getRole().equals(ADMIN)) {
            Node root = BuildTopology.find(controller.getLocalGroupName(), routeMgr.getAllNodes());
            routeMgr.setRoot(root);
            routeMgr.getAdminPath().put(controller.getLocalGroupName(), new LinkedList<>());
        }

        //保存至localLsa、lsdb
        localLsa.setGroupName(controller.getLocalGroupName());
        localLsa.setAddress(controller.getSysV6Addr());
        localLsa.setPort(controller.getSysPort());
        lsdb.getLSDB().put(controller.getLocalGroupName(), localLsa);
    }
}
