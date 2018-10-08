package edu.bupt.wangfu.module.topologyMgr.util;

import edu.bupt.wangfu.info.message.system.LsaMsg;
import edu.bupt.wangfu.info.message.system.LsdbMsg;
import edu.bupt.wangfu.module.routeMgr.util.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 *     构建全局拓扑
 *     这里需要与路由模块结合
 * </p>
 *
 * @see Node
 * @see Neighbor
 */

public class BuildTopology {

    /**
     * 输入为全局lsdb，输出是计算出的全网拓扑情况，格式为 Node 节点
     *
     * @param lsdbMsg
     */
    public static Set<Node> build(LsdbMsg lsdbMsg) {
        Set<Node> nodes = new HashSet<>();
        Map<String, LsaMsg> lsdb = lsdbMsg.getLSDB();
        for (LsaMsg lsa : lsdb.values()) {
            add(lsa, nodes);
        }
        return nodes;
    }

    /**
     * 根据收到的lsa信息，补全拓扑信息
     * @param lsa
     * @return
     */
    public static Set<Node> add(LsaMsg lsa, Set<Node> nodes) {
        String groupName = lsa.getLocalGroupName();
        for (Node node : nodes) {
            if (node.getName().equals(groupName))
                return nodes;
        }
        Node node = new Node();
        node.setName(groupName);
        Map<String, Integer> neighbors = lsa.getDist2NbrGrps();
        for (String group : neighbors.keySet()) {
            int distance = neighbors.get(group);
            Node other = find(group, nodes);
            Neighbor neighbor = new Neighbor();
            neighbor.setNode(other);
            neighbor.setDistance(distance);
            node.getNeighbors().add(neighbor);
        }
        nodes.add(node);
        return nodes;
    }

    public static Node find(String groupName, Set<Node> nodes) {
        for (Node node : nodes) {
            if (node.getName().equals(groupName))
                return node;
        }
        return null;
    }
}
