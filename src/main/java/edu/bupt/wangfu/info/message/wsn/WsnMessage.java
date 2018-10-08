package edu.bupt.wangfu.info.message.wsn;

import lombok.Data;

@Data
public abstract class WsnMessage {
    //发送时间
    private Long sendTime;
}
