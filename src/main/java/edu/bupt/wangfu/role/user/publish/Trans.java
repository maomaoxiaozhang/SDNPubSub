package edu.bupt.wangfu.role.user.publish;

import edu.bupt.wangfu.module.wsnMgr.util.soap.SendWSNCommandWSSyn;

public class Trans {
	private static final String sendAddr = "http://192.168.10.101:9018/wsn-publish";
	private static final String wsnAddr = "http://192.168.10.101:9010/wsn-core";
	private static final String sendTopic = "spark";
	public static SendWSNCommandWSSyn send;
	private static int i = 0;
	public static final String id = String.valueOf(System.currentTimeMillis());
	
	public Trans() {
		send = new SendWSNCommandWSSyn(sendAddr, wsnAddr);
//		send.register(id, sendTopic);
		send.publish(id, sendTopic, "message!!!");
	}

	public void sendMethod(String msg) {
		send.publish(id, sendTopic, msg);
	}
}
