package edu.bupt.wangfu.module.wsnMgr.util.soap.wsn;

import edu.bupt.wangfu.info.device.Controller;
import edu.bupt.wangfu.info.device.User;
import edu.bupt.wangfu.info.message.wsn.UserRequestMsg;
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
 * wsn 层监听，接收本地的发布注册、订阅消息
 * PublishNotificationProcessImpl 负责消息的直接发布
 *
 * @see PublishNotificationProcessImpl
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
        System.out.println("收到消息：" + notification);
        String id, topic, message, encodeAddress, userAddress;
        id = splitString(notification, "<id>", "</id>");
        topic = splitString(notification, "<topic>", "</topic>");
        long delay;
        double lostRate;
        User user;
        switch (getType(notification)) {
            case SUBSCRIBE:
                //订阅
                user = wsnMgr.findUser(id);
                if (user == null) {
                    user = new User();
                    user.setId(id);
                    userAddress = splitString(notification, "<receiveAddress>", "</receiveAddress>");
                    user.setAddress(userAddress);
                }
                //wsn 开启新的监听
                encodeAddress = wsnMgr.addListener(topic);
                //将该用户订阅信息保存至本地订阅表
                wsnMgr.registerSub(user, topic);
                //向控制器上报订阅信息
                if (encodeAddress != null && !encodeAddress.equals("")) {
                    send2controller(topic, user, encodeAddress, SUBSCRIBE);
                }
                //向控制器发送用户配置请求
                delay = Long.parseLong(splitString(notification, "<delay>", "</delay>"));
                lostRate = Double.parseDouble(splitString(notification, "<lostRate>", "</lostRate>"));
                config2controller(topic, delay, lostRate, user);
                break;
            case REGISTER:
                //发布
                user = new User();
                user.setId(id);
                //注册发布信息
                wsnMgr.registerPub(user, topic);
                break;
            default:
                System.out.println("未识别消息类别！");
                break;
        }
    }

    /**
     * 将本地新增订阅情况发送给控制器
     * @param topic
     * @param user
     */
    public void send2controller(String topic, User user, String encodeAddress, String type) {
        SubPubMsg subPubMsg = new SubPubMsg();
        subPubMsg.setGroup(controller.getLocalGroupName());
        subPubMsg.setTopic(topic);
        subPubMsg.setType(type);
        subPubMsg.setUser(user);
        subPubMsg.setEncodeAddress(encodeAddress);
        subPubMsg.setSendTime(System.currentTimeMillis());
        int wsnPort = controller.getWsnPort();
        String address = controller.getLocalAddr();
        MultiHandler handler = new MultiHandler(wsnPort, address);
        handler.v6Send(subPubMsg);
    }

    //将用户配置信息发送给控制器
    public void config2controller(String topic, long delay, double lostRate, User user) {
        int wsnPort = controller.getWsnPort();
        String address = controller.getLocalAddr();
        UserRequestMsg msg = new UserRequestMsg();
        msg.setTopic(topic);
        msg.setDelay(delay);
        msg.setLostRate(lostRate);
        msg.setUser(user);
        MultiHandler handler = new MultiHandler(wsnPort, address);
        handler.v6Send(msg);
    }


    public String splitString(String string, String start, String end)
    {
        int from = string.indexOf(start) + start.length();
        int to = string.indexOf(end);
        return string.substring(from, to);
    }

    public String getType(String str) {
        if (str.startsWith("<wsnt:Subscribe")) {
            return SUBSCRIBE;
        }else if ((str.startsWith("<wsnt:Register"))) {
            return REGISTER;
        }else {
            return UNKNOWN;
        }
    }
}
