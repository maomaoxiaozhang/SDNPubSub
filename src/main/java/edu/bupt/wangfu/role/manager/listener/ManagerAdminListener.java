package edu.bupt.wangfu.role.manager.listener;

import edu.bupt.wangfu.info.device.Controller;
import edu.bupt.wangfu.info.message.admin.AdminMessage;
import edu.bupt.wangfu.info.message.admin.EncodeTopicTreeMsg;
import edu.bupt.wangfu.info.message.admin.GroupMessage;
import edu.bupt.wangfu.module.managerMgr.util.AllGroups;
import edu.bupt.wangfu.module.topicTreeMgr.topicTree.EncodeTopicTree;
import edu.bupt.wangfu.module.util.MultiHandler;
import edu.bupt.wangfu.role.controller.ControllerStart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class ManagerAdminListener implements Runnable{

    @Autowired
    Controller controller;

    @Autowired
    AllGroups allGroups;

    @Override
    public void run() {
        System.out.println("管理员 admin 监听启动~~");
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
     * @param msg
     */
    private void onMsgReceive(Object msg) {
        if (msg instanceof AdminMessage) {
            if (msg instanceof GroupMessage) {
                System.out.println("admin group message");
                GroupMessage message = (GroupMessage)msg;
                String groupName = message.getGroupName();
                allGroups.getAllGroups().put(groupName, message);
            }
        }
    }
}
