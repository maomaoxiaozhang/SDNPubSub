package edu.bupt.wangfu.info.message.wsn;

import lombok.Data;

import java.io.Serializable;

@Data
public abstract class WsnMessage implements Serializable{
    private static final long serialVersionUID = 1L;

    //发送时间
    private Long sendTime;
}
