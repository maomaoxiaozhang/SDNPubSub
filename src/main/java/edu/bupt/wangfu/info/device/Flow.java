package edu.bupt.wangfu.info.device;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static edu.bupt.wangfu.module.util.Constant.PRIORITY;
import static edu.bupt.wangfu.module.util.Constant.TABLE_ID;
import static edu.bupt.wangfu.module.util.Constant.threshold;

/**
 * 流表项信息
 *
 * @author caoming
 */
@Data
public class Flow extends DevInfo{
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

    public Flow() {
    }

    public Flow(String priority, String in, String out, String v6) {
        this.priority = priority;
        this.in = in;
        this.out = out;
        this.v6 = v6;
    }

    public String toStringOutput() {
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
        return String.format("table=%s,dl_type=%s,in_port=%s,ipv6_dst=%s,out_port=%s", TABLE_ID, "0x86DD", in, v6, out);
    }

    public boolean isSame(Flow other) {
        if (priority.equals(other.priority) && in.equals(other.in) &&
                v6.equals(other.v6) && !out.equals(other.out)) {
            return true;
        }
        return false;
    }
}
