package edu.bupt.wangfu.role.manager;

import edu.bupt.wangfu.config.ControllerConfig;
import edu.bupt.wangfu.info.device.Policy;
import edu.bupt.wangfu.module.managerMgr.ManagerMgr;
import edu.bupt.wangfu.module.managerMgr.policyMgr.PolicyUtil;
import edu.bupt.wangfu.module.topicTreeMgr.TopicTreeMgr;
import edu.bupt.wangfu.module.topologyMgr.TopoMgr;
import edu.bupt.wangfu.role.controller.listener.AdminListener;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 控制器入口程序，主要负责监听：
 *     1. adminMessage：管理消息，主题树下发
 *                      由 AdminListener 负责
 *     2. sysMessage：拓扑消息，拓扑构建、维护
 *                    由 TopoMgr 拓扑模块负责
 *
 * @see TopoMgr
 * @see AdminListener
 */
@Component
@Data
public class ManagerStart {
    @Autowired
    TopicTreeMgr topicTreeMgr;

    @Autowired
    private AdminListener adminListener;

    @Autowired
    private TopoMgr topoMgr;

    @Autowired
    private ManagerMgr managerMgr;

    //@Autowired
    //private ManageRouteMgr managerRouteMgr;
    Map<String,Policy> policyMap = new HashMap();
    PolicyUtil util = new PolicyUtil();

    public void start() {
        //测试期间，激活ldap
        topicTreeMgr.buildTopicTree();
        new Thread(adminListener, "adminListener").start();
        topoMgr.start();
        try {
            policyMap = util.readXMLFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ControllerConfig.class);
        ManagerStart managerStart = (ManagerStart) context.getBean("managerStart");
        managerStart.start();
    }



}
