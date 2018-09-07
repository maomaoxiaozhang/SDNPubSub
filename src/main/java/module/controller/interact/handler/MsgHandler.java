package module.controller.interact.handler;

import info.device.Controller;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;

public class MsgHandler {
    @Autowired
    private Controller controller;
    private int port;
    private MulticastSocket multicastSocket;
    private Inet6Address inetAddress;

    public MsgHandler(String addr, int port){
        this.port = port;
        try {
            multicastSocket = new MulticastSocket(port);
            inetAddress = (Inet6Address) Inet6Address.getByName(addr);
            multicastSocket.joinGroup(inetAddress);
            multicastSocket.setReceiveBufferSize(1024 * 200000);
            multicastSocket.setInterface(InetAddress.getByName(controller.getLocalAddr()));
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setInterface(String netInterface){
        try {
            multicastSocket.setInterface(InetAddress.getByName(netInterface));
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
    public void send(Object object) {
        try {
            ByteArrayOutputStream baos;
            ObjectOutputStream oos;
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            byte[] msg = baos.toByteArray();
            DatagramPacket datagramPacket = new DatagramPacket(msg, msg.length,inetAddress, port);
            multicastSocket.send(datagramPacket);//发送数据包
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object receive() {
        try {
            ByteArrayInputStream bais;
            ObjectInputStream ois;

            byte[] data = new byte[1280];
            bais = new ByteArrayInputStream(data);
            DatagramPacket datagramPacket = new DatagramPacket(data, data.length);//创建一个用于接收数据的数据包
            multicastSocket.receive(datagramPacket);//接收数据包
            ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception exception) {
            //exception.printStackTrace();
            //System.out.println("异常");
        }
        return null;
    }
}
