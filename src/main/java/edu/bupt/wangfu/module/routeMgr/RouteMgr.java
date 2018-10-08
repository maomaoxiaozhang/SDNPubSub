package edu.bupt.wangfu.module.routeMgr;

import edu.bupt.wangfu.module.routeMgr.algorithm.Dijkstra;
import edu.bupt.wangfu.module.routeMgr.algorithm.Steiner;
import edu.bupt.wangfu.module.routeMgr.util.Edge;
import edu.bupt.wangfu.module.routeMgr.util.Neighbor;
import edu.bupt.wangfu.module.routeMgr.util.Node;
import edu.bupt.wangfu.module.switchMgr.SwitchMgr;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * <p>
 *     路由管理模块，在全局拓扑中，根据发布、订阅情况计算出一棵组播树
 *     具体流程为：
 *         1. 如果存在发布节点，计算各个发布节点与所有订阅节点的最小距离之和
 *         2. 选出最小值作为根节点
 *         3. 以根节点为中心，计算最小路径，生成组播树
 *
 *     实现路由到flow的转换
 * </p>
 *
 * @see Node
 * @see Neighbor
 * @see Dijkstra
 *
 * @see SwitchMgr
 */

@Data
@Component
public class RouteMgr {
    //根节点
    private Node root;

    //所有节点集合
    private Node[] allNodes;

    //所有边的集合
    private Edge[] allEdges;

    @Autowired
    private SwitchMgr switchMgr;

    /**
     * 根据全局拓扑情况，计算出发布节点与各个订阅节点的距离之和{@code dijkstra}，
     * 以最小值为组播树的根节点
     *
     * @param pubNodes
     *          所有发布节点
     * @param subNodes
     *          所有订阅节点
     * @return
     *          返回发布节点中与所有订阅节点距离和最小的节点
     */
    public Node calRoot(List<Node> pubNodes, List<Node> subNodes) {
        Node result = null;
        int distance = 0;
        Dijkstra dijkstra = new Dijkstra();
        for (Node pubNode : pubNodes) {
            int temp = dijkstra.dijkstra(pubNode, subNodes);
            if (result == null || temp < distance) {
                result = pubNode;
                distance = temp;
            }
        }
        root = result;
        return result;
    }

    /**
     * 根据全局拓扑构建组播树，计算方法为 steiner 最小生成树算法
     *
     * @param allNodes
     *          所有节点集合
     * @param subPubNodes
     *          所有发布、订阅节点集合
     */
    public void buildBroadcastTree(Set<Node> allNodes, Set<Node> subPubNodes) {
        Steiner.steiner(allNodes, subPubNodes);
    }
}
