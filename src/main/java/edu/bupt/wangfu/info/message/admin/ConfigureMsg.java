package edu.bupt.wangfu.info.message.admin;

import lombok.Data;

/**
 * 管理员下发配置消息
 */
@Data
public class ConfigureMsg extends AdminMessage{
    //失效阈值
    private long loseThreshold;

    //扫描周期
    private long scanPeriod;

    //发送周期
    private long sendPeriod;
}
