package module.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.Inet6Address;
import java.net.MulticastSocket;

/**
 * 集群控制器间传输方式，通过v6组播
 * 集群控制器与客户端（wsn层）通过ws进行通信
 */
@Component
public class MultiHandler {
	private int port;
	//形如FF01:0000:0000:0000:0001:2345:6789:abcd，128bit
	private String v6addr;

	@Autowired
	private Encode encode;

	public MultiHandler(int port, String topic, String topicType) {
		this.port = port;
		if (topicType.equals("sys")) {
			v6addr = encode.getSysTopic2code().get(topic);
			this.port += v6addr.charAt(v6addr.length() - 1) - '0';
		} else if (topicType.equals("notify")) {
			v6addr = encode.getNotifyTopic2code().get(topic);
		}
	}

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
			multicastSocket.send(datagramPacket);//发送数据包

			multicastSocket.close();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}