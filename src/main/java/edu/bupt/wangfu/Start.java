package edu.bupt.wangfu;


import com.jcraft.jsch.*;
import edu.bupt.wangfu.module.switchMgr.odl.OvsProcess;

import java.io.*;
import java.util.Properties;
import java.util.Scanner;

public class Start {
    public static void main(String[] args) {
        Session session = null;
        try {
            String userName = "admin";
            String host = "192.168.100.10";
            String password = "pica8";
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
        Scanner in = new Scanner(System.in);
        while (in.hasNext()) {
            int num = in.nextInt();
            remoteExecuteCommand("ifconfig", session, num);
        }
    }

    public static String remoteExecuteCommand(String cmd, Session session, int num) {
        ChannelShell channel = null;
        StringBuilder sb = new StringBuilder();
        try {
            System.out.println("cmd下发流表：" + cmd);
            channel = (ChannelShell) session.openChannel("shell");
            channel.connect();
            InputStream inputStream = channel.getInputStream();
            OutputStream outputStream = channel.getOutputStream();
            PrintWriter writer = new PrintWriter(outputStream);
            long startTime = System.nanoTime();
            for (int i = 0; i < num; i++) {
                writer.println(cmd);
                writer.flush();
                BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
                String msg;
                while ((msg = in.readLine()) != null) {
                    sb.append(msg).append("\n");
                }
                System.out.println(sb);
                in.close();
            }
            long endTime = System.nanoTime();
            System.out.println("num: " + num + "\tdelay: " + (endTime - startTime));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            assert channel != null;
            channel.disconnect();
        }
        return sb.toString();
    }
}

