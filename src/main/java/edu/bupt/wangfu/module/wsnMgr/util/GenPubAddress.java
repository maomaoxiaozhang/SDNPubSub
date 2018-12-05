package edu.bupt.wangfu.module.wsnMgr.util;

import edu.bupt.wangfu.info.device.User;

import java.util.Set;

import static edu.bupt.wangfu.module.util.Constant.publishAddr;
import static edu.bupt.wangfu.module.util.Constant.publishPort;

/**
 * 生成发布消息地址
 */
public class GenPubAddress {

    public static String getPubAddress(Set<User> allConsumers, User user) {
        String address = "";
        if (isNew(allConsumers, user)) {
            address = String.format(publishAddr, publishPort++);
        }
        return address;
    }

    public static boolean isNew(Set<User> allConsumers, User user) {
        for (User ur : allConsumers) {
            if (ur.getId().equals(user.getId())) {
                return false;
            }
        }
        return true;
    }

}
