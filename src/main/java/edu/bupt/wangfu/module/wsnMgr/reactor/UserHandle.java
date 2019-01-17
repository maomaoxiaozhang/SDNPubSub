package edu.bupt.wangfu.module.wsnMgr.reactor;

import edu.bupt.wangfu.info.device.User;
import edu.bupt.wangfu.module.wsnMgr.util.LoadBalance;
import edu.bupt.wangfu.module.wsnMgr.util.soap.SendWSNCommandWSSyn;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * 具体任务执行handle
 */
public class UserHandle extends Handle{

    private SendWSNCommandWSSyn send;
    private String address = null;
    public static int count = 0;

    public UserHandle(User user) {
        this.user = user;
        taskQueue = new LinkedBlockingQueue<>();
        address = user.getAddress();
        send = new SendWSNCommandWSSyn("", address);
    }

    @Override
    public void handle(Task task) {
        try {
            taskQueue.put(task);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            //子队列负载均衡策略，防止因单个任务超时导致队列溢出
            LoadBalance.userBalance(this);
            Task task = null;
            try {
                task = taskQueue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String topic = task.getTopic();
            Object msg = task.getMsg();
            send.notify(topic, String.valueOf(msg));
//            System.out.println("消费者获取任务，发送主题：" + topic + "\t消息：" + msg);
        }
    }
}
