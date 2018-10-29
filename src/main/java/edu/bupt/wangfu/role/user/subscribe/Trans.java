package edu.bupt.wangfu.role.user.subscribe;

import edu.bupt.wangfu.module.wsnMgr.util.soap.NotificationProcessImpl;
import edu.bupt.wangfu.module.wsnMgr.util.soap.SendWSNCommand;

import javax.xml.ws.Endpoint;

/**
 * 用户向wsn层发送、接收消息，消息的格式为：
 * subscribe-address-topic
 */
public class Trans {
	private static final String receiveAddr = "http://192.168.10.101:9016/wsn-subscribe";
//	private static final String sendAddr = "http://192.168.10.101:9018/wsn-core-subscriber";
	private static final String wsnAddr = "http://192.168.10.101:9010/wsn-core";
//	private static final String sendTopic = "all:today";
	private static final String receiveTopic = "spark";
//	public static SendWSNCommandWSSyn send = new SendWSNCommandWSSyn(sendAddr, wsnAddr);
	public static SendWSNCommand receive = new SendWSNCommand(receiveAddr, wsnAddr);
	private static int i;

	public Trans() {
		NotificationProcessImpl implementor = new NotificationProcessImpl();// 消息处理逻辑
		Endpoint endpint = Endpoint.publish(receiveAddr, implementor);// 开启接收服务
		try {
			receive.subscribe(receiveTopic);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendMethod(String mes) {
//		System.out.println(i++ + "\t" + mes.length() + "\t" + send.reliableNotify(sendTopic, mes, false, "A"));
	}

	public String getUser() {
		return receiveAddr;
	}

}
