package edu.bupt.wangfu.module.topicTreeMgr.topicTree;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 编码后的主题树节点，采用ipv6地址编码，包含主题名{@code topic}、编码地址{@code encode}、
 * 父主题编码地址{@code encodeParent}、子主题编码地址集合{@code encodeChildList}
 * 主题来自编码前的主题树节点
 *
 * @see TopicTreeEntry
 */

@Data
public class EncodeTopicTreeEntry implements Serializable{
    private static final long serialVersionUID = 1L;

    private String topic;

    //主题编码
    private String encode;

    //主题对应的ipv6地址
    private String address;

    private EncodeTopicTreeEntry encodeParent;
    private List<EncodeTopicTreeEntry> encodeChildList;

    @Override
    public String toString() {
        return "EncodeTopicTreeEntry{" +
                "topic='" + topic + '\'' +
                ", encode='" + encode + '\'' +
                ", encodeParent=" + (encodeParent == null ? "null" : encodeParent.getTopic()) +
                '}';
    }
}
