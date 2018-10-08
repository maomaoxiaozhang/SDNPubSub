package edu.bupt.wangfu.role.controller.interact.listener;

import edu.bupt.wangfu.info.device.Controller;
import edu.bupt.wangfu.role.controller.interact.handler.MsgHandler;
import org.springframework.beans.factory.annotation.Autowired;

public class AdminSysListener implements Runnable{
    @Autowired
    private Controller controller;
    private static AdminSysListener listener;
    private MsgHandler sysHandler;

    private AdminSysListener() {
        this.sysHandler = new MsgHandler(controller.getSysV6Addr(), controller.getSysPort());
    }

    public static void listen() {
        if (listener == null) {
            listener = new AdminSysListener();
        }
        new Thread(listener).start();
        System.out.println("系统消息监听启动，端口为：" );
    }

    @Override
    public void run() {
        try {
            while (true) {
                Object sysMsg = sysHandler.receive();
                new Thread(new SysProcess(sysMsg)).start();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class SysProcess implements Runnable {
        private Object object;

        SysProcess(Object object) {
            this.object = object;
        }

        @Override
        public void run() {
            process(object);
        }

        private void process(Object obj) {

           /* if (obj instanceof HashMap) {
                allGroups = (HashMap<String, GroupUnit>) obj;
                System.out.println("当前集群信息为：" + allGroups);
            }else if  (obj instanceof ConcurrentHashMap) {
                System.out.println("订阅表有新变化，本地已更新！");
                subTable = (ConcurrentHashMap<String, HashMap<String, HashMap<String, Double>>>) obj;
                System.out.println("订阅表为：" + subTable);
            }else if (obj instanceof NewPub) {
                System.out.println("有新的发布消息，正在计算路径...");
            }else if (obj instanceof MsgDelayFeedback_) {
                MsgDelayFeedback_ msgDelayFeedback_ = (MsgDelayFeedback_)obj;
                if (msgDelayFeedback_.getGroupName().equals(groupName)) {
                    int priority = msgDelayFeedback_.getPriority();
                    double delay = msgDelayFeedback_.getDelayConstraint();
                    String target = msgDelayFeedback_.getTarget();

                    Config config = Config.configure();
                    config.setDelayConstraint(delay, priority, target);
                }
            }else if (obj instanceof MsgChangeRequest) {
                System.out.println("时延无法满足，请求用户重新订阅");
                MsgChangeRequest msgChangeRequest = (MsgChangeRequest)obj;
                // 反馈用户重新订阅

                if (msgChangeRequest.getGroupName().equals(groupName)) {
                    HashMap<String, Double> d = subTable.get(msgChangeRequest.getTopic()).get(groupName);
                    for (String addr : d.keySet())
                        if (d.get(addr) < msgChangeRequest.getMinDelay())
                            new DoPusher(addr).doPushByV6("Change Delay! No higher than _" + msgChangeRequest.getMinDelay());
                }
            }else {
                System.out.println("No such Massage!");
                System.out.println("订阅表为：" + subTable);
            }*/
        }
    }
}
