package edu.bupt.wangfu.module.topicTreeMgr.util;

import edu.bupt.wangfu.module.topicTreeMgr.topicTree.EncodeTopicEntry;
import edu.bupt.wangfu.module.topicTreeMgr.topicTree.EncodeTopicTree;
import edu.bupt.wangfu.module.topicTreeMgr.topicTree.TopicEntry;

import java.util.LinkedList;
import java.util.List;

public class EncodeUtil {
    /**
     * 在当前节点的编码基础上，遍历子节点，依次添加编码为0 -- 01 -- 011 -- 0111 等
     *
     * @param root
     *          当前节点
     * @param resultRoot
     *          编码后的节点
     */
    public static void encode(TopicEntry root, EncodeTopicEntry resultRoot) {
        if (root != null) {
            String temp = resultRoot.getEncode();
            List<TopicEntry> nodes = root.getChildList();
            for (int i = 0; i < nodes.size(); i++) {
                EncodeTopicEntry entry = new EncodeTopicEntry();
                entry.setTopic(root.getTopic());
                entry.setEncode(temp + "0" + convert(i));
                entry.setEncodeParent(resultRoot);
                resultRoot.getEncodeChildList().add(entry);
                encode(root.getChildList().get(i), entry);
            }
        }
    }

    /**
     * 将输入参数转换为对应的编码格式
     * @param i
     * @return
     */
    public static String convert(int i) {
        StringBuffer sb = new StringBuffer();
        while (i > 0) {
            sb.append("1");
            i--;
        }
        return sb.toString();
    }

    /**
     * 层次遍历编码主题树，当主题相同时停止，返回对应的编码主题节点
     *
     * @param topicName
     * @return
     */
    public static EncodeTopicEntry getEncodeEntry(String topicName, EncodeTopicTree encodeTopicTree) {
        List<EncodeTopicEntry> queue = new LinkedList<>();
        EncodeTopicEntry root = encodeTopicTree.getRoot();
        queue.add(root);
        while (!queue.isEmpty()) {
            root = queue.get(0);
            if (root.getTopic().equals(topicName))
                return root;
            queue.remove(0);
            for (EncodeTopicEntry entry : root.getEncodeChildList())
                queue.add(entry);
        }
        return null;
    }
}
