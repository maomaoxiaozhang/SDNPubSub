package module.controller.ospf;

import info.device.Controller;
import info.device.Flow;
import info.device.Switch;
import info.msg.Hello;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 监听Hello消息，被动接受来自其他集群的连接建立请求
 */
@Component
public class HelloReceiver implements Runnable {
    @Autowired
    private Controller controller;

    @Override
    public void run() {

    }

    public void onHello(Hello mh) throws InterruptedException {
        if (!mh.getStartGroup().equals(controller.getLocalGroupName()) && !mh.getStartGroup().equals("")) {
            if (mh.getEndGroup().equals(controller.getLocalGroupName())) {
                //第三次握手，携带这个跨集群连接的全部信息
                new Thread(new OnFinalHello(mh)).start();
            } else {
                //第一次握手，只携带发起方的信息，需要补完接收方的信息，也就是当前节点
                new Thread(new OnHello(mh)).start();
            }
        }
    }

    private class OnHello implements Runnable {
        Hello re_hello;

        OnHello(Hello mh) {
            mh.setEndBorderSwtId(controller.getLocalGroupName());
            this.re_hello = mh;
        }

        @Override
        public void run() {
            for (Switch swt : controller.getOutSwitches().values()) {
                for (String out : swt.getPorts()) {
                    if (!out.equals("LOCAL")) {
                        re_hello.setEndBorderSwtId(swt.getId());
                        re_hello.setEndOutPort(out);
//                        re_hello.setLsa();

                        System.out.println("收到来自" + re_hello.getStartGroup() + "的Hello消息");

//                        //这条路径保证从groupCtl发出来的re_hello能到达borderSwt
//                        List<String> outRehello = RouteUtil.calRoute(localSwtId, swt.id);
//                        List<Flow> rs = RouteUtil.downInGrpRtFlows(outRehello, portWsn2Swt, out, "re_hello", "sys", groupCtl);
//                        System.out.println("下发从本地交换机到" + swt.id + "交换机的" + out + "端口的ReHello消息流表");
//
//                        //把re_hello发送到每一个outPort，中间的时延保证对面有足够的时间反应第一条收到的信息
//                        MultiHandler handler = new MultiHandler(sysPort, "re_hello", "sys");
//                        handler.v6Send(re_hello);
//                        System.out.println("通过" + swt.id + "交换机的" + out + "端口发送ReHello消息");

                        try {
                            Thread.sleep(controller.getReHelloPeriod());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
//                        //删除这条回复流表，准备下次发送
//                        RouteUtil.delRouteFlows(rs);
                    }
                }
            }
        }
    }

    private class OnFinalHello implements Runnable {
        Hello finalHello;

        OnFinalHello(Hello mh) {
            this.finalHello = mh;
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
        }
    }
}
