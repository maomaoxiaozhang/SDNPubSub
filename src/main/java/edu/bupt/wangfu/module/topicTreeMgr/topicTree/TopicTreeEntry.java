package edu.bupt.wangfu.module.topicTreeMgr.topicTree;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * 主题树节点，用于保存单个主题节点，包含主题名{@code topic}、父主题{@code parent}、
 * 子主题集合{@code childList}
 * 与之对应的是编码后的主题节点
 *
 * @see EncodeTopicTreeEntry
 */

@Data
public class TopicTreeEntry {
    private String topic;
    private TopicTreeEntry parent;

    @Override
    public String toString() {
        return "TopicTreeEntry{" +
                "topic='" + topic + '\'' +
                '}';
    }

    public List<TopicTreeEntry> childList;

}
