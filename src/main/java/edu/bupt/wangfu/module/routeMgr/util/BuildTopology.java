package edu.bupt.wangfu.module.routeMgr.util;

import edu.bupt.wangfu.module.topologyMgr.util.Lsa;

import java.util.Map;
import java.util.Set;

/**
 * <p>
 *     构建全局拓扑
 *     需要与路由模块结合
 * </p>
 *
 * @see Node
 * @see Neighbor
 */

public class BuildTopology {

    /**
     * 输入为全局lsdb，输出是计算出的全网拓扑情况，格式为 Node 节点
     *
     * @param name
     */
    public static Node build(String name) {
        Node node = new Node();
        node.setName(name);
        return node;
    }

    /**
     * 根据收到的lsa信息，补全拓扑信息
     * 新建node 节点，并生成对应的edge 信息
     * @param lsa
     * @return
     */
    public static Set<Node> add(Lsa lsa, Set<Node> nodes, Set<Edge> edges) {
        String groupName = lsa.getGroupName();
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
            if (other == null) {
                other = new Node();
                other.setName(group);

            }
            Neighbor neighbor = new Neighbor();
            neighbor.setName(other.getName());
            neighbor.setDistance(distance);
            node.getNeighbors().add(neighbor);

            neighbor = new Neighbor();
            neighbor.setName(node.getName());
            neighbor.setDistance(distance);
            other.getNeighbors().add(neighbor);

            Edge edge = new Edge();
            edge.setStartNode(node);
            edge.setEndNode(other);
            edge.setLength(distance);
            edges.add(edge);

            edge = new Edge();
            edge.setStartNode(other);
            edge.setEndNode(node);
            edge.setLength(distance);
            edges.add(edge);
//            nodes.add(other);
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
