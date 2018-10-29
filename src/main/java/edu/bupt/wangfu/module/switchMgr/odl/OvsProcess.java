package edu.bupt.wangfu.module.switchMgr.odl;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import edu.bupt.wangfu.info.device.Controller;
import edu.bupt.wangfu.info.device.Switch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;

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
 *
 *      一般表达式：
 *          添加：
 *              ovs-ofctl add-flow br0 priority=%s,in_port=%s,dl_type=0x86DD,ipv6_dst=%s,actions=output:%s
 *          删除：
 *              ovs-ofctl del-flows br0 in_port=%s,dl_type=0x86DD,ipv6_dst=%s,out_port=%s
 *          查看：
 *              ovs-ofctl dump-flows br0
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

    public void addFlow(String body) {
        String cmd = "ovs-ofctl add-flow br0 " + body;
        remoteExecuteCommand(cmd);
    }

    public void addFlow(Controller ctrl, String tpid, String body) {
//        String brName = RestProcess.getBrNameByTpid(ctrl, tpid);
//        String cmd = "ovs-ofctl add-flow " + brName + " " + body;
//
//        remoteExcuteCommand(cmd);

        String cmd = "ovs-ofctl add-flow br0 " + body;
        remoteExecuteCommand(cmd);
    }

    public void deleteFlows(String body) {
        String cmd = "ovs-ofctl del-flows br0 " + body;
        remoteExecuteCommand(cmd);
    }

    //查看下发的所有流表
    public String dumpFlows() {
        String cmd = "ovs-ofctl dump-flow br0 ";
        return remoteExecuteCommand(cmd);
    }

    public String dumpFlows(Controller ctrl, String tpid, String body) {
//        String brName = RestProcess.getBrNameByTpid(ctrl, tpid);
//        String cmd = "ovs-ofctl dump-flows " + brName + " " + body;
        String cmd = "ovs-ofctl dump-flow br0 " + body;
        return remoteExecuteCommand(cmd);
    }
}
