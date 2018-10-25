package edu.bupt.wangfu.module.wsnMgr.util.soap.wsn;

import edu.bupt.wangfu.info.device.Controller;
import edu.bupt.wangfu.info.device.User;
import edu.bupt.wangfu.info.message.wsn.SubPubMsg;
import edu.bupt.wangfu.module.util.MultiHandler;
import edu.bupt.wangfu.module.wsnMgr.WsnMgr;
import edu.bupt.wangfu.module.wsnMgr.util.soap.INotificationProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.jws.WebService;

import static edu.bupt.wangfu.module.util.Constant.*;

/**
 * wsn 层监听，接收本地的发布订阅消息
 */
@WebService(endpointInterface= "edu.bupt.wangfu.module.wsnMgr.util.soap.INotificationProcess",
        serviceName="WsnNotificationProcessImpl")
@Component
public class WsnNotificationProcessImpl implements INotificationProcess{

    @Autowired
    Controller controller;

    @Autowired
    @Lazy
    WsnMgr wsnMgr;

    /**
     * 接收来自用户的订阅请求，将新的订阅信息保存本地并向控制器发送
     * @param notification
     */
    @Override
    public void notificationProcess(String notification) {
        String endpoint = splitString("<wsa:Address>", "</wsa:Address>", notification);
        String topic = splitString("<wsnt:TopicExpression Dialect=\"http://docs.oasis-open.org/wsn/t-1/TopicExpression/Simple\">",
                "</wsnt:TopicExpression>", notification).trim();
        String address = splitString("<wsnt:SubscriberAddress>", "</wsnt:SubscriberAddress>", notification);
        User user = new User();
        user.setAddress(address);
        String encodeAddress = wsnMgr.updateSubPubMap(user, topic);
        if (encodeAddress != null && !encodeAddress.equals("")) {
            send2controller(topic, user, encodeAddress);
        }
    }

    /**
     * 将本地新增订阅情况发送给控制器
     * @param topic
     * @param user
     */
    public void send2controller(String topic, User user, String encodeAddress) {
        SubPubMsg subPubMsg = new SubPubMsg();
        subPubMsg.setGroup(controller.getLocalGroupName());
        subPubMsg.setTopic(topic);
        subPubMsg.setType(SUBSCRIBE);
        subPubMsg.setUser(user);
        subPubMsg.setEncodeAddress(encodeAddress);
        subPubMsg.setSendTime(System.currentTimeMillis());
        int wsnPort = controller.getWsnPort();
        String address = controller.getLocalAddr();
        MultiHandler handler = new MultiHandler(wsnPort, address);
        handler.v6Send(subPubMsg);
    }


    public String splitString(String start, String end, String string)
    {
        int from = string.indexOf(start) + start.length();
        int to = string.indexOf(end);
        return string.substring(from, to);
    }
}
