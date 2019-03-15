package edu.bupt.wangfu.infc;

import edu.bupt.wangfu.info.device.Manager;
import edu.bupt.wangfu.module.wsnMgr.WsnMgr;
import edu.bupt.wangfu.role.manager.ManagerStart;
import edu.bupt.wangfu.role.manager.util.ManagerInit;

/**
 * 本系统对外暴露的接口，提供单机的发布订阅服务
 *      1. 启动管理员节点
 *      2.启动WSN管理节点
 */
public class Init {
    public static void main(String[] args) {
        ManagerStart.main(args);
        WsnMgr.main(args);
    }
}
