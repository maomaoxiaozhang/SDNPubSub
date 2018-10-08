package edu.bupt.wangfu.module.switchMgr.odl;

import edu.bupt.wangfu.info.device.Controller;
import edu.bupt.wangfu.info.device.Flow;
import edu.bupt.wangfu.info.device.Switch;
import edu.bupt.wangfu.module.topicTreeMgr.topicTree.EncodeTopicTree;
import edu.bupt.wangfu.module.topicTreeMgr.util.EncodeUtil;

import static edu.bupt.wangfu.module.util.Constant.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 流表管理工具类，提供流表的封装和下发功能，具体下发指令由{@code OvsProcess}负责
 *
 * @see OvsProcess
 */
public class FlowUtil {

    //计数flowcount，确保每条流表的编号必须不一样
    public static Flow generateFlow(String swtId, String in, String out, String topic, String topicType,
                                    String t_id, String pri, Controller controller, EncodeTopicTree encodeTopicTree) {
        Set<Flow> topicFlowSet;
        //将route路径中的每一段flow都添加到set中，保证后面不用重复下发，控制flowcount
        Map<String, Set<Flow>> notifyFlows = controller.getNotifyFlows();
        if (notifyFlows.get(topic) != null) {
            topicFlowSet = notifyFlows.get(topic);
            for (Flow flow : topicFlowSet) {
                if (flow.getSwtId().equals(swtId)
                        && (flow.getIn() == null || flow.getIn().equals(in))
                        && flow.getOut().equals(out)
                        && flow.getTopic().equals(topic)) {
                    return flow;
                }
            }
        } else {
            topicFlowSet = new HashSet<>();
        }
        //之前没生成过这条流表，需要重新生成
        // 非outport flood流表
        Map<String, Switch> switchMap = controller.getSwitches();
        if (out.equals("flood-in-grp")) {
            if (switchMap.get(swtId).getNeighbors().keySet().isEmpty()) {
                if (in.equals(LOCAL))
                    return null;
                else
                    out = LOCAL;
            } else {
                out = "";
                for (String s : switchMap.get(swtId).getNeighbors().keySet())
                    out += ("," + s);
                out = out.substring(1);
            }
        }

        String v6Addr = null;
        switch (topicType) {
            case SYSTEM:
                v6Addr = controller.getSysV6Addr();
                break;
            case ADMIN:
                v6Addr = controller.getAdminV6Addr();
                break;
            case NOTIFY:
                v6Addr = EncodeUtil.getEncodeEntry(topic, encodeTopicTree).getEncode();
                break;
            default:
                break;
        }

        int flowCount = controller.getFlowCount();
        controller.setFlowCount(++flowCount);

        Flow flow = new Flow();
        flow.setSwtId(swtId);
        flow.setIn(in);
        flow.setOut(out);
        flow.setTopic(topic);
        flow.setTable_id(t_id);
        flow.setFlow_id(flowCount);
        flow.setPriority(pri);
        flow.setV6(v6Addr + "/" + "128");
        //生成后，将其添加到notifyFlows里，以备后面调用查看
        topicFlowSet.add(flow);
        notifyFlows.put(topic, topicFlowSet);

        return flow;
    }



    /**
     * 下发流表
     * @param controller
     * @param flow
     * @param action
     */
    public static void downFlow(Controller controller, Flow flow, String action) {
        if (flow == null || flow.getOut().equals("")) {
            return;
        }
        //这里还要考虑下发到具体哪个流表里，看要执行的动作是 更新流表项 还是 添加新流表项
        // action == "add" "update"
        //RestProcess.doClientPost(controller, flow.swtId, flow.toStringOutput());
        if (action.equals("update")) { // 如果是更新的流表，先查看已下发的出端口，然后将新的端口添加进去
            String dumpResult = OvsProcess.dumpFlows(controller, flow.getSwtId(), flow.toStringDelete());
            if (dumpResult.split("\n").length < 2) {
                OvsProcess.addFlow(controller, flow.getSwtId(), flow.toStringOutput());
            } else {
                String outPort = "";
                for (int i = 1; i < dumpResult.split("\n").length; i++) {
                    String singleFlow = dumpResult.split("\n")[i];
                    singleFlow = singleFlow.substring(singleFlow.indexOf("actions="));
                    singleFlow = singleFlow.substring(singleFlow.indexOf("=") + 1);
                    ArrayList<String> list = new ArrayList<>();
                    for (int j = 0; j < singleFlow.split(",").length; j++) {
                        if (singleFlow.split(",")[j].equals("LOCAL") && !list.contains("LOCAL"))
                            list.add("LOCAL");
                        if (singleFlow.split(",")[j].contains(":")) {
                            String str = singleFlow.split(",")[j].split(":")[1];
                            if (!list.contains(str) && !flow.getOut().equals(str))
                                list.add(str);
                        }
                    }
                    if (!list.contains(flow.getOut()))
                        list.add(flow.getOut());
                    for (String s : list)
                        outPort += ("," + s);
                }
                if (outPort.length() > 1)
                    outPort = outPort.substring(1);
                flow.setOut(outPort);
                OvsProcess.addFlow(controller, flow.getSwtId(), flow.toStringOutput());
            }
//			System.out.println("update flow \"" + flow.toStringOutput() + "\" complete");
        } else if (action.equals("add")) {//把旧流表覆盖掉
            OvsProcess.addFlow(controller, flow.getSwtId(), flow.toStringOutput());
//			System.out.println("add flow \"" + flow.toStringOutput() + "\" complete");
        }
    }
}
