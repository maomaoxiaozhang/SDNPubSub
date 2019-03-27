package edu.bupt.wangfu.module.managerMgr;

import edu.bupt.wangfu.module.managerMgr.design.PSManagerUI;
import edu.bupt.wangfu.module.managerMgr.policyMgr.PolicyUtil;
import edu.bupt.wangfu.module.managerMgr.schemaMgr.webService.SchemaWSMgr;
import edu.bupt.wangfu.module.managerMgr.util.AllGroups;
import edu.bupt.wangfu.module.managerMgr.util.PolicyMap;
import edu.bupt.wangfu.module.topicMgr.ldap.webService.TopicWSMgr;
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

    @Autowired
    SchemaWSMgr schemaWSMgr;

    @Autowired
    TopicWSMgr topicWSMgr;

    public void start() {
        try {
            policyMap.setPolicyMap(PolicyUtil.readXMLFile());
            ui.start();
            schemaWSMgr.start();
            topicWSMgr.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
