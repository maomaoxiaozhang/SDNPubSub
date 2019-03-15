package edu.bupt.wangfu.infc;

import edu.bupt.wangfu.module.wsnMgr.util.soap.SendWSNCommand;
import edu.bupt.wangfu.module.wsnMgr.util.soap.wsn.UserNotificationProcessImpl;

import javax.xml.ws.Endpoint;

import static edu.bupt.wangfu.infc.Test.*;

/**
 * 订阅接口
 */
public class Subscribe {

    public static SendWSNCommand receive;

    public Subscribe(String receiveAddr, String wsnAddr, String id, String topic) {
        receive = new SendWSNCommand(receiveAddr, wsnAddr);
        // 消息处理逻辑
        UserNotificationProcessImpl implementor = new UserNotificationProcessImpl();
        // 开启接收服务
        Endpoint endpint = Endpoint.publish(receiveAddr, implementor);
        receive.subscribe(id, topic);
    }

    public static void main(String[] args) {
        Subscribe sub = new Subscribe(receiveAddr, wsnAddr, "sub_id", "test1");
    }
}
