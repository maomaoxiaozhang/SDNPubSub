package edu.bupt.wangfu.module.queueMgr.util;

import edu.bupt.wangfu.info.device.Manager;
import edu.bupt.wangfu.info.message.admin.ChangeRequestMessage;
import edu.bupt.wangfu.info.message.admin.RequestFeedBackMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;


public class ThresholdSetter implements Runnable {
    @Autowired
    Manager manager;
    private String group;
    private String topic;
    private String path;
    private int priority;

    ThresholdSetter(String group, String topic, String path, int priority) {
        this.group = group;
        this.topic = topic;
        this.path = path;
        this.priority = priority;
    }

    @Override
    public void run() {
        String[] ps = path.split("-");
        int jumpNum = ps.length;
        String targetGroup = ps[ps.length - 1];

        /**
         * 初始化阈值设定
         */
        if (manager.getSubTable().containsKey(topic)) {
            HashMap<String, HashMap<String, Double>> groupDelay = manager.getSubTable().get(topic);
            Double userDelay = Double.MAX_VALUE;

            if (groupDelay.containsKey(targetGroup)) {
                HashMap<String, Double> addrs = groupDelay.get(targetGroup);
                for (Double d : addrs.values())
                    userDelay = Math.min(userDelay, d);

                System.out.println(topic + "有订阅，订阅者为：" + targetGroup +  "时延要求为：" + userDelay);

                if (userDelay < jumpNum * manager.getNodeMinDelay()) {
                    // 通知订阅者时延要求无法满足，新的时延待商定
                    System.out.println("时延要求无法满足，即将联系订阅者重新订阅！");
                    ChangeRequestMessage msgChangeRequest = new ChangeRequestMessage(topic, targetGroup, jumpNum * manager.getNodeMinDelay());
                    //发送消息
                } else {
                    Double initDelayConstraint = getInitDelayConstraint(userDelay, jumpNum);
                    //　下发
                    for (String g : ps) {
                        if (manager.getConstraintTable().containsKey(g)) {
                            double last = manager.getConstraintTable().get(g).get(priority);
                            if (initDelayConstraint < last)
                                manager.getConstraintTable().get(g).put(priority, initDelayConstraint);
                        } else {
                            HashMap<Integer, Double> constraint = new HashMap<>();
                            constraint.put(priority, initDelayConstraint);
                            manager.getConstraintTable().put(g, constraint);
                        }

                        RequestFeedBackMsg msgDelayFeedback_ = new RequestFeedBackMsg();
                        //发消息
                    }
                }
            }
        }
    }

    private double getInitDelayConstraint(double userDelay, int jumpNum) {
        // 保留20%用于后期时延调整
        return userDelay / jumpNum * 0.8;
    }
}
