package edu.bupt.wangfu.role.controller.util;

import edu.bupt.wangfu.info.device.Controller;
import edu.bupt.wangfu.info.device.Flow;
import edu.bupt.wangfu.info.device.Switch;
import edu.bupt.wangfu.module.routeMgr.util.AllFlows;
import edu.bupt.wangfu.module.switchMgr.odl.OvsProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static edu.bupt.wangfu.module.util.Constant.PRIORITY;
import static edu.bupt.wangfu.module.util.Constant.SWITCH_ADDRESS;

/**
 * 控制器初始化
 * 负责流表预下发
 */
@Component
public class ControllerInit {
    @Autowired
    OvsProcess ovsProcess;

    @Autowired
    Controller controller;

    @Autowired
    AllFlows allFlows;

    /**
     * 流表预下发（所有对外端口进 -> 控制器所在交换机端口）
     */
    public void init() {
        //测试使用，添加虚拟交换机
        Switch sw = new Switch();
        sw.setId("66666666666");
        sw.setAddress(SWITCH_ADDRESS);
        sw.setUserName("admin");
        sw.setPassword("pica8");
        Set<String> ports = new HashSet<>();
        ports.add("1");
        ports.add("2");
        ports.add("3");
        ports.add("4");
        ports.add("5");
        ports.add("6");
        ports.add("8");
        sw.setPorts(ports);
        Map<String, String> outPorts = new HashMap<>();
        outPorts.put("1", "1");
        outPorts.put("8", "8");
        sw.setOutPorts(outPorts);
        Map<String, Switch> switchMap = new HashMap<>();
        switchMap.put(sw.getId(), sw);
        controller.setSwitches(switchMap);
        Map<String, Switch> outSwitchMap = new HashMap<>();
        outSwitchMap.put(sw.getId(), sw);
        controller.setOutSwitches(outSwitchMap);
        controller.setLocalSwtId(sw.getId());

        ovsProcess.init();
        System.out.println("流表预下发：");
        int swtPort = controller.getSwitchPort();
        for (Switch swt : controller.getOutSwitches().values()) {
            for (String port : swt.getOutPorts().values()) {
                Flow flow = new Flow(PRIORITY, port, String.valueOf(swtPort), controller.getSysV6Addr());
                ovsProcess.addFlow(flow);
                flow = new Flow(PRIORITY, port, String.valueOf(swtPort), controller.getAdminV6Addr());
                ovsProcess.addFlow(flow);
            }
        }
    }
}
