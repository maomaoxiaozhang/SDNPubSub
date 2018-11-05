package edu.bupt.wangfu.module.routeMgr.util;

import edu.bupt.wangfu.info.device.Controller;
import edu.bupt.wangfu.info.device.Flow;
import edu.bupt.wangfu.info.device.Switch;
import edu.bupt.wangfu.module.routeMgr.algorithm.Dijkstra;
import edu.bupt.wangfu.module.routeMgr.algorithm.Steiner;
import edu.bupt.wangfu.module.switchMgr.odl.FlowUtil;
import edu.bupt.wangfu.module.switchMgr.odl.OvsProcess;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static edu.bupt.wangfu.module.util.Constant.ADD;
import static edu.bupt.wangfu.module.util.Constant.PRIORITY;
import static edu.bupt.wangfu.module.util.Constant.TABLE_ID;

/**
 * 工具类，提供集群内路径计算的功能，路径到流表的转换由{@code generateFlow}实现，流表下发由{@code downFlow}提供
 *
 * @see FlowUtil
 */

public class RouteUtil {

    public static List<String> calRoute(String startSwtId, String endSwtId, Map<String, Switch> switchMap) {
        List<String> route = Dijkstra.dijkstra(startSwtId, endSwtId, switchMap);
        System.out.println("集群内路径结算结果为");
        printRoute(route);
        return route;
    }

    public static void printRoute(List<String> route) {
        System.out.print("从" + route.get(0) + "到" + route.get(route.size() - 1) + "的路径为：");
        for (int i = 0; i < route.size(); i++) {
            if (i != route.size() - 1)
                System.out.print(route.get(i) + "-->");
            else
                System.out.println(route.get(i));
        }
    }

//    /**
//     * 下发集群内交换机间的流表
//     * @param route
//     * @param in
//     *          入端口
//     * @param out
//     *          出端口
//     * @param topic
//     * @param topicType
//     * @param controller
//     * @param encodeTopicTree
//     * @param groupEdges
//     * @return
//     */
//    public static List<Flow> downInGrpRtFlows(List<String> route, String in, String out, String topic,
//                                              String topicType,  Controller controller,
//                                              EncodeTopicTree encodeTopicTree, Edge[] groupEdges) {
//        List<Flow> routeFlows = new ArrayList<>();
//        if (route.size() == 1) {//测试
//            Flow flow = FlowUtil.generateFlow(route.get(0), in, out, topic, topicType,
//                    TABLE_ID, PRIORITY, controller, encodeTopicTree);
//            routeFlows.add(flow);
//            FlowUtil.downFlow(controller, flow, "update");
//            return routeFlows;
//        }
//        for (int i = 0; i < route.size(); i++) {
//            Switch pre;
//            Switch cur;
//            Switch next;
//
//            String inPort = (i == 0 ? in : null);
//            String outPort = (i == route.size() - 1 ? out : null);
//
//            Map<String, Switch> switchMap = controller.getSwitches();
//            for (Edge e : groupEdges) {
//                if (i != 0) {
//                    pre = switchMap.get(route.get(i - 1));
//                    cur = switchMap.get(route.get(i));
//                    if (e.getStartNode().getName().equals(pre.getId()) && e.getEndNode().getName().equals(cur.getId()))
//                        inPort = e.getStartNode().getName();
//                }
//                if (i != route.size() - 1) {
//                    cur = switchMap.get(route.get(i));
//                    next = switchMap.get(route.get(i + 1));
//                    if (e.getStartNode().equals(cur.getId()) && e.getEndNode().equals(next.getId()))
//                        outPort = e.getStartNode().getName();
//                }
//            }
//            Flow flow = FlowUtil.generateFlow(route.get(i), inPort, outPort, topic, topicType, TABLE_ID,
//                    PRIORITY, controller, encodeTopicTree);
//            routeFlows.add(flow);
//            FlowUtil.downFlow(controller, flow, "update");
//        }
//        return routeFlows;
//    }

//    public static Flow downInGrpRtFlows(String swtId, String in, String out, String topic, String topicType,
//                                        Controller controller, EncodeTopicTree encodeTopicTree,
//                                        OvsProcess ovsProcess, String action) {
//        Flow flow = FlowUtil.generateFlow(swtId, in, out, topic, topicType,
//                TABLE_ID, PRIORITY, controller, encodeTopicTree);
//        FlowUtil.downFlow(flow, action, ovsProcess);
//        return flow;
//    }

