package edu.bupt.wangfu.role.controller.util;

import edu.bupt.wangfu.info.device.Controller;
import edu.bupt.wangfu.info.device.Switch;
import edu.bupt.wangfu.module.switchMgr.odl.OvsProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static edu.bupt.wangfu.module.util.Constant.PRIORITY;
import static edu.bupt.wangfu.module.util.Constant.SWITCH_ADDRESS;

/**
 * 控制器初始化
 * 负责流表预下发
 */
@Component
public class Init {
    @Autowired
    OvsProcess ovsProcess;

    @Autowired
    Controller controller;

    /**
     * 流表预下发（所有对外端口进 -> 控制器所在交换机端口）
     */
    public void init() {
        //测试使用，添加虚拟交换机
        Switch sw = new Switch();
        sw.setId("8888888");
//        sw.setId("66666666666");
        sw.setAddress(SWITCH_ADDRESS);
        sw.setUserName("admin");
        sw.setPassword("pica8");
        Map<String, String> ports = new HashMap<String, String>() {
            {
                put("1", "1");
                put("2", "2");
                put("3", "3");
                put("4", "4");
                put("5", "5");
                put("6", "6");
                put("8", "8");
            }
        };
        sw.setPorts(ports.keySet());
        Map<String, String> outPorts = new HashMap<String, String>() {
            {
                put("1", "1");
//                put("2", "2");
//                put("4", "4");
//                put("6", "6");
            }
        };
        sw.setOutPorts(outPorts);
        controller.setSwitches(new HashMap<String, Switch>(){
            {
                put(sw.getId(), sw);
            }
        });
        controller.setOutSwitches(new HashMap<String, Switch>(){
            {
                put(sw.getId(), sw);
            }
        });
        controller.setLocalSwtId(sw.getId());

        ovsProcess.init();
        System.out.println("流表预下发：");
        int swtPort = controller.getSwitchPort();
        for (Switch swt : controller.getOutSwitches().values()) {
            for (String port : swt.getOutPorts().values()) {
                ovsProcess.addFlow(String.format("priority=%s,in_port=%s,dl_type=0x86DD,ipv6_dst=%s/128,actions=output:%d",
                        PRIORITY, port, controller.getSysV6Addr(), swtPort));
            }
        }
    }
}
