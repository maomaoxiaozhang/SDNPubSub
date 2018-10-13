package edu.bupt.wangfu.module.topicTreeMgr;

import edu.bupt.wangfu.module.topicMgr.ldap.LdapUtil;
import edu.bupt.wangfu.module.topicMgr.ldap.TopicEntry;
import edu.bupt.wangfu.module.topicTreeMgr.topicTree.EncodeTopicTreeEntry;
import edu.bupt.wangfu.module.topicTreeMgr.topicTree.EncodeTopicTree;
import edu.bupt.wangfu.module.topicTreeMgr.topicTree.TopicTreeEntry;
import edu.bupt.wangfu.module.topicTreeMgr.topicTree.TopicTree;
import edu.bupt.wangfu.module.topicTreeMgr.util.EncodeUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.naming.NamingException;
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
 * @see TopicTreeEntry
 * @see TopicTree
 * @see EncodeTopicTreeEntry
 * @see EncodeTopicTree
 */

@Data
@Component
public class TopicTreeMgr {
    @Autowired
    private TopicTree topicTree;

    @Autowired
    private EncodeTopicTree encodeTopicTree;

    /**
     * 主题树、编码主题树的构建，将ldap返回的结果转换成代码识别的主题树结构
     */
    public void buildTopicTree() {
        LdapUtil util = new LdapUtil();
        try {
            util.connectLdap();
            List<TopicTreeEntry> nodes = util.getAllChildrens(
                    new TopicEntry("all", "1",
                            "ou=all_test,dc=wsn,dc=com", null));
            TopicTreeEntry root = (nodes.size() == 0 ? null : nodes.get(0));
            topicTree.setNodes(nodes);
            topicTree.setRoot(root);
            if (root != null) {
                EncodeTopicTreeEntry encodeRoot = new EncodeTopicTreeEntry();
                encodeRoot.setTopic(root.getTopic());
                encodeRoot.setEncode("0");
                List<EncodeTopicTreeEntry> encodeNodes = EncodeUtil.encode(root, encodeRoot);
                encodeTopicTree.setNodes(encodeNodes);
                encodeTopicTree.setRoot(encodeNodes.get(0));
            }
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }
}
