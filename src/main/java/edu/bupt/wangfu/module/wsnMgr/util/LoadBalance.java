package edu.bupt.wangfu.module.wsnMgr.util;

import edu.bupt.wangfu.info.device.User;
import edu.bupt.wangfu.module.wsnMgr.reactor.Dispatcher;
import edu.bupt.wangfu.module.wsnMgr.reactor.Handle;
import edu.bupt.wangfu.module.wsnMgr.reactor.Selector;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 负载均衡，提供工具方法，判断是否能够向UserHandle 分派任务
 * 这里比较任务和处理器数量的关系，若任务过多则线程自动休眠
 *
 * @see Dispatcher
 */
public class LoadBalance {

    public static void balance(Map<User, Handle> handleMap, Selector selector) {
        int handlerNum = handleMap.size();
        int taskNum = selector.getTaskQueue().size();
        if (taskNum >= handlerNum * 10) {
            try {
                TimeUnit.MILLISECONDS.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
