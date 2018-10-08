package edu.bupt.wangfu.module.topicTreeMgr;

import edu.bupt.wangfu.module.topicTreeMgr.topicTree.EncodeTopicEntry;
import edu.bupt.wangfu.module.topicTreeMgr.topicTree.EncodeTopicTree;
import edu.bupt.wangfu.module.topicTreeMgr.topicTree.TopicEntry;
import edu.bupt.wangfu.module.topicTreeMgr.topicTree.TopicTree;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 *     主题树管理模块，包含主题树节点的定义和存储、编码地址的存储等；
 *     提供以特定主题为根节点，层次遍历并转换为对应编码地址的操作方法{@code encode}
 * </p>
 *
 * <p>
 *     主题树采用ipv6地址编码的方式，除固定前缀、全局参数、队列信息外，剩余100位用于主题编码；
 *     将多个主题树连接至虚拟根节点 all 上，每层编码时遵循先后顺序，第一个节点编码为0，第二个
 *     为01，第三个为011，以此类推
 *     将主题树编码为二叉树的形式，可快速区分不同主题，适合于 SDN 交换机的匹配
 * </p>
 *
 * @see TopicEntry
 * @see TopicTree
 * @see EncodeTopicEntry
 * @see EncodeTopicTree
 */

@Data
@Component
public class TopicTreeMgr {
    private TopicTree topicTree;
    private EncodeTopicTree encodeTopicTree;



}
