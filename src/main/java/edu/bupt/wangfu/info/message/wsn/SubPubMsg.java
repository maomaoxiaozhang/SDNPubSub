package edu.bupt.wangfu.info.message.wsn;

import edu.bupt.wangfu.info.device.User;
import edu.bupt.wangfu.module.wsnMgr.WsnMgr;
import lombok.Data;

/**
 * 发布订阅消息，用户上传给wsn
 *
 * @see TopicEncodeMsg
 * @see WsnMgr
 */
@Data
public class SubPubMsg extends WsnMessage {
    //当前集群
    private String group;

    //消息类型，包括发布、订阅、取消发布、取消订阅
    private String type;

    //主题
    private String topic;

    //主题编码地址
    private String encodeAddress;

    //用户
    private User user;
}
