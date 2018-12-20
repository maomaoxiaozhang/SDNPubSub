package edu.bupt.wangfu.module.wsnMgr.reactor;

import edu.bupt.wangfu.info.device.User;
import edu.bupt.wangfu.module.wsnMgr.util.soap.SendWSNCommandWSSyn;

/**
 * 具体任务执行handle，这里采用多线程的方式异步执行
 */
public class UserHandle extends Handle{

    private SendWSNCommandWSSyn send;

    public UserHandle(User user) {
        this.user = user;
        String userAddress = user.getAddress();
        send = new SendWSNCommandWSSyn("", userAddress);
    }

    @Override
    public void handle(Task task) {
        String topic = task.getTopic();
        Object msg = task.getMsg();
        new Thread(() -> {
            send.notify(topic, String.valueOf(msg));
        }).start();
        System.out.println("消费者获取任务，发送主题：" + topic + "\t消息：" + msg);
    }
}
