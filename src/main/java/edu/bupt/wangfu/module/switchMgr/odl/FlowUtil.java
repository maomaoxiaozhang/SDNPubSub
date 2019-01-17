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
        if (notifyFlows != null && notifyFlows.get(topic) != null) {
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
        //之前没生成过这条流表，需要重新生成，非outport flood流表
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
            case WSN:
                v6Addr = EncodeUtil.getEncodeEntry(topic, encodeTopicTree).getAddress();
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

    public static void downFlow(Flow flow, String action, OvsProcess ovsProcess) {
        if (flow == null || flow.getOut().equals("")) {
            return;
        }
        switch (action) {
            case ADD:
                ovsProcess.addFlow(flow);
                break;
            case DELETE:
                ovsProcess.deleteFlows(flow.toStringDelete());
                break;
            case DUMP:
                ovsProcess.dumpFlows();
                break;
            default:
                break;
        }
    }

    public static void deleteFlow(Flow flow, OvsProcess ovsProcess) {
        ovsProcess.deleteFlows(flow.toStringDelete());
    }
}
