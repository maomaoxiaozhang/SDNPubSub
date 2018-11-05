package edu.bupt.wangfu.info.message.wsn;

import edu.bupt.wangfu.info.device.User;
import edu.bupt.wangfu.info.message.admin.AdminMessage;
import lombok.Data;

/**
 * 用户需求
 */
@Data
public class UserRequestMsg extends WsnMessage {
    //用户
    User user;

    //主题
    String topic;

    //时延需求
    long delay;

    //丢包率需求
    double lostRate;
}
