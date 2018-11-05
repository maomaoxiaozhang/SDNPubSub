package edu.bupt.wangfu.role.user.subscribe;

import edu.bupt.wangfu.module.wsnMgr.util.soap.SendWSNCommand;
import edu.bupt.wangfu.module.wsnMgr.util.soap.wsn.UserNotificationProcessImpl;

import javax.xml.ws.Endpoint;

/**
 * 用户向wsn层发送、接收消息，消息的格式为：
 * subscribe-address-topic
 */
public class Trans {
	private static final String receiveAddr = "http://192.168.10.101:9016/wsn-subscribe";
	private static final String wsnAddr = "http://192.168.10.101:9010/wsn-core";
	private static final String receiveTopic = "spark";
	public static SendWSNCommand receive = new SendWSNCommand(receiveAddr, wsnAddr);
	public static final String id = String.valueOf(System.currentTimeMillis());

	public Trans() {
		// 消息处理逻辑
		UserNotificationProcessImpl implementor = new UserNotificationProcessImpl();
		// 开启接收服务
		Endpoint endpint = Endpoint.publish(receiveAddr, implementor);
		receive.subscribe(id, receiveTopic, receiveAddr);
//		receive.config(id, receiveTopic, 1000L, 80.8);
	}

	public void sendMethod(String mes) {
//		System.out.println(i++ + "\t" + mes.length() + "\t" + send.reliableNotify(sendTopic, mes, false, "A"));
	}

	public String getUser() {
		return receiveAddr;
	}

}
