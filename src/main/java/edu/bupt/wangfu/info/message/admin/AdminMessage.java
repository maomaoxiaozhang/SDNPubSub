package edu.bupt.wangfu.info.message.admin;

import lombok.Data;

import java.io.Serializable;

@Data
public abstract class AdminMessage implements Serializable{
    private static final long serialVersionUID = 1L;

    //消息序号
    private Long id;

    //发送时间
    private Long sendTime;

    //目标集群名
    private String destGroupName;
}
