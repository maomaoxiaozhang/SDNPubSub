package edu.bupt.wangfu.info.device;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Component
//@PropertySource("classpath:/manager.properties")
public class Manager{

    //管理员所在集群名
    @Value("${adminName:G2}")
    private String adminName;

    //管理路径下发消息地址
    @Value("${adminV6Addr:FF0E:0000:0000:0000:0001:2345:6789:ABCD}")
    private String adminV6Addr;

    //拓扑维护地址
    @Value("${sysV6Addr:FF0E:0000:0000:0000:0001:2345:6790:ABCD}")
    private String sysV6Addr;

    //管理端口
    @Value("${adminPort:30001}")
    private int adminPort;

    //系统端口
    @Value("${sysPort:30002}")
    private int sysPort;

    //本地交换机，key是swtId
    @Value("${switches:#{null}}")
    private Map<String, Switch> switches;

    //本地拥有对外端口的交换机，key是交换机id，value是对应的交换机
    @Value("${outSwitches:#{null}}")
    private Map<String, Switch> outSwitches;

    //控制器的主机在交换机上的连接端口
    @Value("${switchPort:5}")
    private int switchPort;

    //管理员所在交换机id
    @Value("${localSwtId:139329991887403}")
    private String localSwtId;

    //保存交换机与邻居的端口对应情况，key——集群名，value——与之相接的交换机端口列表
    private Map<String, List<String>> port2nei = new HashMap<>();

    @Value("${groups:#{null}}")
    private Map<String, Controller> groups;


}
