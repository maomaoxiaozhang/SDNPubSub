package edu.bupt.wangfu.module.wsnMgr.util.soap.wsn;

import edu.bupt.wangfu.infc.Publish;
import edu.bupt.wangfu.module.wsnMgr.util.soap.INotificationProcess;
import edu.bupt.wangfu.role.user.publish.Trans;
import org.springframework.stereotype.Component;

import javax.jws.WebService;

/**
 * 发送方处理程序，在发布注册后得到返回的 publishAddress
 */
@WebService(endpointInterface= "edu.bupt.wangfu.module.wsnMgr.util.soap.INotificationProcess",
        serviceName="NotificationProcessImpl")
public class SendNotificationProcessImpl implements INotificationProcess {
    @Override
    public void notificationProcess(String notification) {
        Publish.publishAddress = splitString(notification, "<message>", "</message>");
        System.out.println("注册成功，返回发布地址：" + Publish.publishAddress);
    }

    public String splitString(String string, String start, String end)
    {
        int from = string.indexOf(start) + start.length();
        int to = string.indexOf(end);
        return string.substring(from, to);
    }
}
