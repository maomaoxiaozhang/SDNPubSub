package edu.bupt.wangfu.role.user.publish;

import edu.bupt.wangfu.module.wsnMgr.util.soap.SendWSNCommandWSSyn;
import edu.bupt.wangfu.module.wsnMgr.util.soap.wsn.SendNotificationProcessImpl;

import javax.xml.ws.Endpoint;

import static edu.bupt.wangfu.module.util.Constant.*;

public class Trans {
	public static SendWSNCommandWSSyn register;

	public static SendWSNCommandWSSyn send;

	public static String publishAddress = "";

	private static int i = 0;

	//设置用户id
	public static final String id = String.valueOf(System.currentTimeMillis());
	
	public Trans() {
		register = new SendWSNCommandWSSyn(sendAddr, wsnAddr);
		SendNotificationProcessImpl impl = new SendNotificationProcessImpl();
		Endpoint.publish(sendAddr, impl);
		register.register(id, sendTopic, sendAddr);
	}

	public void sendMethod(String msg) {
		if (!publishAddress.equals("")) {
			send = new SendWSNCommandWSSyn(sendAddr, publishAddress);
			send.publish(id, sendTopic, msg);
		}else {
			System.out.println("用户还未获得发布地址，无法发布！");
		}
	}
}
