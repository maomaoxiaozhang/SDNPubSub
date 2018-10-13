package edu.bupt.wangfu.module.wsnMgr.util.soap;

import javax.jws.WebService;

/**
 * wsn层监听，接收用户订阅请求，消息格式为：subscribe-address-topic
 */
@WebService(endpointInterface= "edu.bupt.wangfu.module.wsnMgr.util.soap.INotificationProcess",
        serviceName="INotificationProcess")
public class NotificationProcessWSNImpl implements INotificationProcess {

    public void notificationProcess(String notification) {
        if (notification.startsWith("subscribe")) {
            String address = notification.split("-")[1];
            String topic = notification.split("-")[2];

        }

    }
}