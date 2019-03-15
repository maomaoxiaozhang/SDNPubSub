package edu.bupt.wangfu.infc;

import edu.bupt.wangfu.module.wsnMgr.util.soap.SendWSNCommandWSSyn;
import edu.bupt.wangfu.module.wsnMgr.util.soap.wsn.SendNotificationProcessImpl;

import javax.xml.ws.Endpoint;

import java.util.concurrent.TimeUnit;

import static edu.bupt.wangfu.infc.Test.wsnAddr;
import static edu.bupt.wangfu.infc.Test.sendAddr;

/**
 * 发布接口
 */
public class Publish {
    public static SendWSNCommandWSSyn register;

    public static String publishAddress = "";

    public static SendWSNCommandWSSyn send;

    private String id;

    private String topic;

    public Publish(String wsnAddr, String sendAddr, String id, String topic) {
        this.id = id;
        this.topic = topic;
        register = new SendWSNCommandWSSyn(sendAddr, wsnAddr);
        SendNotificationProcessImpl impl = new SendNotificationProcessImpl();
        Endpoint.publish(sendAddr, impl);
        register.register(id, topic, sendAddr);
    }

    public void sendMethod(String msg) {
        if (!publishAddress.equals("")) {
            send = new SendWSNCommandWSSyn(sendAddr, publishAddress);
            send.publish(id, topic, msg);
        }else {
            System.out.println("用户还未获得发布地址，无法发布！");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Publish pub = new Publish(wsnAddr, sendAddr, "pub_id", "test1");
        TimeUnit.SECONDS.sleep(3);
        pub.sendMethod("query");
    }
}
