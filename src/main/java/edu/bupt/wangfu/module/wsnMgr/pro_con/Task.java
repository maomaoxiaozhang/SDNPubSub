package edu.bupt.wangfu.module.wsnMgr.pro_con;

import lombok.Data;

/**
 * 生产者消费者模式下的任务，需要由消费者处理（将主题、内容发送给用户）
 */
@Data
public class Task {
    //任务的主题
    private String topic;

    //任务的内容
    private Object msg;
}
