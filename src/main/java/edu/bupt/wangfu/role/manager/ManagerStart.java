package edu.bupt.wangfu.role.manager;

import edu.bupt.wangfu.config.ControllerConfig;
import edu.bupt.wangfu.info.device.Controller;
import edu.bupt.wangfu.info.device.Queue;
import edu.bupt.wangfu.info.device.Switch;
import edu.bupt.wangfu.info.device.User;
import edu.bupt.wangfu.info.message.admin.EncodeTopicTreeMsg;
import edu.bupt.wangfu.info.message.admin.GroupMessage;
import edu.bupt.wangfu.module.managerMgr.ManagerMgr;
import edu.bupt.wangfu.module.managerMgr.design.PSManagerUI;
import edu.bupt.wangfu.module.managerMgr.listener.ManagerWsnListener;
import edu.bupt.wangfu.module.managerMgr.util.AllGroups;
import edu.bupt.wangfu.module.queueMgr.QueueMgr;
import edu.bupt.wangfu.module.switchMgr.odl.OvsProcess;
import edu.bupt.wangfu.module.topicTreeMgr.TopicTreeMgr;
import edu.bupt.wangfu.module.topicTreeMgr.topicTree.EncodeTopicTree;
import edu.bupt.wangfu.module.topologyMgr.TopoMgr;
import edu.bupt.wangfu.module.util.Constant;
import edu.bupt.wangfu.module.util.MultiHandler;
import edu.bupt.wangfu.module.util.store.LocalSubPub;
import edu.bupt.wangfu.role.controller.ControllerStart;
import edu.bupt.wangfu.role.controller.listener.AdminListener;
import edu.bupt.wangfu.module.managerMgr.listener.ManagerAdminListener;
import edu.bupt.wangfu.role.manager.util.ManagerInit;
import edu.bupt.wangfu.role.controller.listener.WsnListener;
import edu.bupt.wangfu.role.util.WsnTopicTask;
import edu.bupt.wangfu.test.PropertiesTest;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static edu.bupt.wangfu.module.queueMgr.util.GetInfo.getQueueInfo;

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
    //采用线程池管理线程的生命周期
    private static ExecutorService exec = Executors.newCachedThreadPool();

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

    @Autowired
    OvsProcess ovsProcess;

    //本地发布订阅信息
    @Autowired
    LocalSubPub localSubPub;

    @Autowired
    AllGroups allGroups;

    @Autowired
    PSManagerUI ui;

    public class GroupTask extends TimerTask{

        @Override
        public void run() {
            int adminPort = controller.getAdminPort();
            String address = controller.getAdminV6Addr();
            MultiHandler handler = new MultiHandler(adminPort, address);
            System.out.println("向管理员发送集群信息");
            GroupMessage msg = new GroupMessage();
            msg.setController(controller);
            msg.setGroupName(controller.getLocalGroupName());
            for (Switch swt : controller.getOutSwitches().values()) {
                for (String port : swt.getOutPorts().values()) {
                    String str = ovsProcess.dumpQueues(Integer.parseInt(port));
//                    System.out.println(str);
                    Map<Integer, List<Queue>> queueMap = getQueueInfo(str, Integer.parseInt(port));
                    swt.getQueueMap().putAll(queueMap);
                }
            }
            msg.setSwitchMap(controller.getSwitches());
            Set<String> subTopics = new HashSet<>();
            for (User user : localSubPub.getLocalSubMap().keySet()) {
                List<String> list = localSubPub.getLocalSubMap().get(user);
                for (String str : list) {
                    subTopics.add(str);
                }
            }
            msg.setSubTopics(subTopics);
            Set<String> pubTopics = new HashSet<>();
            for (User user : localSubPub.getLocalPubMap().keySet()) {
                List<String> list = localSubPub.getLocalPubMap().get(user);
                for (String str : list) {
                    pubTopics.add(str);
                }
            }
            msg.setPubTopics(pubTopics);
            allGroups.getAllGroups().put(controller.getLocalGroupName(), msg);
            ui.reloadAllGroup();
//            System.out.println("====================");
//            System.out.println("下发主题路径");
//            System.out.println("主题：test1\t路径：G1--G2");
//        System.out.println("主题：test1\t路径：G1--G2，G1--G3");
//            System.out.println("====================");
        }
    }

    public void start() {
        //更新constant 类中的属性值
//        PropertiesTest.refreshPro();
        //集群初始化，流表预下发
        managerInit.init();
        //生成编码主题树
        topicTreeMgr.buildTopicTree();
        //时间驱动，定时向控制器发送编码主题树
//        new Timer().schedule(new ControllerTopicTask(), 1000, 20000);
        //时间驱动，定时向wsn发送编码主题树
        new Timer().schedule(wsnTopicTask, 1000, 15000);
//        topoMgr.start();
        //启动wsn监听，接收集群内的发布订阅情况
        exec.execute(managerWsnListener);
        //启动admin监听，接收各集群上报的信息
        exec.execute(managerAdminListener);
        //时间驱动，启动管理员模块、ui界面
//        new Timer().schedule(new GroupTask(), 1000, 20000);
//        managerMgr.start();
        //启动队列管理
//        queueMgr.start(exec);
    }

    public class ControllerTopicTask extends TimerTask {

        @Override
        public void run() {
            int adminPort = controller.getAdminPort();
            String address = controller.getAdminV6Addr();
            MultiHandler handler = new MultiHandler(adminPort, address);
            EncodeTopicTreeMsg msg = new EncodeTopicTreeMsg();
            msg.setEncodeTopicTree(encodeTopicTree);
            handler.v6Send(msg);
            System.out.println("向controller发送编码主题树，大小：" + encodeTopicTree.getSize());
        }
    }

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ControllerConfig.class);
        ManagerStart managerStart = (ManagerStart) context.getBean("managerStart");
        managerStart.start();
    }

}
