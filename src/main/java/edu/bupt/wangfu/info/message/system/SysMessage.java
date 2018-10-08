package edu.bupt.wangfu.info.message.system;

import lombok.Data;

@Data
public abstract class SysMessage {
    //发送时间
    private Long sendTime;

    //本地集群名
    private String localGroupName;
}
