package edu.bupt.wangfu.module.wsnMgr.util.soap.wsn;

import edu.bupt.wangfu.module.wsnMgr.util.soap.INotificationProcess;

import javax.jws.WebService;

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
        System.out.println("收到订阅主题 " + topic + " 消息：" + msg);
    }

    public String splitString(String string, String start, String end)
    {
        int from = string.indexOf(start) + start.length();
        int to = string.indexOf(end);
        return string.substring(from, to);
    }
}