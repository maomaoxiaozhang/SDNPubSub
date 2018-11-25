package edu.bupt.wangfu.module.wsnMgr.pro_con;

import edu.bupt.wangfu.info.device.User;
import edu.bupt.wangfu.module.util.store.LocalSubPub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 任务总队列，当消息到来时先存放在任务总队列中，再根据负载均衡策略下放至consumer 队列
 */
@Component
public class TaskQueue {

    @Autowired
    LocalSubPub localSubPub;

    @Autowired
    AllConsumers allConsumers;

    BlockingQueue<Task> taskQueue = new LinkedBlockingQueue<>();

    public void put(Task task) {
        try {
            taskQueue.put(task);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void dispatch() {
        while (true) {
            try {
                Task task = taskQueue.take();
                send2consumer(task);
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void send2consumer(Task task) {
        String topic = task.getTopic();
        Map<User, List<String>> localSubMap = localSubPub.getLocalSubMap();
        for (User user : localSubMap.keySet()) {
            if (localSubMap.get(user).contains(topic)) {
                Consumer consumer = allConsumers.getConsumerMap().get(user);
                try {
                    consumer.getQueue().put(task);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
