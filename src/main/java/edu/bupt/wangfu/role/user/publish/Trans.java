package edu.bupt.wangfu.role.user.publish;

import edu.bupt.wangfu.module.wsnMgr.util.soap.SendWSNCommandWSSyn;

import static edu.bupt.wangfu.module.util.Constant.*;

public class Trans {
	public static SendWSNCommandWSSyn register;

	public static SendWSNCommandWSSyn send;

	private static int i = 0;

	//设置用户id
	public static final String id = String.valueOf(System.currentTimeMillis());
	
	public Trans() {
		register = new SendWSNCommandWSSyn(sendAddr, wsnAddr);
		register.register(id, sendTopic);
//		send = new SendWSNCommandWSSyn(sendAddr, publishAddr);
//		send.publish(id, sendTopic, "message!!!");
	}

	public void sendMethod(String msg) {
		send.publish(id, sendTopic, msg);
	}
}
