package edu.bupt.wangfu.module.topologyMgr;

import edu.bupt.wangfu.info.device.Controller;
import edu.bupt.wangfu.info.device.Switch;
import edu.bupt.wangfu.info.message.system.HelloMsg;
import edu.bupt.wangfu.info.message.system.LsdbMsg;
import edu.bupt.wangfu.module.routeMgr.RouteMgr;
import edu.bupt.wangfu.module.routeMgr.util.Node;
import edu.bupt.wangfu.module.topicTreeMgr.TopicTreeMgr;
import edu.bupt.wangfu.module.util.MultiHandler;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import edu.bupt.wangfu.module.topologyMgr.ospf.*;

import java.util.*;

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
    private LsdbMsg lsdb = new LsdbMsg();

    //保存全局节点信息
    private Set<Node> nodes;

    @Autowired
    TopicTreeMgr topicTreeMgr;

    @Autowired
    RouteMgr routeMgr;

    @Autowired
    HelloReceiver helloReceiver;

    public void start() {
        System.out.println("开始 OSPF 拓扑发现");
        new Thread(helloReceiver, "拓扑发现").start();

        //测试使用，添加虚拟交换机
        Switch sw = new Switch();
        sw.setId("139329991887403");
        sw.setAddress("FF0E:0000:0000:0000:0001:2345:6791:ABCD");
        Map<String, String> ports = new HashMap<String, String>() {
            {
                put("1", "1");
                put("2", "2");
            }
        };
        sw.setPorts(ports.keySet());
        sw.setOutPorts(ports);
        controller.setOutSwitches(new HashMap<String, Switch>(){
            {
                put("sw", sw);
            }
        });


        for (Switch swt : controller.getOutSwitches().values()) {
            for (String out : swt.getOutPorts().values()) {
//                List<String> path = RouteUtil.calRoute(swt.getId(), swt.getId(), controller.getSwitches());
//                List<String> inRehello = RouteUtil.calRoute(swt.getId(), controller.getLocalSwtId(), controller.getSwitches());
//                List<String> outHello = RouteUtil.calRoute(controller.getLocalSwtId(), swt.getId(), controller.getSwitches());

                //缺少sdn环节，暂不使用
//                List<Flow> ctl2out = RouteUtil.downInGrpRtFlows(outHello, LOCAL, out, "hello", SYSTEM, controller,
//                        topicTreeMgr.getEncodeTopicTree(), routeMgr.getAllEdges());
//                List<Flow> out2ctl = RouteUtil.downInGrpRtFlows(inRehello, out, LOCAL, "re_hello", SYSTEM, controller,
//                        topicTreeMgr.getEncodeTopicTree(), routeMgr.getAllEdges());

                sendHello(out, swt.getId());

                System.out.println("向交换机" + swt.getId() + "通过" + out + "端口发送Hello消息");
                //删除这次握手的流表，准备下次的
//                RouteUtil.delRouteFlows(ctl2out);
//                RouteUtil.delRouteFlows(out2ctl);
                System.out.println("删除从" + swt.getId() + "交换机的" + out + "端口发出Hello消息的流表");
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
//        MultiHandler handler = new MultiHandler("hello", SYSTEM, controller, topicTreeMgr);
        MultiHandler handler = new MultiHandler(controller.getSysPort(), controller.getLocalAddr());
        hello.setStartGroup(controller.getLocalGroupName());
        hello.setEndGroup(null);
        hello.setStartBorderSwtId(swtId);
        hello.setStartOutPort(out);
        hello.setState(State.down);
        hello.setSendTime(System.currentTimeMillis());
        handler.v6Send(hello);
    }
}
