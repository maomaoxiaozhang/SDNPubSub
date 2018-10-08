package edu.bupt.wangfu.module.topicTreeMgr.topicTree;

import lombok.Data;

import java.util.List;

/**
 * <p>
 *      所有编码主题的树形保存格式，包含一棵树的所有节点{@code nodes}、根节点{@code root}
 *      这里建议采用层序的方式存储
 * </p>
 *
 * @see TopicTree
 */

@Data
public class EncodeTopicTree {
    private List<EncodeTopicEntry> nodes;
    private EncodeTopicEntry root;
}
