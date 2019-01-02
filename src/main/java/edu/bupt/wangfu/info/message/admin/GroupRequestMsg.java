package edu.bupt.wangfu.info.message.admin;

import lombok.Data;

/**
 * 由控制器发送给管理员的用户需求
 */
@Data
public class GroupRequestMsg extends AdminMessage{
    //集群名
    String group;

    //主题
    String topic;

    //时延需求
    long delay;

    //丢包率需求
    double lostRate;
}
