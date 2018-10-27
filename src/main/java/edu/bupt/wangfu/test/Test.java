package edu.bupt.wangfu.test;

import java.io.*;
import java.net.*;

public class Test {
    public static void main(String[] args) {

            try {
                ByteArrayOutputStream baos;
                ObjectOutputStream oos;
                baos = new ByteArrayOutputStream();
                oos = new ObjectOutputStream(baos);
                oos.writeObject("lalalla");
                byte[] msg = baos.toByteArray();
                //根据主题名返回主题的IP地址
                Inet6Address inetAddress = (Inet6Address) Inet6Address.getByName("FF0E:0000:0000:0000:0001:2345:6790:ABCD");
                //这里的端口没有用，最终转发还是看流表
                DatagramPacket datagramPacket = new DatagramPacket(msg, msg.length, inetAddress, 4567);
                MulticastSocket multicastSocket = new MulticastSocket(4568);
                multicastSocket.joinGroup(inetAddress);
                while (true) {
                    multicastSocket.send(datagramPacket);//发送数据包
                    System.out.println(datagramPacket.getAddress() + "\t" + datagramPacket.getPort() + "\t" + datagramPacket.getData().length);
                    Thread.sleep(1000);
                }

            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
}
