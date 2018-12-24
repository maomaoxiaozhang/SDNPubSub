package edu.bupt.wangfu.module.wsnMgr.reactor;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 保存任务队列，原始消息 --> Acceptor --> Selector
 * 经过两次阻塞队列，转换了消息类型
 */
@Component
@Data
public class Selector {
    BlockingQueue<Task> taskQueue = new LinkedBlockingQueue<>();

    public void addTask(Task task) {
        try {
            taskQueue.put(task);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Task select() {
        Task task = null;
        try {
            task = taskQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return task;
    }
}
