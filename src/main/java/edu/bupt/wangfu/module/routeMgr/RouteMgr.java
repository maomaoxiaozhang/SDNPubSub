package edu.bupt.wangfu.module.routeMgr;

import edu.bupt.wangfu.info.device.Controller;
import edu.bupt.wangfu.module.routeMgr.algorithm.Dijkstra;
import edu.bupt.wangfu.module.routeMgr.algorithm.Steiner;
import edu.bupt.wangfu.module.routeMgr.util.*;
import edu.bupt.wangfu.module.switchMgr.SwitchMgr;
import edu.bupt.wangfu.module.switchMgr.odl.OvsProcess;
import edu.bupt.wangfu.module.topicTreeMgr.TopicTreeMgr;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

import static edu.bupt.wangfu.module.util.Constant.ADMIN;

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
 * @see SwitchMgr
 */

@Data
@Component
public class RouteMgr {
    //根节点，保存的是管理员所在节点
    private Node root;

    //保存管理路径
    private Map<String, List<String>> adminPath = new HashMap<>();

    //所有节点集合
    private Set<Node> allNodes = new HashSet<>();

    //所有订阅节点集合，key——主题，value——该主题的订阅节点
    private Map<String, Set<Node>> allSubNodes = new HashMap<>();

    //所有边的集合
    private Set<Edge> allEdges = new HashSet<>();

    @Autowired
    Controller controller;

    @Autowired
    OvsProcess ovsProcess;

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

    /**
     * 建立管理路径并下发
     * 每个节点只需要下发与自己关联的流表即可
     */
    public void buildAdminTree() {
        adminPath = Dijkstra.dijkstra(root, allNodes);
        List<String> path = adminPath.get(root.getName());
        int i = 0;
        for (; i < path.size(); i++) {
            if (path.get(i).equals(root.getName())) {
                break;
            }
        }
        if (i > 0) {
            String name = path.get(i-1);
            List<String> portList = controller.getPort2nei().get(name);
            for (String port : portList) {
                RouteUtil.downAdminRtFlows(controller, controller.getLocalSwtId(), port, String.valueOf(controller.getSwitchPort()),
                        controller.getAdminV6Addr(), ovsProcess);
                RouteUtil.downAdminRtFlows(controller, controller.getLocalSwtId(), String.valueOf(controller.getSwitchPort()), port,
                        controller.getAdminV6Addr(), ovsProcess);
            }
        }
        if (i < path.size()-1) {
            String name = path.get(i+1);
            List<String> portList = controller.getPort2nei().get(name);
            for (String port : portList) {
                RouteUtil.downAdminRtFlows(controller, controller.getLocalSwtId(), port, String.valueOf(controller.getSwitchPort()),
                        controller.getAdminV6Addr(), ovsProcess);
                RouteUtil.downAdminRtFlows(controller, controller.getLocalSwtId(), String.valueOf(controller.getSwitchPort()), port,
                        controller.getAdminV6Addr(), ovsProcess);
            }
        }
    }

    /**
     * 在已有管理路径的基础上添加新的节点
     * 选择新增节点与所有节点中距离最小的点
     * @param node
     */
    public void addAdminTree(Node node) {
        List<Neighbor> neighborList = node.getNeighbors();
        int min = Integer.MAX_VALUE;
        String groupName = "";
        for (Neighbor neighbor : neighborList) {
            if (neighbor.getDistance() < min) {
                min = neighbor.getDistance();
                groupName = neighbor.node.getName();
            }
        }
        if (!groupName.equals("")) {
            List<String> path = new LinkedList<>(adminPath.get(groupName));
            path.add(groupName);
            adminPath.put(node.getName(), path);
            List<String> portList = controller.getPort2nei().get(groupName);
            for (String port : portList) {
                RouteUtil.downAdminRtFlows(controller, controller.getLocalSwtId(), port, String.valueOf(controller.getSwitchPort()),
                        controller.getAdminV6Addr(), ovsProcess);
                RouteUtil.downAdminRtFlows(controller, controller.getLocalSwtId(), String.valueOf(controller.getSwitchPort()), port,
                        controller.getAdminV6Addr(), ovsProcess);
            }
        }
    }
}
