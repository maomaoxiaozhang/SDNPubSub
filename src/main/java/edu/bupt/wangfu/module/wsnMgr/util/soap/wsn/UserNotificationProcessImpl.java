package edu.bupt.wangfu.module.wsnMgr.util.soap.wsn;

import edu.bupt.wangfu.module.wsnMgr.util.soap.INotificationProcess;
import edu.bupt.wangfu.role.user.subscribe.MySubscriber;
import edu.bupt.wangfu.role.user.util.RTPpacket;
import sun.misc.BASE64Decoder;

import javax.jws.WebService;
import java.awt.*;
import java.io.IOException;

/**
 * 客户端ws处理程序
 * 对应的是wsn层ws处理程序
 */
@WebService(endpointInterface= "edu.bupt.wangfu.module.wsnMgr.util.soap.INotificationProcess",
        serviceName="NotificationProcessImpl")
public class UserNotificationProcessImpl implements INotificationProcess {

    public void notificationProcess(String notification) {
        String topic = splitString(notification, "<topic>", "</topic>");
        String msg = splitString(notification, "<message>", "</message>");
        if (msg.startsWith("rtp--")) {
            //视频内容
            System.out.println("视频播放中");
            msg = msg.substring(5);
            BASE64Decoder decoder = new BASE64Decoder();
            RTPpacket rtp_packet = null;
            byte[] packet_bits = new byte[0];
            try {
                packet_bits = decoder.decodeBuffer(msg);
            } catch (IOException e) {
                System.out.println("解码错误");
            }
            rtp_packet = new RTPpacket(packet_bits, packet_bits.length);
            int seqNb = rtp_packet.getsequencenumber();
            //get the payload bitstream from the RTPpacket object
            int payload_length = rtp_packet.getpayload_length();
            byte [] payload = new byte[payload_length];
            rtp_packet.getpayload(payload);
            //get an Image object from the payload bitstream
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            MySubscriber.fsynch.addFrame(toolkit.createImage(payload, 0, payload_length), seqNb);
        }else {
            //文本消息
            System.out.println("收到订阅主题 " + topic + " 文本消息：" + msg);
            String[] strings = msg.split(":");
//            int id = Integer.parseInt(strings[0]);
//            if (id <= MySubscriber.num) {
//                MySubscriber.sendTimeList.add(Long.valueOf(strings[1]));
//                MySubscriber.receiveTimeList.add(System.currentTimeMillis());
//            }else {
//                if (MySubscriber.flag){
//                    int count = MySubscriber.sendTimeList.size();
//                    long delay = 0L;
//                    for (int i = 0; i < count; i++) {
//                        delay += (MySubscriber.receiveTimeList.get(i) - MySubscriber.sendTimeList.get(i));
//                    }
//                    MySubscriber.flag = false;
//                    if (count == 0) {
//                        System.out.println("not yet!");
//                    }else {
//                        System.out.println("发包总数：" + MySubscriber.num + " 接收总数：" + count + " 总时延：" + delay + " 平均时延：" + delay/count);
//                    }
//                }else {
//                    //nothing
//                }
//            }
        }
    }

    public String splitString(String string, String start, String end)
    {
        int from = string.indexOf(start) + start.length();
        int to = string.indexOf(end);
        return string.substring(from, to);
    }
}