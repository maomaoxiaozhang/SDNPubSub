package module.controller.ospf;

import info.device.Controller;
import info.device.Flow;
import info.device.Switch;
import info.msg.Hello;
import info.msg.LSA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.TimerTask;

/**
 * 拓扑维护过程：遍历所有对外端口，邻居集群间定时发送心跳消息，并交换LSDB
 */

@Component
public class TopoMgr extends TimerTask{
    @Autowired
    private Controller controller;

    @Override
    public void run() {
        System.out.println("开始心跳任务");
//        for (Switch swt : controller.getOutSwitches().values()) {
//            for (String out : swt.getPorts()) {
//                if (!out.equals("LOCAL")) {
//                    LSA lsa = new LSA();
//                    lsa.setUpdateTime(System.currentTimeMillis());
//                    allGroups.put(localGroupName, localGrp);
//
//                    List<String> outHello = RouteUtil.calRoute(localSwtId, swt.id);
//                    List<String> inRehello = RouteUtil.calRoute(swt.id, localSwtId);
//                    List<Flow> ctl2out = RouteUtil.downInGrpRtFlows(outHello, portWsn2Swt, out, "hello", "sys", groupCtl);
//                    List<Flow> out2ctl = RouteUtil.downInGrpRtFlows(inRehello, out, portWsn2Swt, "re_hello", "sys", groupCtl);
//
//                    sendHello(out, swt.id);
//                    System.out.println("向交换机" + swt.id + "通过" + out + "端口发送Hello消息");
//                    //发送后阻塞线程，这期间：对面收到hello，回复re_hello，最后再发送一条最终版的hello
//                    //这之后（无论之前是否回复），都继续发下一条
//                    try {
//                        Thread.sleep(helloPeriod);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    //删除这次握手的流表，准备下次的
//                    RouteUtil.delRouteFlows(ctl2out);
//                    RouteUtil.delRouteFlows(out2ctl);
//                    System.out.println("删除从" + swt.id + "交换机的" + out + "端口发出Hello消息的流表");
//						/*} else {
//							sendHeartBeat();
//						}*/
//                }
            }
//        }
        //定时检测邻居集群的代表是否还在线
//        for (Group g : allGroups.values()) {
//            if (!g.groupName.equals(localGroupName) && System.currentTimeMillis() - g.updateTime > nbrGrpExpiration) {
//                allGroups.remove(g.groupName);
//                nbrGrpLinks.remove(g.groupName);
//                allGroups.get(localGroupName).dist2NbrGrps.remove(g.groupName);
//                System.out.println("集群" + g.groupName + "已失效，从本地删除，本地邻居为：" + allGroups.get(localGroupName).dist2NbrGrps.keySet());
//                GroupUtil.spreadLocalGrp();
//                System.out.println("***亲自发现邻居失效了，开始重新计算路由：" + System.currentTimeMillis());
//                RouteUtil.reCalRoutes();
//            }
//        }
//    }

    private void sendHello(String out, String swtId) {
//        Hello hello = new Hello();
////        MultiHandler handler = new MultiHandler(sysPort, "hello", "sys");
//
//        hello.setStartGroup(controller.getLocalGroupName());
//        hello.startOutPort = out;
//        hello.startBorderSwtId = swtId;
//        hello.reHelloPeriod = reHelloPeriod;
//        hello.allGroups = cloneGrpMap(allGroups);
//
//        handler.v6Send(hello);
    }
//
    private void sendHeartBeat() {
//        //可能会多次发送给相同的集群
//        Group heart = new Group(localGroupName);
//        heart.id += allGroups.get(localGroupName).id;
//        heart.updateTime = System.currentTimeMillis();
//        heart.dist2NbrGrps = cloneIntMap(allGroups.get(localGroupName).dist2NbrGrps);
//
//        MultiHandler handler = new MultiHandler(sysPort, "heart", "sys");
//        handler.v6Send(heart);
    }
}
