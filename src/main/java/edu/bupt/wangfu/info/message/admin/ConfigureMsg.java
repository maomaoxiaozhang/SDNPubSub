package edu.bupt.wangfu.info.message.admin;

import lombok.Data;

@Data
public class ConfigureMsg extends AdminMessage{
    //失效阈值
    private Long loseThreshold;

    //扫描周期
    private Long scanPeriod;

    //发送周期
    private Long sendPeriod;
}