    //下发系统消息路径
    public static Flow downSysRtFlows(Controller controller, String swtId, String in, String out, String address, OvsProcess ovsProcess) {
        System.out.println("下发系统路径");
        int flowCount = controller.getFlowCount();
        controller.setFlowCount(++flowCount);
        Flow flow = new Flow();
        flow.setSwtId(swtId);
        flow.setIn(in);
        flow.setOut(out);
        flow.setFlow_id(flowCount);
        flow.setTable_id(TABLE_ID);
        flow.setPriority(PRIORITY);
        flow.setV6(address + "/" + "128");
        FlowUtil.downFlow(flow, ADD, ovsProcess);
        return flow;
    }

    //下发管理路径
    public static Flow downAdminRtFlows(Controller controller, String swtId, String in, String out, String address, OvsProcess ovsProcess) {
        System.out.println("下发管理路径");
        int flowCount = controller.getFlowCount();
        controller.setFlowCount(++flowCount);
        Flow flow = new Flow();
        flow.setSwtId(swtId);
        flow.setIn(in);
        flow.setOut(out);
        flow.setFlow_id(flowCount);
        flow.setPriority(PRIORITY);
        flow.setV6(address + "/" + "128");
        FlowUtil.downFlow(flow, ADD, ovsProcess);
        return flow;
    }

    //下发主题订阅路由，双向流表
    public static List<Flow> downTopicRtFlows(Set<Node> allNodes, Set<Node> select, Controller controller, String address, OvsProcess ovsProcess) {
        System.out.println("下发主题路径");
        Set<Edge> edgeSet = Steiner.steiner(allNodes, select);
        String groupName = controller.getLocalGroupName();
        String in = String.valueOf(controller.getSwitchPort());
        List<Flow> flowList = new LinkedList<>();
        for (Edge edge : edgeSet) {
            if (edge.getStartNode().getName().equals(groupName)) {
                List<String>  portList = controller.getPort2nei().get(edge.getEndNode().getName());
                for (String out : portList) {
                    int flowCount = controller.getFlowCount();
                    controller.setFlowCount(++flowCount);
                    Flow flow = new Flow();
                    flow.setIn(in);
                    flow.setOut(out);
                    flow.setFlow_id(flowCount);
                    flow.setPriority(PRIORITY);
                    flow.setV6(address + "/" + "128");
                    FlowUtil.downFlow(flow, ADD, ovsProcess);
                    flowList.add(flow);

                    flowCount = controller.getFlowCount();
                    controller.setFlowCount(++flowCount);
                    flow = new Flow();
                    flow.setIn(out);
                    flow.setOut(in);
                    flow.setFlow_id(flowCount);
                    flow.setPriority(PRIORITY);
                    flow.setV6(address + "/" + "128");
                    FlowUtil.downFlow(flow, ADD, ovsProcess);
                    flowList.add(flow);
                }
            }
            if (edge.getEndNode().getName().equals(groupName)) {
                List<String>  portList = controller.getPort2nei().get(edge.getStartNode().getName());
                for (String out : portList) {
                    int flowCount = controller.getFlowCount();
                    controller.setFlowCount(++flowCount);
                    Flow flow = new Flow();
                    flow.setIn(in);
                    flow.setOut(out);
                    flow.setFlow_id(flowCount);
                    flow.setPriority(PRIORITY);
                    flow.setV6(address + "/" + "128");
                    FlowUtil.downFlow(flow, ADD, ovsProcess);
                    flowList.add(flow);

                    flowCount = controller.getFlowCount();
                    controller.setFlowCount(++flowCount);
                    flow = new Flow();
                    flow.setIn(out);
                    flow.setOut(in);
                    flow.setFlow_id(flowCount);
                    flow.setPriority(PRIORITY);
                    flow.setV6(address + "/" + "128");
                    FlowUtil.downFlow(flow, ADD, ovsProcess);
                    flowList.add(flow);
                }
            }
        }
        return flowList;
    }

    public static void delRouteFlows(List<Flow> flowList, OvsProcess ovsProcess) {
        for (Flow flow : flowList) {
            FlowUtil.deleteFlow(flow, ovsProcess);
        }
    }

    public static void delRouteFlow(Flow flow, OvsProcess ovsProcess) {
        FlowUtil.deleteFlow(flow, ovsProcess);
    }

    public static String dumpQueues(int port, OvsProcess ovsProcess) {
        return ovsProcess.dumpQueues(port);
    }
}
