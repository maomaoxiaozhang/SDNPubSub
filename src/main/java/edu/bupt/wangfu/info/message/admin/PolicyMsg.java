package edu.bupt.wangfu.info.message.admin;

import edu.bupt.wangfu.info.device.Policy;
import lombok.Data;

import java.util.Map;


@Data
public class PolicyMsg extends AdminMessage {
    //定时消息还是事件消息
    private boolean type;
    //相关策略信息
    private Map<String,Policy> policyMap;

}
