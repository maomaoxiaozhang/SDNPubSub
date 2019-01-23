package edu.bupt.wangfu.role.user.publish;

import edu.bupt.wangfu.module.wsnMgr.util.soap.SendWSNCommandWSSyn;
import edu.bupt.wangfu.module.wsnMgr.util.soap.wsn.SendNotificationProcessImpl;

import javax.xml.ws.Endpoint;

import java.util.concurrent.TimeUnit;

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

	/**
	 * 文本发送测试，单个文本大小为1024B
	 * @param num
	 */
	public void sendTest(int num) {
		if (!publishAddress.equals("")) {
			send = new SendWSNCommandWSSyn(sendAddr, publishAddress);
		}else {
			System.out.println("用户还未获得发布地址，无法发布！");
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 256-13-4; i++) {
			sb.append('a');
		}
		String msg = sb.toString();
		//发三倍，防止丢包
		for (int i = 0; i < num * 1.5; i++) {
			send.publish(id, sendTopic, i + ":" + System.currentTimeMillis() + ":" + msg);
		}
		System.out.println("over");
	}

	public void sendTestWithSleep(int num) {
		if (!publishAddress.equals("")) {
			send = new SendWSNCommandWSSyn(sendAddr, publishAddress);
		}else {
			System.out.println("用户还未获得发布地址，无法发布！");
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 1024-13-4; i++) {
			sb.append('a');
		}
		String msg = sb.toString();
		//发三倍，防止丢包
		long startTime = System.currentTimeMillis(), endTime;
		for (int i = 0; i < num * 1.5; i++) {
			send.publish(id, sendTopic, i + ":" + System.currentTimeMillis() + ":" + msg);
			endTime = System.currentTimeMillis();
			if (endTime - startTime > 1000) {
				try {
					Thread.sleep(endTime - startTime);
					startTime = System.currentTimeMillis();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("over");
	}
}
