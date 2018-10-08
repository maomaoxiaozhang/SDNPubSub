package edu.bupt.wangfu.info.message.wsn;

import lombok.Data;

@Data
public class SubPubMsg extends WsnMessage {
    //消息类型，包括发布、订阅、取消发布、取消订阅
    private String type;

    //主题
    private String topic;

    //主机地址
    private String address;
}
