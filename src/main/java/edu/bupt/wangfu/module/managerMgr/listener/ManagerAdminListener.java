package edu.bupt.wangfu.module.managerMgr.listener;

import edu.bupt.wangfu.info.device.Controller;
import edu.bupt.wangfu.info.message.admin.AdminMessage;
import edu.bupt.wangfu.info.message.admin.GroupMessage;
import edu.bupt.wangfu.info.message.admin.GroupRequestMsg;
import edu.bupt.wangfu.info.message.admin.RequestFeedBackMsg;
import edu.bupt.wangfu.module.managerMgr.design.PSManagerUI;
import edu.bupt.wangfu.module.managerMgr.util.AllGroups;
import edu.bupt.wangfu.module.routeMgr.RouteMgr;
import edu.bupt.wangfu.module.routeMgr.algorithm.Dijkstra;
import edu.bupt.wangfu.module.routeMgr.util.Node;
import edu.bupt.wangfu.module.util.MultiHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class ManagerAdminListener implements Runnable{

    @Autowired
    Controller controller;

    @Autowired
    PSManagerUI ui;

    @Autowired
    AllGroups allGroups;

    @Autowired
    RouteMgr routeMgr;

    @Override
    public void run() {
        System.out.println("ManagerAdminListener start");
        int adminPort = controller.getAdminPort();
        String address = controller.getAdminV6Addr();
        MultiHandler handler = new MultiHandler(adminPort, address);
        while (true) {
            Object msg = handler.v6Receive();
            onMsgReceive(msg);
        }
    }

    /**
     * 收到控制器上报的集群内部信息，存储在Allgroups 中
     * 计算带宽并下发给控制器
     * @param msg
     */
    private void onMsgReceive(Object msg) {
        if (msg instanceof AdminMessage) {
            if (msg instanceof GroupMessage) {
                System.out.println("GroupMessage");
                GroupMessage message = (GroupMessage)msg;
                String groupName = message.getGroupName();
                allGroups.getAllGroups().put(groupName, message);
                ui.reloadAllGroup();
            }else if (msg instanceof GroupRequestMsg) {
                //收到集群请求，计算相应的带宽、时延，向用户下发队列带宽具体分配结果
                GroupRequestMsg groupRequestMsg = (GroupRequestMsg) msg;
                System.out.println("收到控制器发来的用户请求：" + groupRequestMsg);
                //对用户时延、丢包率进行分析，并将分析结果下发给控制器或用户
                handleConfig(groupRequestMsg);
            }
        }
    }

    /**
     * 分析用户请求，计算是否能够满足需要，若：
     *      1. 能够满足：下发带宽分配策略给控制器
     *      2. 不能满足：反馈给用户，需要用户重新提出需求
     */
    public void handleConfig(GroupRequestMsg msg) {
        String group = msg.getGroup();
        String topic = msg.getTopic();
        long delay = msg.getDelay();
        double lostRate = msg.getLostRate();
        //计算带宽
        double bind = 88.8;

        Set<Node> nodes = routeMgr.getAllPubNodes().get(group);
        //当前主题并无发布者，无需下发指令
        if (nodes == null) {
            return;
        }else {
            Node root = routeMgr.getNode(group);
            Map<String, List<String>> path = Dijkstra.dijkstra(root, nodes);
            int count = 0;
            for (String str : path.keySet()) {
                count = Math.max(count, path.get(str).size());
            }
            //根据最长路径计算出平均时延
            long everDelay = count != 0 ? delay / count : delay;
            bind = delay2bind(everDelay);
            //下发给控制器
            RequestFeedBackMsg feedBackMsg = new RequestFeedBackMsg();
            feedBackMsg.setBind(bind);
            int port = controller.getAdminPort();
            String address = controller.getAdminV6Addr();
            MultiHandler handler = new MultiHandler(port, address);
            handler.v6Send(feedBackMsg);
        }
    }

    /**
     * 时延带宽转换函数：时延 * 带宽 = 包大小
     * @param delay
     * @return
     */
    public double delay2bind(long delay) {
        //默认数据包最大为1k
        int pack = 1;
        return pack / delay / 1024 * 1000;
    }
}
