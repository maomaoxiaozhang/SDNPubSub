package module.controller.ospf;

import info.device.Controller;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Timer;

/**
 * 拓扑建立过程，参考OSPF协议，以集群为单位：
 * 1. 集群控制器刚接入拓扑，需要对外发送Hello消息，并监听对外端口
 * 2. 收到Hello消息则将对面集群添加至本地邻居表，封装本地信息并返回ReHello消息
 * 3. 本地集群收到ReHello消息，连接建立
 */
@Component
public class TopoBuild {

    @Autowired
    private HelloReceiver receiver;

    public void build() {
        new Thread(receiver).start();
        new Thread().start();
    }
}
