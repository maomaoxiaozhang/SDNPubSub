package edu.bupt.wangfu.info.device;

import edu.bupt.wangfu.module.routeMgr.util.Edge;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 集群控制器信息
 *
 * @author caoming
 */
@Data
@Component
@PropertySource("classpath:/controller.properties")
public class Controller {

    @Value("${GroupControllerAddr:192.168.10.101}")
    private String GroupControllerAddr;

    //保存管理员所在集群名
    @Value("${adminName:null}")
    private String adminName;

    //本地集群名
    @Value("${localGroupName:G1}")
    private String localGroupName;

    @Value("${role:controller}")
    private String role;

    //本地集群地址
    @Value("${localAddr:FF0E:0000:0000:0000:0001:2345:6792:abcd}")
    private String localAddr;

    @Value("${adminV6Addr:FF0E:0000:0000:0000:0001:2345:6789:ABCD}")
    private String adminV6Addr;

    @Value("${sysV6Addr:FF0E:0000:0000:0000:0001:2345:6790:ABCD}")
    private String sysV6Addr;

    @Value("${sysV6Addr:FF0E:0000:0000:0000:0001:2345:6791:ABCD}")
    private String wsnV6Addr;

    @Value("${wsnAddr:http://192.168.10.101:9010/wsn-core}")
    private String wsnAddr;

    @Value("${adminPort:30001}")
    private int adminPort;

    @Value("${sysPort:30002}")
    private int sysPort;

    @Value("${wsnPort:30003}")
    private int wsnPort;

    @Value("${topicPort:30004}")
    private int topicPort;

    //控制器的主机在交换机上的连接端口
    @Value("${switchPort:5}")
    private int switchPort;

    //本地交换机，key是swtId
    @Value("${switches:#{null}}")
    private Map<String, Switch> switches;

    //本地拥有对外端口的交换机，key是交换机id，value是对应的交换机
    @Value("${outSwitches:#{null}}")
    private Map<String, Switch> outSwitches;

    //Hello 消息有效时间
    @Value("${HelloAliveTime:30000}")
    private long HelloAliveTime;

    //ReHello 消息有效时间
    @Value("${ReHelloAliveTime:30000}")
    private long ReHelloAliveTime;

    //Hello 消息有效时间
    @Value("${FinalHelloAliveTime:30000}")
    private long FinalHelloAliveTime;

    @Value("${helloTaskPeriod:90000}")
    private long helloTaskPeriod;

    @Value("${helloPeriod:50000}")
    private long helloPeriod;

    @Value("${reHelloPeriod:40000}")
    private long reHelloPeriod;

    @Value("${nbrGrpExpiration:120000}")
    private long nbrGrpExpiration;

    //存储当前控制器下已有流表条目数
    private int flowCount;

    //保存本地下发的流表，key -- 订阅的主题，value -- 该主题对应的所有流表
    private Map<String, Set<Flow>> notifyFlows = new HashMap<>();

    //控制器所在交换机id
    @Value("${localSwtId:139329991887403}")
    private String localSwtId;

    //保存交换机与邻居的端口对应情况，key——集群名，value——与之相接的交换机端口列表
    private Map<String, List<String>> port2nei = new HashMap<>();
}
