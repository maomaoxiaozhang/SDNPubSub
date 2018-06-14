package module.wsn;

import java.awt.Toolkit;
import java.io.IOException;

import javax.jws.WebService;

import subscribe.Client;
import subscribe.RTPpacket;
import sun.misc.BASE64Decoder;

@WebService(endpointInterface= "module.wsn.INotificationProcess",
		serviceName="INotificationProcess")
public class NotificationProcessImpl implements INotificationProcess{
	Client client = Client.getInstance();

	private static int counter = 0;
	public void notificationProcess(String notification) {
		counter++;
		if (notification.startsWith("rtcp")) {
			String result = notification.split("--")[1];
			String state = notification.split("--")[2];
			if (result.equals("ok")) {
				if (client.state == client.INIT && state.equals("setup")) {
					System.out.println("订阅成功！");
					client.state = client.READY;
				}else if (client.state == client.READY && state.equals("play")) {
					client.state = client.PLAYING;
					client.timer.start();
				}else if (client.state == client.PLAYING && state.equals("pause")) {
					client.state = client.READY;
					client.timer.stop();
				}else {
					System.out.println("未匹配状态");
				}
			}
		}else if (notification.startsWith("rtp") && client.state == client.PLAYING) {
			String rtp = notification.split("--")[1];
			BASE64Decoder decoder = new BASE64Decoder();
			byte[] bytes = null;
			try {
				bytes = decoder.decodeBuffer(rtp);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			//解析成RTCPPacket，加入fram队列
			RTPpacket rtp_packet = new RTPpacket(bytes, bytes.length);
			int payload_length = rtp_packet.getpayload_length();
	        byte [] payload = new byte[payload_length];
	        rtp_packet.getpayload(payload);
	        int seqNb = rtp_packet.getsequencenumber();
			Toolkit toolkit = Toolkit.getDefaultToolkit();
	        client.fsynch.addFrame(toolkit.createImage(payload, 0, payload_length), seqNb);
		}

	}

}