package edu.bupt.wangfu.info.message.wsn;

import lombok.Data;

@Data
public class SetConfMsg extends WsnMessage{
    //时延
    private Long delay;

    //丢包率
    private Long rate;
}
