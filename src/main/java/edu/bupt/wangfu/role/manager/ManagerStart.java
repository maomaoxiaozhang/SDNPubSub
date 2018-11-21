package edu.bupt.wangfu.role.manager;

import edu.bupt.wangfu.config.ControllerConfig;
import edu.bupt.wangfu.info.device.Controller;
import edu.bupt.wangfu.info.message.admin.EncodeTopicTreeMsg;
import edu.bupt.wangfu.module.managerMgr.ManagerMgr;
import edu.bupt.wangfu.module.managerMgr.listener.ManagerWsnListener;
import edu.bupt.wangfu.module.queueMgr.QueueMgr;
import edu.bupt.wangfu.module.topicTreeMgr.TopicTreeMgr;
import edu.bupt.wangfu.module.topicTreeMgr.topicTree.EncodeTopicTree;
import edu.bupt.wangfu.module.topologyMgr.TopoMgr;
import edu.bupt.wangfu.module.util.MultiHandler;
import edu.bupt.wangfu.role.controller.listener.AdminListener;
import edu.bupt.wangfu.module.managerMgr.listener.ManagerAdminListener;
import edu.bupt.wangfu.role.manager.util.ManagerInit;
import edu.bupt.wangfu.role.controller.listener.WsnListener;
import edu.bupt.wangfu.role.util.WsnTopicTask;
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
    ManagerWsnListener managerWsnListener;

    @Autowired
    private ManagerMgr managerMgr;

    @Autowired
    ManagerAdminListener managerAdminListener;

    @Autowired
    WsnTopicTask wsnTopicTask;

    @Autowired
    QueueMgr queueMgr;

    public void start() {
        //集群初始化，流表预下发
        managerInit.init();
        //生成编码主题树
        topicTreeMgr.buildTopicTree();
        //时间驱动，定时向控制器发送编码主题树
        new Timer().schedule(new ControllerTopicTask(), 1000, 20000);
        //时间驱动，定时向wsn发送编码主题树
        new Timer().schedule(wsnTopicTask, 1000, 15000);
        topoMgr.start();
        //启动wsn监听，接收集群内的发布订阅情况
        new Thread(managerWsnListener, "ManagerWsnListener").start();
        //启动admin监听，接收各集群上报的信息
        new Thread(managerAdminListener, "ManagerAdminListener").start();
        //启动管理员模块、ui界面
        managerMgr.start();
        //启动队列管理
        queueMgr.start();
    }

    public class ControllerTopicTask extends TimerTask {

        @Override
        public void run() {
            int adminPort = controller.getAdminPort();
            String address = controller.getAdminV6Addr();
            MultiHandler handler = new MultiHandler(adminPort, address);
            System.out.println("向controller发送编码主题树");
            EncodeTopicTreeMsg msg = new EncodeTopicTreeMsg();
            msg.setEncodeTopicTree(encodeTopicTree);
            handler.v6Send(msg);
        }
    }

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ControllerConfig.class);
        ManagerStart managerStart = (ManagerStart) context.getBean("managerStart");
        managerStart.start();
    }

}
