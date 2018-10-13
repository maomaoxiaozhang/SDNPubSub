package edu.bupt.wangfu.info.message.wsn;

import edu.bupt.wangfu.module.topicTreeMgr.topicTree.EncodeTopicTree;
import edu.bupt.wangfu.module.wsnMgr.WsnMgr;
import lombok.Data;

/**
 * 主题编码对应的信息，控制器下发给wsn
 *
 * @see SubPubMsg
 * @see WsnMgr
 */

@Data
public class TopicEncodeMsg extends WsnMessage{
    //主题树编码
    EncodeTopicTree topicTree;
}
