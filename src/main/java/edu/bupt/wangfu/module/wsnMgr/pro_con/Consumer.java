package edu.bupt.wangfu.module.wsnMgr.pro_con;

import edu.bupt.wangfu.info.device.User;
import edu.bupt.wangfu.module.wsnMgr.util.soap.SendWSNCommandWSSyn;
import lombok.Data;

import java.util.concurrent.BlockingQueue;

/**
 * 消费者模型，内部使用阻塞队列，消息到来时根据用户的订阅情况将消息任务推入
 */
@Data
public class Consumer implements Runnable{
    //当前用户
    private User user;

    //任务队列
    private BlockingQueue<Task> queue;

    @Override
    public void run() {
        String userAddress = user.getAddress();
        SendWSNCommandWSSyn send = new SendWSNCommandWSSyn("", userAddress);
        Task task = null;
        while (true) {
            try {
                task = queue.take();
                String topic = task.getTopic();
                Object msg = task.getMsg();
                send.notify(topic, String.valueOf(msg));
                System.out.println("消费者获取任务，发送主题：" + topic + "\t消息：" + msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
