package edu.bupt.wangfu.role.manager;

import edu.bupt.wangfu.config.ControllerConfig;
import edu.bupt.wangfu.info.device.Controller;
import edu.bupt.wangfu.info.message.wsn.TopicEncodeMsg;
import edu.bupt.wangfu.module.managerMgr.ManagerMgr;
import edu.bupt.wangfu.module.topicTreeMgr.TopicTreeMgr;
import edu.bupt.wangfu.module.topicTreeMgr.topicTree.EncodeTopicTree;
import edu.bupt.wangfu.module.topologyMgr.TopoMgr;
import edu.bupt.wangfu.module.util.MultiHandler;
import edu.bupt.wangfu.role.controller.ControllerStart;
import edu.bupt.wangfu.role.controller.listener.AdminListener;
import edu.bupt.wangfu.role.controller.listener.ManagerListener;
import edu.bupt.wangfu.role.manager.util.ManagerInit;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 控制器入口程序，主要负责：
 *     1. adminMessage：管理消息
 *                      发送主题树
 *     2. sysMessage：拓扑消息，拓扑构建、维护
 *                    由 TopoMgr 拓扑模块负责
 *     3. wsnMessage：订阅消息
 *                    接收本集群内的发布订阅情况
 *
 * @see TopoMgr
 * @see AdminListener
 */

@Component
@Data
public class ManagerStart {
    @Autowired
    ManagerInit managerInit;

    @Autowired
    Controller controller;

    @Autowired
    TopicTreeMgr topicTreeMgr;

    @Autowired
    EncodeTopicTree encodeTopicTree;

    @Autowired
    private TopoMgr topoMgr;

    @Autowired
    ManagerListener managerListener;

    @Autowired
    private ManagerMgr managerMgr;

    public void start() {
//        managerInit.init();
//        topicTreeMgr.buildTopicTree();
//        new Timer().schedule(new TopicTask(), 1000, 15000);
//        topoMgr.start();
//        new Thread(managerListener, "managerListener").start();
        managerMgr.start();
    }

    public class TopicTask extends TimerTask {

        @Override
        public void run() {
            int wsnPort = controller.getAdminPort();
            String address = controller.getAdminV6Addr();
            MultiHandler handler = new MultiHandler(wsnPort, address);
            System.out.println("向controller发送编码主题树");
            TopicEncodeMsg msg = new TopicEncodeMsg();
            msg.setTopicTree(encodeTopicTree);
            handler.v6Send(msg);
        }
    }

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ControllerConfig.class);
        ManagerStart managerStart = (ManagerStart) context.getBean("managerStart");
        managerStart.start();
    }

}
