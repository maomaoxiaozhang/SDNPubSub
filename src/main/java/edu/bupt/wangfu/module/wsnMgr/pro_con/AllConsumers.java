package edu.bupt.wangfu.module.wsnMgr.pro_con;

import edu.bupt.wangfu.info.device.User;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 保存本地所有的消费者
 */
@Data
@Component
public class AllConsumers {
    //保存所有用户对应的消费者
    Map<User, Consumer> consumerMap = new HashMap<>();
}
