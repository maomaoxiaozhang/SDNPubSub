package edu.bupt.wangfu.module.switchMgr.odl;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import edu.bupt.wangfu.info.device.Controller;
import edu.bupt.wangfu.info.device.Queue;
import edu.bupt.wangfu.info.device.Switch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;

import static edu.bupt.wangfu.module.util.Constant.*;

/**
 *  与交换机建立session通道，对交换机进行管理
 *
 *  <p>
 *      添加流表：
 *      ovs-ofctl add-flow br0 priority=50,in_port=all,dl_type=0x86DD,ipv6_dst=FF0E:0000:0000:0000:0001:2345:6790:ABCD/128,actions=output:2
 *      删除流表：
 *      ovs-ofctl del-flows br0 in_port=all,dl_type=0x86DD,ipv6_dst=FF0E:0000:0000:0000:0001:2345:6790:ABCD/128,out_port=1
 *      查看流表：
 *      ovs-ofctl dump-flows br0
 *      查看队列：
 *      ovs-appctl qos/show ge-1/1/5
 *      添加队列：
 *      ovs-vsctl -- set port ge-1/1/2 qos=@newqos -- --id=@newqos create qos type=PRONTO_STRICT queues=0=@q0,1=@q1,2=@q2 -- --id=@q0 create queue other-config:min-rate=60000000 other-config:max-rate=60000000 -- --id=@q1 create queue other-config:min-rate=30000000 other-config:max-rate=30000000  -- --id=@q2 create queue other-config:min-rate=10000000 other-config:max-rate=10000000
 *
 *      一般表达式：
 *          添加：
 *              ovs-ofctl add-flow br0 priority=%s,in_port=%s,dl_type=0x86DD,ipv6_dst=%s,actions=output:%s
 *          删除：
 *              ovs-ofctl del-flows br0 in_port=%s,dl_type=0x86DD,ipv6_dst=%s,out_port=%s
 *          查看流表：
 *              ovs-ofctl dump-flows br0
 *          查看队列：
 *              ovs-appctl qos/show ge-1/1/%d
 *          添加队列：
 *              ovs-vsctl -- set port ge-1/1/%s qos=@newqos -- --id=@newqos create qos type=PRONTO_STRICT queues=0=@q0,1=@q1,2=@q2 -- --id=@q0 create queue other-config:min-rate=60000000 other-config:max-rate=60000000 -- --id=@q1 create queue other-config:min-rate=30000000 other-config:max-rate=30000000  -- --id=@q2 create queue other-config:min-rate=10000000 other-config:max-rate=10000000
 *  </p>
 */
@Component
public class OvsProcess {
    @Autowired
    Controller controller;

    private Session session;
    private ChannelExec channel;



    public void init() {
        try {
            Switch localSwitch = controller.getSwitches().get(controller.getLocalSwtId());
            String userName = localSwitch.getUserName();
            String host = localSwitch.getAddress();
            String password = localSwitch.getPassword();
            JSch jsch = new JSch();
            session = jsch.getSession(userName, host, 22);
            session.setPassword(password);
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
        } catch (JSchException e) {
            e.printStackTrace();
        }
    }

    /**
     * 指定用户名、交换机ip、密码，建立session连接，下发 openflow 指令
     * @param cmd
     * @return
     */
    public synchronized String remoteExecuteCommand(String cmd) {
        StringBuilder sb = new StringBuilder();
        try {
            cmd = cmd.trim();
            if (!cmd.startsWith("/ovs/bin/")) {
                cmd = "/ovs/bin/" + cmd;
            }
            System.out.println("cmd下发流表：" + cmd);
            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(cmd);
            channel.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(channel.getInputStream()));
            String msg;
            while ((msg = in.readLine()) != null) {
                sb.append(msg).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            assert channel != null;
            channel.disconnect();
        }
        return sb.toString();
    }

    //添加流表
    public void addFlow(String body) {
        String cmd = ADD_FLOW + body;
        remoteExecuteCommand(cmd);
    }

    //删除所有流表
    public void deleteFlows(String body) {
        String cmd = DEL_FLOW + body;
        remoteExecuteCommand(cmd);
    }

    //查看下发的所有流表
    public String dumpFlows() {
        String cmd = DUMP_FLOW;
        return remoteExecuteCommand(cmd);
    }

    //查看所有队列信息
    public String dumpQueues(int port) {
        String cmd = String.format(DUMP_QUEUES, port);
        return remoteExecuteCommand(cmd);
    }

    /**
     * 初始化队列，默认分配带宽，比例为6:3:1
     * @param port
     * @return
     */
    public String defaultInitQueues(int port) {
        String cmd = String.format(INIT_QUEUES, port);
        return remoteExecuteCommand(cmd);
    }

    public synchronized void getInAndOutRate(Queue queue) {
        queue.setLastInRate(queue.getInRate());
        queue.setLastOutRate(queue.getOutRate());
        String cmd = "/ovs/bin/ovs-ofctl dump-flows br0 && /ovs/bin/ovs-appctl qos/show " + queue.getPort() ;
        String res = remoteExecuteCommand(cmd);
        queue.setInRate(getInRate(queue, res));
        queue.setOutRate(getOutRate(queue, res));
    }

    private static Long getInRate(Queue queue, String rtn) {
        String res = "0";
        for (String line : rtn.split("\n")) {
            if (line.contains("output:" + queue.getPort()) && line.contains("set_queue:" + queue.getId())) {
                int left = line.indexOf("n_bytes=");
                int right = line.indexOf(", priority");
                res = line.substring(left + 8, right);
                break;
            }
        }
        return Long.parseLong(res.trim());
    }

    private static Long getOutRate(Queue queue, String rtn) {
        String trans;
        String index = "";
        switch (queue.getId()) {
            case 0 : index = "Queue 7:"; break;
            case 1 : index = "Queue 6:"; break;
            case 2 : index = "Queue 5:"; break;
        }
        String sub = rtn.substring(rtn.indexOf(index));
        int left = sub.indexOf("tx_bytes: ");
        int right = sub.indexOf("tx_errors");
        trans = sub.substring(left + 9, right);
        return Long.parseLong(trans.trim());
    }
}
