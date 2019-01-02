package edu.bupt.wangfu.module.wsnMgr.reactor;

import edu.bupt.wangfu.info.device.User;

import java.util.concurrent.BlockingQueue;

/**
 * 抽象父类，用于处理任务
 */
public abstract class Handle implements Runnable{
    //当前用户
    public User user;

    //用户端的阻塞队列
    public BlockingQueue<Task> taskQueue;

    public abstract void handle(Task task);
}
