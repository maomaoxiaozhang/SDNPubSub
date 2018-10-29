package edu.bupt.wangfu.info.device;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static edu.bupt.wangfu.module.util.Constant.PRIORITY;
import static edu.bupt.wangfu.module.util.Constant.TABLE_ID;

/**
 * 流表项信息
 *
 * @author caoming
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Flow {
    //流表项id
    private int flow_id;
    //流表项所属流表id
    private String table_id;
    //流表项优先级
    private String priority;
    private String topic;
    private String swtId;
    //进出端口号
    private String in;
    private String out;
    //v4源、目的地址；v6目的地址
    private String v4_src;
    private String v4_dst;
    private String v6;

    public String toStringOutput() {
//        if (in != null) { // generateFlow
//            if (v6 != null)
//                return String.format("table=%s,priority=%s,dl_type=%s,in_port=%s,ipv6_dst=%s,action=output:%s", table_id, priority, "0x86DD", in, v6, out);
//            else
//                return String.format("table=%s,priority=%s,dl_type=%s,in_port=%s,action=goto_table:%s", table_id, priority, "0x0800", in, out);
//        }
//        if (v6 != null) { // generateNoInPortFlow
//            return String.format("table=%s,priority=%s,dl_type=%s,ipv6_dst=%s,action=output:%s", table_id, priority, "0x86DD", v6, out);
//        }
//        if (v4_src != null) { // generateRestFlow
//            return String.format("table=%s,priority=%s,dl_type=%s,nw_src=%s,action=output:%s", table_id, priority, "0x0800", v4_src, out);
//        }
//        if (v4_dst != null) {
//            return String.format("table=%s,priority=%s,dl_type=%s,nw_dst=%s,action=output:%s", table_id, priority, "0x0800", v4_dst, out);
//        }
//        return null;

        return String.format("priority=%s,in_port=%s,dl_type=0x86DD,ipv6_dst=%s,actions=output:%s",
                PRIORITY, in, v6, out);
    }

    public String toString() {
        return "topic: " + topic + ", swtId: " + swtId + ", out port: " + out;
    }

    public String toStringEnQueue() {
        return String.format("table=%s,priority=%s,dl_type=%s,in_port=%s,ipv6_dst=%s,action=enqueue:%s", table_id, priority, "0x86DD", in, v6, out);
    }

    public String toStringDelete() {
        //ovs-ofctl del-flows br0 in_port=%s,dl_type=0x86DD,ipv6_dst=%s,out_port=%s
        return String.format("table=%s,dl_type=%s,in_port=%s,ipv6_dst=%s,out_port=%s", TABLE_ID, "0x86DD", in, v6, out);
    }
}
