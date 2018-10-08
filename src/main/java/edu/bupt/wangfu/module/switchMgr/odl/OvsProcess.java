package edu.bupt.wangfu.module.switchMgr.odl;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import edu.bupt.wangfu.info.device.Controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 *  与交换机建立session通道，对交换机进行管理
 */
public class OvsProcess {
    private static OvsProcess ovsProcess;
    private static Session session;

    public OvsProcess(String swchUser, String swchAddr, String swchPwd) {
        try {
            JSch jSchIn = new JSch();
            session = jSchIn.getSession(swchUser, swchAddr, 22);
            session.setPassword(swchPwd);
            Properties configin = new Properties();
            configin.put("StrictHostKeyChecking", "no");
            session.setConfig(configin);
            session.connect();
        }catch (Exception e) { e.printStackTrace(); }
    }

    public static synchronized String remoteExecuteCommand(String cmd) {
        cmd = cmd.trim();
        StringBuilder sb = new StringBuilder();
        ChannelExec channel = null;
        try {
            if (!cmd.startsWith("/ovs/bin/"))
                cmd = "/ovs/bin/" + cmd;
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

    public static void addFlow(String body) {
        String cmd =  body;
        remoteExecuteCommand(cmd);
    }

    public static void addFlow(Controller ctrl, String tpid, String body) {
//        String brName = RestProcess.getBrNameByTpid(ctrl, tpid);
//        String cmd = "ovs-ofctl add-flow " + brName + " " + body;
//
//        remoteExcuteCommand(cmd);

        String cmd =  body;
        remoteExecuteCommand(cmd);
    }

    public static void deleteFlows(String body) {
        String cmd = "ovs-ofctl del-flows br0 " + body;
        remoteExecuteCommand(cmd);
    }

    public static String dumpFlows(String body) {
        String cmd = "ovs-ofctl dump-flows br0 " + body;
        return remoteExecuteCommand(cmd);
    }

    public static String dumpFlows(Controller ctrl, String tpid, String body) {
//        String brName = RestProcess.getBrNameByTpid(ctrl, tpid);
//        String cmd = "ovs-ofctl dump-flows " + brName + " " + body;
        String cmd = "ovs-ofctl dump-flows br0 " + body;
        return remoteExecuteCommand(cmd);
    }

    public static void main(String[] args) {
        String cmd = "/ovs/bin/ovs-appctl qos/show ge-1/1/5 && /ovs/bin/ovs-ofctl dump-flows br0";
        //String cmd2 = "ovs-appctl qos/show ge-1/1/5";
        System.out.println(remoteExecuteCommand(cmd));
    }
}
