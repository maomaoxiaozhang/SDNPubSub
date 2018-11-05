package edu.bupt.wangfu.role.controller;

import edu.bupt.wangfu.config.ControllerConfig;
import edu.bupt.wangfu.info.device.Controller;
import edu.bupt.wangfu.info.device.Queue;
import edu.bupt.wangfu.info.device.Switch;
import edu.bupt.wangfu.info.message.admin.GroupMessage;
import edu.bupt.wangfu.module.switchMgr.odl.OvsProcess;
import edu.bupt.wangfu.module.topicTreeMgr.TopicTreeMgr;
import edu.bupt.wangfu.module.topicTreeMgr.topicTree.EncodeTopicTree;
import edu.bupt.wangfu.module.topologyMgr.TopoMgr;
import edu.bupt.wangfu.module.util.MultiHandler;
import edu.bupt.wangfu.module.util.store.GlobalSubPub;
import edu.bupt.wangfu.module.util.store.LocalSubPub;
import edu.bupt.wangfu.role.controller.listener.AdminListener;
import edu.bupt.wangfu.role.controller.listener.WsnListener;
import edu.bupt.wangfu.role.controller.util.ControllerInit;
import edu.bupt.wangfu.role.util.WsnTopicTask;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 控制器入口程序，主要负责监听：
 *     1. adminMessage：管理消息，主题树下发
 *                      由 AdminListener 负责
 *     2. sysMessage：拓扑消息，拓扑构建、维护
 *                    由 TopoMgr 拓扑模块负责
 *     3. wsnMessage：wsn消息，集群发布订阅情况
 *                    由 WsnListener 监听本集群发布订阅情况
 *
 * @see TopoMgr
 * @see AdminListener
 * @see WsnListener
 */
@Component
@Data
public class ControllerStart {
    //测试使用，激活ldap
    @Autowired
    TopicTreeMgr topicTreeMgr;
    @Autowired
    Controller controller;

    @Autowired
    private AdminListener adminListener;

    @Autowired
    private TopoMgr topoMgr;

    @Autowired
    private WsnListener wsnListener;

    @Autowired
    private ControllerInit controllerInit;

    //本地发布订阅信息
    LocalSubPub localSubPub = new LocalSubPub();

    //全局发布订阅信息
    GlobalSubPub globalSubPub = new GlobalSubPub();

    @Autowired
    EncodeTopicTree encodeTopicTree;

    @Autowired
    WsnTopicTask wsnTopicTask;

    @Autowired
    OvsProcess ovsProcess;

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
                    Map<Integer, List<Queue>> queueMap = getQueueInfo(str, Integer.parseInt(port));
                    swt.getQueueMap().putAll(queueMap);
                }
            }
            msg.setSwitchMap(controller.getSwitches());
            handler.v6Send(msg);
        }
    }

    //获取端口对应的队列情况，每个端口包含三个队列
    public Map<Integer, List<Queue>> getQueueInfo(String str, int port) {
        List<Queue> queueList = new LinkedList<>();
        Map<Integer, List<Queue>> queueMap = new HashMap<>();
        String[] strings = str.split("burst");
        for (int i = 1; i < strings.length; i++) {
            Queue queue = new Queue();
            String[] temp = strings[i].split("\n\t");
            long max_rate = Long.parseLong(temp[1].split(": ")[1]);
            long min_rate = Long.parseLong(temp[2].split(": ")[1]);
            long packets = Long.parseLong(temp[4].split(": ")[1]);
            long bytes = Long.parseLong(temp[5].split(": ")[1]);
            long errors = Long.parseLong(temp[6].split("\n")[0].split(": ")[1]);
//            System.out.println(i + "\t" +  max_rate + "\t" + min_rate + "\t" + packets + "\t" + bytes + "\t" + errors);
            queue.setId(i-1);
            queue.setPort(port);
            queue.setPackets(packets);
            queue.setBytes(bytes);
            queue.setErrors(errors);
            queue.setBrandWidth(max_rate);
            queueList.add(queue);
        }
        queueMap.put(port, queueList);
        return queueMap;
    }

    public void start() {
        //初始化集群，流表预下发
        controllerInit.init();
        //启动管理消息监听，接收主题树
        new Thread(adminListener, "adminListener").start();
        //时间驱动，定时向wsn更新主题树
        new Timer().schedule(wsnTopicTask, 1000, 15000);
        //时间驱动，定时向管理员发送集群内信息
        new Timer().schedule(new GroupTask(), 1000, 20000);
        topoMgr.start();
//        启动wsn监听，接收集群内的发布订阅情况
        new Thread(wsnListener, "wsnListener").start();
    }

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ControllerConfig.class);
        ControllerStart controllerStart = (ControllerStart) context.getBean("controllerStart");
        controllerStart.start();
    }
}
