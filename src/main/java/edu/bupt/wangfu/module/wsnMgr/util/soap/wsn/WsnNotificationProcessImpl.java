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
                user = new User();
                user.setId(id);
                userAddress = splitString(notification, "<receiveAddress>", "</receiveAddress>");
                user.setAddress(userAddress);
                //开启新的监听
                encodeAddress = wsnMgr.addListener(topic);
                //将该用户订阅信息保存至本地订阅表
                wsnMgr.registerSub(user, topic);
                //向控制器上报订阅信息
                if (encodeAddress != null && !encodeAddress.equals("")) {
                    send2controller(topic, user, encodeAddress, SUBSCRIBE);
                }
                break;
            case CONFIG:
                //用户配置请求
                delay = Long.parseLong(splitString(notification, "<delay>", "</delay>"));
                lostRate = Double.parseDouble(splitString(notification, "<lostRate>", "</lostRate>"));
                UserRequestMsg msg = new UserRequestMsg();
                user = wsnMgr.findUser(id);
                if (user == null) {
                    System.out.println("该用户尚未进行订阅注册，请先注册订阅用户：" + id);
                }else {
                    msg.setTopic(topic);
                    msg.setDelay(delay);
                    msg.setLostRate(lostRate);
                    msg.setUser(user);
                    config2controller(msg);
                }
                break;
            case REGISTER:
                user = new User();
                user.setId(id);
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
    public void config2controller(UserRequestMsg msg) {
        int wsnPort = controller.getWsnPort();
        String address = controller.getLocalAddr();
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
        }else if (str.startsWith("<wsnt:Publish")) {
            return PUBLISH;
        }else if ((str.startsWith("<wsnt:Config"))) {
            return CONFIG;
        }else if ((str.startsWith("<wsnt:Register"))) {
            return REGISTER;
        }else {
            return UNKNOWN;
        }
    }
}
