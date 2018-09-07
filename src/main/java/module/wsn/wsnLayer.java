package module.wsn;

import lombok.Data;

import javax.xml.ws.Endpoint;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * wsn层，记录用户请求，当收到消息时向指定用户分发
 * 独立于用户端，设为单例模式
 * wsn的思想表现为开启监听（额外线程），通过单例模式调用程序主体
 *
 * @author caoming
 */
@Data
public class wsnLayer {
    //订阅主题和用户地址的映射
    private Map<String, List<String>> map;
    private static final String receiveAddr = "http://10.108.166.14:9010/wsn-core";

    private static class wsnHolder {
        public static wsnLayer wsn = new wsnLayer();
    }

    public static wsnLayer getInstance() {
        return wsnHolder.wsn;
    }

    public static void main(String[] args) {
        wsnLayer wsn = getInstance();
        NotificationProcessImpl implementor = new NotificationProcessImpl();// 消息处理逻辑
        Endpoint endpoint = Endpoint.publish(receiveAddr, implementor);// 开启接收服务
    }

    /**
     * 用于处理本地用户新订阅请求，新主题或新用户
     * @param topic
     * @param userAddr
     */
    public void add(String topic, String userAddr) {
        List<String> userList = map.get(topic);
        if (userList == null)
            userList = new ArrayList<>();
        userList.add(userAddr);
        map.put(topic, userList);
    }

}
