package edu.bupt.wangfu.info.message.admin;

import edu.bupt.wangfu.module.topicTreeMgr.topicTree.EncodeTopicTree;
import lombok.Data;

/**
 * 主题树消息，负责将编码主题树从管理下发，传至各个交换机
 */
@Data
public class EncodeTopicTreeMsg extends AdminMessage{
    //主题树信息
    private EncodeTopicTree encodeTopicTree = new EncodeTopicTree();
}
