package edu.bupt.wangfu.module.switchMgr;

import edu.bupt.wangfu.info.device.Controller;
import edu.bupt.wangfu.module.switchMgr.odl.OvsProcess;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * 根据路径信息下发流表
 */

public class AddFlow {
    @Autowired
    private Controller controller;
    private OvsProcess ovs;
    public void add(Map<String, String> pathInfo){
        String lastGroup = null;
        String nextGroup = null;
        String swtId;
        String port;
        String address;
        String password;
        for(String group : pathInfo.keySet()){
            if(group.equals(controller.getLocalGroupName())){
                lastGroup = pathInfo.get(group);
                swtId = controller.getOutSwitches().get(lastGroup).getId();
                port  = controller.getSwitches().get(swtId).getOutPorts().get(lastGroup);
                address = controller.getSwitches().get(swtId).getAddress();
                password = controller.getSwitches().get(swtId).getPassword();
                ovs = new OvsProcess(swtId,address,password);
                ovs.addFlow("ovs-ofctl add-flow "+swtId+" priority=50,in_port="+port+",dl_type=0x86DD,ipv6_dst=FF0E:80:401::A000/128,actions=set_queue:7,output:FLOOD");
            }
            if(pathInfo.get(group).equals(controller.getLocalGroupName())){
                nextGroup = pathInfo.get(group);
                swtId = controller.getOutSwitches().get(nextGroup).getId();
                port  = controller.getSwitches().get(swtId).getOutPorts().get(nextGroup);
                address = controller.getSwitches().get(swtId).getAddress();
                password = controller.getSwitches().get(swtId).getPassword();
                ovs = new OvsProcess(swtId,address,password);
                ovs.addFlow("ovs-ofctl add-flow "+swtId+" priority=50,in_port=ANY,dl_type=0x86DD,ipv6_dst=FF0E:80:401::A000/128,actions=set_queue:7,output:"+port);
            }
        }
    }
}