package edu.bupt.wangfu.module.topicTreeMgr.topicTree;

import lombok.Data;

import java.util.List;

/**
 * 编码后的主题树节点，采用ipv6地址编码，包含主题名{@code topic}、编码地址{@code encode}、
 * 父主题编码地址{@code encodeParent}、子主题编码地址集合{@code encodeChildList}
 * 主题来自编码前的主题树节点
 *
 * @see TopicEntry
 */

@Data
public class EncodeTopicEntry {
    private String topic;

    //进行ipv6编码后的地址
    private String encode;

    private EncodeTopicEntry encodeParent;
    private List<EncodeTopicEntry> encodeChildList;
}
