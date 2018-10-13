package edu.bupt.wangfu.module.util;

import edu.bupt.wangfu.info.device.Controller;
import edu.bupt.wangfu.module.topicTreeMgr.TopicTreeMgr;
import edu.bupt.wangfu.module.topicTreeMgr.util.Encode;
import edu.bupt.wangfu.module.topicTreeMgr.util.EncodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static edu.bupt.wangfu.module.util.Constant.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.Inet6Address;
import java.net.MulticastSocket;

/**
 * 提供消息的传输与接收方法
 *
 * <p>
 *     集群控制器间传输方式，通过v6组播：
 *         controller <——> controller
 *
 *     集群控制器与客户端（wsn层）通过ws进行通信：
 *         controller <——> wsnMgr <——> user
 * </p>
 *
 * <p>
 *     消息分为系统消息system、管理消息admin、发布订阅消息notify
 *     system：采用默认编码
 *     admin：管理消息，采用默认编码
 *     sub_pub：主题树编码{@code encode}
 * </p>
 *
 * @see TopicTreeMgr
 */
public class MultiHandler {
	private int port;
	//形如FF01:0000:0000:0000:0001:2345:6789:abcd，128bit
	private String v6addr;

//	@Autowired
//    private TopicTreeMgr topicTreeMgr;

//	@Autowired
//    private Controller controller;

	public MultiHandler() {
	}

	public MultiHandler(int port, String v6addr) {
		this.port = port;
		this.v6addr = v6addr;
	}

//	public MultiHandler(String topic, String topicType, Controller controller, TopicTreeMgr topicTreeMgr) {
//	    switch (topicType) {
//            case SYSTEM:
//                port = controller.getSysPort();
//                v6addr = controller.getSysV6Addr();
//                break;
//            case ADMIN:
//                port = controller.getAdminPort();
//                v6addr = controller.getAdminV6Addr();
//                break;
//            case WSN:
//                port = controller.getWsnPort();
//                v6addr = EncodeUtil.getEncodeEntry(topic, topicTreeMgr.getEncodeTopicTree()).getEncode();
//                break;
//            default:
//                break;
//        }
//	}

	public Object v6Receive() {
		try {
			MulticastSocket multicastSocket = new MulticastSocket(port);
			Inet6Address inetAddress = (Inet6Address) Inet6Address.getByName(v6addr);
			//多播套接字加入多播组
			multicastSocket.joinGroup(inetAddress);
			ByteArrayInputStream bais;
			ObjectInputStream ois;

			byte[] data = new byte[409600];
			bais = new ByteArrayInputStream(data);
			//创建一个用于接收数据的数据包
			DatagramPacket datagramPacket = new DatagramPacket(data, data.length);
			//接收数据包
			multicastSocket.receive(datagramPacket);
			ois = new ObjectInputStream(bais);
			multicastSocket.close();
			return ois.readObject();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return null;
	}

	public void v6Send(Object obj) {
		try {
			ByteArrayOutputStream baos;
			ObjectOutputStream oos;
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(obj);
			byte[] msg = baos.toByteArray();
			//根据主题名返回主题的IP地址
			Inet6Address inetAddress = (Inet6Address) Inet6Address.getByName(v6addr);
			//这里的端口没有用，最终转发还是看流表
			DatagramPacket datagramPacket = new DatagramPacket(msg, msg.length, inetAddress, port);
			MulticastSocket multicastSocket = new MulticastSocket();
			multicastSocket.joinGroup(inetAddress);
			multicastSocket.send(datagramPacket);//发送数据包
			multicastSocket.close();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}