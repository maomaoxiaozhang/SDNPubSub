package edu.bupt.wangfu.module.wsnMgr.reactor;

import edu.bupt.wangfu.info.device.User;

/**
 * 抽象父类，用于处理任务
 */
public abstract class Handle {
    //当前用户
    public User user;

    public abstract void handle(Task task);
}
