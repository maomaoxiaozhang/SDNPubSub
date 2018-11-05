package edu.bupt.wangfu.test;

import edu.bupt.wangfu.config.ControllerConfig;
import edu.bupt.wangfu.info.device.Controller;
import edu.bupt.wangfu.info.device.Manager;
import edu.bupt.wangfu.info.device.Switch;
import edu.bupt.wangfu.info.message.admin.GroupMessage;
import edu.bupt.wangfu.info.message.wsn.TopicEncodeMsg;
import edu.bupt.wangfu.module.switchMgr.odl.OvsProcess;
import edu.bupt.wangfu.module.util.MultiHandler;
import edu.bupt.wangfu.role.manager.util.ManagerInit;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Test {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ControllerConfig.class);
        Controller controller = (Controller) context.getBean("controller");
        ManagerInit managerInit = (ManagerInit) context.getBean("managerInit");
        managerInit.init();
        OvsProcess ovsProcess = (OvsProcess) context.getBean("ovsProcess");
        for (Switch swt : controller.getSwitches().values()) {
            for (String port : swt.getOutPorts().values()) {
                String str = ovsProcess.dumpQueues(5);
//                help(str);
            }
        }
    }


}
