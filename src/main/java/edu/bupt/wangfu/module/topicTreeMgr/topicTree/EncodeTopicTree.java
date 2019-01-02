package edu.bupt.wangfu.module.topicTreeMgr.topicTree;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 *      所有编码主题的树形保存格式，包含一棵树的所有节点{@code nodes}、根节点{@code root}
 *      这里采用层序的方式存储
 * </p>
 *
 * @see TopicTree
 */

@Data
@Component
public class EncodeTopicTree implements Serializable{
    private static final long serialVersionUID = 1L;

    //所有节点集合
    private List<EncodeTopicTreeEntry> nodes = new LinkedList<>();

    //根节点
    private EncodeTopicTreeEntry root;

    /**
     * 根据主题返回对应的编码
     * @param topic
     * @return
     */
    public String getEncode(String topic) {
        for (EncodeTopicTreeEntry entry : nodes) {
            if (entry.getTopic().equals(topic)) {
                return entry.getEncode();
            }
        }
        return "";
    }

    /**
     * 根据主题返回对应的地址
     * @param topic
     * @return
     */
    public String getAddress(String topic) {
        for (EncodeTopicTreeEntry entry : nodes) {
            if (entry.getTopic().equals(topic)) {
                return entry.getAddress();
            }
        }
        return "";
    }

    //获取当前主题树的所有节点个数
    public int getSize() {
        return nodes.size();
    }
}
