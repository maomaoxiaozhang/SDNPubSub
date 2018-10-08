package edu.bupt.wangfu.module.topologyMgr.ospf;

import edu.bupt.wangfu.info.device.Controller;
import edu.bupt.wangfu.info.device.Switch;
import edu.bupt.wangfu.info.message.system.HelloMsg;
import edu.bupt.wangfu.info.message.system.LsaMsg;
import edu.bupt.wangfu.module.topologyMgr.TopoMgr;
import edu.bupt.wangfu.module.util.MultiHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static edu.bupt.wangfu.module.util.Constant.*;
import static edu.bupt.wangfu.module.topologyMgr.util.BuildTopology.*;

/**
 * 监听Hello消息，被动接受来自其他集群的连接建立请求
 */
@Component
public class HelloReceiver implements Runnable {
    @Autowired
    private Controller controller;

    @Autowired
    TopoMgr topoMgr;

    @Override
    public void run() {

    }

    /**
     * 当收到ospf的Hello消息后，需要根据消息状态，区分处理
     *
     * @param msg
     */
    public void onMsgReceive(HelloMsg msg) {
        //第一次握手，收到 Hello 消息，返回 ReHello
        if (msg.getEndGroup().equals(null) && msg.getState() == State.down) {
            if (isValid(msg.getSendTime(), HELLO)) {
                new Thread(new OnHelloClass(msg)).start();
            }
        }else if (msg.getEndGroup().equals(controller.getLocalGroupName()) && msg.getState() == State.init) {
            //第二次握手，收到 ReHello 消息，返回 FinalHello，本地保存邻居集群信息
            if (isValid(msg.getSendTime(), RE_HELLO)) {
                update(msg);
                new Thread(new OnReHelloClass(msg)).start();
            }
        }else if (msg.getEndGroup().equals(controller.getLocalGroupName()) && msg.getState() == State.two_way) {
            //第三次握手，收到 FinalHello 消息，本地保存邻居集群信息
            topoMgr.getLsdb().getLSDB().put(msg.getLocalGroupName(), msg.getLsa());
            add(msg.getLsa(), topoMgr.getNodes());
        }
    }

    /**
     * 处理 Hello 消息的方法
     * 发送 ReHello
     */
    private class OnHelloClass implements Runnable {
        HelloMsg re_hello;

        OnHelloClass(HelloMsg msg) {
            re_hello = new HelloMsg();
            re_hello.setStartGroup(controller.getLocalGroupName());
            re_hello.setEndGroup(msg.getStartGroup());
            re_hello.setLsa(topoMgr.getLsdb().getLSDB().get(controller.getLocalGroupName()));
            re_hello.setState(State.init);
        }

        @Override
        public void run() {
            for (Switch swt : controller.getOutSwitches().values()) {
                for (String out : swt.getOutPorts().values()) {
                    System.out.println("收到来自" + re_hello.getStartGroup() + "的Hello消息");
                    //这条路径保证从groupCtl发出来的re_hello能到达borderSwt
//                    List<String> outRehello = RouteUtil.calRoute(localSwtId, swt.getId());
//                    List<Flow> rs = RouteUtil.downInGrpRtFlows(outRehello, portWsn2Swt, out, "re_hello", "sys", groupCtl);
                    System.out.println("下发从本地交换机到" + swt.getId() + "交换机的" + out + "端口的ReHello消息流表");

                    //把re_hello发送到每一个outPort，中间的时延保证对面有足够的时间反应第一条收到的信息
                    MultiHandler handler = new MultiHandler("re_hello", SYSTEM);
                    re_hello.setSendTime(System.currentTimeMillis());
                    handler.v6Send(re_hello);
                    System.out.println("通过" + swt.getId() + "交换机的" + out + "端口发送ReHello消息");
                }
            }
        }
    }

    private class OnReHelloClass implements Runnable {
        HelloMsg final_hello;

        OnReHelloClass(HelloMsg msg) {
            final_hello = new HelloMsg();
            final_hello.setStartGroup(controller.getLocalGroupName());
            final_hello.setEndGroup(msg.getStartGroup());
            final_hello.setLsa(topoMgr.getLsdb().getLSDB().get(controller.getLocalGroupName()));
            final_hello.setState(State.two_way);
        }

        @Override
        public void run() {
//            //这里存的和最早发出hello信息的那边，顺序正好相反
//            GroupLink gl = new GroupLink();
//            gl.srcGroupName = finalHello.endGroup;
//            gl.dstGroupName = finalHello.startGroup;
//            gl.srcBorderSwtId = finalHello.endBorderSwtId;
//            gl.srcOutPort = finalHello.endOutPort;
//            gl.dstBorderSwtId = finalHello.startBorderSwtId;
//            gl.dstOutPort = finalHello.startOutPort;
//            nbrGrpLinks.put(gl.dstGroupName, gl);
//            System.out.println("从" + finalHello.startGroup + "集群获得了FinallHello消息，此连接中我方边界交换机为" + gl.srcBorderSwtId + "，对外端口为" + gl.srcOutPort);
//
//            //同步LSDB，其他集群的连接情况；把对面已知的每个group的信息都替换为最新版本的
//            Map<String, Group> newAllGroup = finalHello.allGroups;
//            for (String grpName : newAllGroup.keySet()) {
//                if ((allGroups.get(grpName) == null
//                        && System.currentTimeMillis() - newAllGroup.get(grpName).updateTime < nbrGrpExpiration)
//                        || (allGroups.get(grpName) != null
//                        && allGroups.get(grpName).id < newAllGroup.get(grpName).id))
//
//                    allGroups.put(grpName, newAllGroup.get(grpName));
//            }
//
//            System.out.println("邻居建立完成,邻居情况如下:");
//            Group localGrp = allGroups.get(localGroupName);
//            System.out.println(localGroupName + "的原有邻居为：" + localGrp.dist2NbrGrps.keySet() + "，即将新增的邻居为：" + finalHello.startGroup);
//
//            //全网广播自己的集群信息
//            Group g = allGroups.get(localGroupName);
//            g.id += 1;
//            g.updateTime = System.currentTimeMillis();
//            g.dist2NbrGrps.put(finalHello.startGroup, 1);
//            //全网广播自己的集群信息
//            GroupUtil.spreadLocalGrp();

            for (Switch swt : controller.getOutSwitches().values()) {
                for (String out : swt.getOutPorts().values()) {
                    MultiHandler handler = new MultiHandler("re_hello", SYSTEM);
                    final_hello.setSendTime(System.currentTimeMillis());
                    handler.v6Send(final_hello);
                    System.out.println("通过" + swt.getId() + "交换机的" + out + "端口发送FinalHello消息");
                }
            }
        }
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
     * 通过 OSPF 感知邻居集群，更新本地 lsa、lsdb 信息
     *
     * @param msg
     */
    public void update(HelloMsg msg) {
        String neiGroupName = msg.getLocalGroupName();
        LsaMsg neiLsa = msg.getLsa();
        int distance = DISTANCE;
        neiLsa.getDist2NbrGrps().put(controller.getLocalGroupName(), distance);
        topoMgr.getLsdb().getLSDB().put(neiGroupName, neiLsa);
        LsaMsg localLsa = topoMgr.getLsdb().getLSDB().get(controller.getLocalGroupName());
        localLsa.getDist2NbrGrps().put(neiGroupName, distance);
        add(msg.getLsa(), topoMgr.getNodes());
    }
}
