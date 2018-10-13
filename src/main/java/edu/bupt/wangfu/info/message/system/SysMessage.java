package edu.bupt.wangfu.info.message.system;

import lombok.Data;

import java.io.Serializable;

@Data
public abstract class SysMessage implements Serializable{
    private static final long serialVersionUID = 1L;

    //发送时间
    private Long sendTime;

    //本地集群名
    private String localGroupName;
}
