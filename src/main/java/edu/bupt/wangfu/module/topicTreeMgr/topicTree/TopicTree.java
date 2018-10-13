package edu.bupt.wangfu.module.topicTreeMgr.topicTree;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 *      所有主题的树形保存格式，包含一棵树的所有节点{@code nodes}、根节点{@code root}
 *      这里建议采用层序的方式存储
 * </p>
 *
 * @see EncodeTopicTree
 */

@Data
@Component
public class TopicTree {
    private List<TopicTreeEntry> nodes;
    private TopicTreeEntry root;
}
