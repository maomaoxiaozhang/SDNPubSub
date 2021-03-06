package edu.bupt.wangfu.module.wsnMgr.reactor;

import edu.bupt.wangfu.info.device.User;
import edu.bupt.wangfu.module.util.store.LocalSubPub;
import edu.bupt.wangfu.module.wsnMgr.util.LoadBalance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 分派器，负责分发任务给具体执行handle，当判断负载均衡策略允许时分派任务
 * 注册handle --> 回调使用
 *
 * @see LoadBalance
 */
@Component
public class Dispatcher {

    @Autowired
    LocalSubPub localSubPub;

    @Autowired
    Selector selector;

    Map<User, Handle> handleMap = new ConcurrentHashMap<>();

    public void registerHandle(User user, Handle handle) {
        handleMap.put(user, handle);
    }

    public void removeHandle(User user) {
        handleMap.remove(user);
    }

    public void handleEvents() {
        while (true) {
            Task task = selector.select();
            dispatch(task);
        }
    }

    public void dispatch(Task task) {
        String topic = task.getTopic();
        Map<User, List<String>> localSubMap = localSubPub.getLocalSubMap();
        //总队列负载均衡处理机制
        LoadBalance.dispatcherBalance(handleMap);
        for (User user : localSubMap.keySet()) {
            if (localSubMap.get(user).contains(topic)) {
                Handle handle = handleMap.get(user);
                handle.handle(task);
            }
        }
    }
}
