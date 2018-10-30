package edu.bupt.wangfu.module.managerMgr;

import edu.bupt.wangfu.module.managerMgr.design.PSManagerUI;
import edu.bupt.wangfu.module.managerMgr.policyMgr.PolicyUtil;
import edu.bupt.wangfu.module.managerMgr.util.AllGroups;
import edu.bupt.wangfu.module.managerMgr.util.PolicyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 管理员：
 *      1. 初始化策略信息
 *      2. 启动集群界面
 */

@Component
public class ManagerMgr {

    @Autowired
    PolicyMap policyMap;

    @Autowired
    AllGroups allGroups;

    @Autowired
    PSManagerUI ui;

    public void start() {
        try {
            policyMap.setPolicyMap(PolicyUtil.readXMLFile());
            ui.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
