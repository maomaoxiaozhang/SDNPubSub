package edu.bupt.wangfu.info.message.admin;

import lombok.Data;

@Data
public abstract class AdminMessage {
    //消息序号
    private Long id;

    //发送时间
    private Long sendTime;

    //目标集群名
    private String destGroupName;
}
