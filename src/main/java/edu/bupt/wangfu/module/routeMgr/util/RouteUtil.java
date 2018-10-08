package edu.bupt.wangfu.module.routeMgr.util;

import edu.bupt.wangfu.info.device.Controller;
import edu.bupt.wangfu.info.device.Flow;
import edu.bupt.wangfu.info.device.Switch;
import edu.bupt.wangfu.module.routeMgr.algorithm.Dijkstra;
import edu.bupt.wangfu.module.switchMgr.odl.FlowUtil;
import edu.bupt.wangfu.module.topicTreeMgr.topicTree.EncodeTopicTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 工具类，提供集群内路径计算的功能，路径到流表的转换由{@code generateFlow}实现，流表下发由{@code downFlow}提供
 *
 * @see FlowUtil
 */

public class RouteUtil {

    public static List<String> calRoute(String startSwtId, String endSwtId, Map<String, Switch> switchMap) {
        System.out.println("计算集群内路径中，起点为" + startSwtId + "，终点为" + endSwtId);
        List<String> route = Dijkstra.dijkstra(startSwtId, endSwtId, switchMap);
        System.out.println("集群内路径结算结果为");
        printRoute(route);
        return route;
    }

    private static void printRoute(List<String> route) {
        System.out.print("从" + route.get(0) + "到" + route.get(route.size() - 1) + "的路径为：");
        for (int i = 0; i < route.size(); i++) {
            if (i != route.size() - 1)
                System.out.print(route.get(i) + "-->");
            else
                System.out.println(route.get(i));
        }
    }

    public static List<Flow> downInGrpRtFlows(List<String> route, String in, String out, String topic,
                                              String topicType,  Controller controller,
                                              EncodeTopicTree encodeTopicTree, Edge[] groupEdges) {
        List<Flow> routeFlows = new ArrayList<>();
        if (route.size() == 1) {//测试
            Flow flow = FlowUtil.generateFlow(route.get(0), in, out, topic, topicType,
                    "0", "50", controller, encodeTopicTree);
            routeFlows.add(flow);
            FlowUtil.downFlow(controller, flow, "update");
            return routeFlows;
        }
        for (int i = 0; i < route.size(); i++) {
            Switch pre;
            Switch cur;
            Switch next;

            String inPort = (i == 0 ? in : null);
            String outPort = (i == route.size() - 1 ? out : null);

            Map<String, Switch> switchMap = controller.getSwitches();
            for (Edge e : groupEdges) {
                if (i != 0) {
                    pre = switchMap.get(route.get(i - 1));
                    cur = switchMap.get(route.get(i));
                    if (e.getStartNode().getName().equals(pre.getId()) && e.getEndNode().getName().equals(cur.getId()))
                        inPort = e.getStartNode().getName();
                }
                if (i != route.size() - 1) {
                    cur = switchMap.get(route.get(i));
                    next = switchMap.get(route.get(i + 1));
                    if (e.getStartNode().equals(cur.getId()) && e.getEndNode().equals(next.getId()))
                        outPort = e.getStartNode().getName();
                }
            }
            Flow flow = FlowUtil.generateFlow(route.get(i), inPort, outPort, topic, topicType, inPort,
                    "50", controller, encodeTopicTree);
            routeFlows.add(flow);
            FlowUtil.downFlow(controller, flow, "update");
        }
        return routeFlows;
    }
}
