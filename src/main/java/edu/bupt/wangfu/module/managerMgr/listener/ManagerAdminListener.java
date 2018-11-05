package edu.bupt.wangfu.module.managerMgr.listener;

import edu.bupt.wangfu.info.device.Controller;
import edu.bupt.wangfu.info.message.admin.AdminMessage;
import edu.bupt.wangfu.info.message.admin.GroupMessage;
import edu.bupt.wangfu.info.message.admin.GroupRequestMsg;
import edu.bupt.wangfu.info.message.admin.RequestFeedBackMsg;
import edu.bupt.wangfu.module.managerMgr.util.AllGroups;
import edu.bupt.wangfu.module.util.MultiHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ManagerAdminListener implements Runnable{

    @Autowired
    Controller controller;

    @Autowired
    AllGroups allGroups;

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
            }else if (msg instanceof GroupRequestMsg) {
                //收到集群请求，计算相应的带宽、时延，向用户下发队列带宽具体分配结果
                GroupRequestMsg groupRequestMsg = (GroupRequestMsg) msg;
                System.out.println("GroupRequestMsg" + groupRequestMsg.getGroup() + "\t" + groupRequestMsg.getTopic() + "\t" +
                    groupRequestMsg.getDelay() + "\t" + groupRequestMsg.getLostRate());
                handleConfig(groupRequestMsg);
            }
        }
    }

    /**
     * 处理用户配置，根据时延、丢包率的要求计算相应的带宽
     * 将带宽分配结果下发给控制器
     * @param msg
     */
    public void handleConfig(GroupRequestMsg msg) {
        String group = msg.getGroup();
        String topic = msg.getTopic();
        long delay = msg.getDelay();
        double lostRate = msg.getLostRate();
        //计算带宽
        double bind = 88.8;

        //下发给控制器
        RequestFeedBackMsg feedBackMsg = new RequestFeedBackMsg();
        feedBackMsg.setBind(bind);
        int port = controller.getAdminPort();
        String address = controller.getAdminV6Addr();
        MultiHandler handler = new MultiHandler(port, address);
        handler.v6Send(feedBackMsg);
    }
}
