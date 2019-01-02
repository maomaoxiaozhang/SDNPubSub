package edu.bupt.wangfu.module.topicTreeMgr.topicTree;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * 主题树节点，用于保存单个主题节点，包含主题名{@code topic}、父主题{@code parent}、
 * 子主题集合{@code childList}
 * 主题存储路径{@code path}
 * 主题标记{@code layer}
 * 与之对应的是编码后的主题节点
 *
 * @see EncodeTopicTreeEntry
 */

@Data
public class TopicTreeEntry {
    private String topic;
    private TopicTreeEntry parent;
    public List<TopicTreeEntry> childList = new LinkedList<>();
    public String path;
    private int layer;

    @Override
    public String toString() {
        return topic;
    }
}
