package edu.bupt.wangfu.module.wsnMgr.util;

import edu.bupt.wangfu.info.device.User;
import edu.bupt.wangfu.module.wsnMgr.reactor.Dispatcher;
import edu.bupt.wangfu.module.wsnMgr.reactor.Handle;
import edu.bupt.wangfu.module.wsnMgr.reactor.Selector;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static edu.bupt.wangfu.module.util.Constant.blockSize;
import static edu.bupt.wangfu.module.util.Constant.drop;
import static edu.bupt.wangfu.module.util.Constant.threshold;

/**
 * 负载均衡，提供工具方法，判断是否能够向UserHandle 分派任务
 * 这里比较任务和处理器数量的关系，若任务过多则线程自动休眠
 *
 * @see Dispatcher
 */
public class LoadBalance {

    /**
     * 总队列的负载均衡策略，判断条件：
     *      1. 若大多数子队列阻塞，总队列停止分发
     *      2. 若少于阈值的子队列阻塞，继续分发任务，由子队列的负载均衡算法调控
     *
     * @param handleMap
     */
    public static void dispatcherBalance(Map<User, Handle> handleMap) {
        int blockNum = 0;
        for (Handle handle : handleMap.values()) {
            if (isBlock(handle)) {
                blockNum++;
            }
        }
        //若阻塞子队列数超过阈值，则主队列暂停分发任务
        if (blockNum > handleMap.size() * Double.parseDouble(threshold)) {
            System.out.println("执行dispatcher负载均衡策略");
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 用户端负载均衡策略，若子队列阻塞，则采取尾丢弃策略
     * @param handle
     */
    public static void userBalance(Handle handle) {
        if (isBlock(handle)) {
            System.out.println("执行消费者负载均衡策略，对阻塞任务尾丢弃");
            for (int i = 0; i < Integer.parseInt(drop) && !handle.taskQueue.isEmpty(); i++) {
                handle.taskQueue.poll();
            }
        }
    }

    /**
     * 判断子队列是否阻塞，比较子队列任务数量与阻塞阈值的大小关系
     * @param handle
     * @return
     */
    public static boolean isBlock(Handle handle) {
        if (handle.taskQueue.size() >= Integer.parseInt(blockSize)) {
            return true;
        }
        return false;
    }
}
