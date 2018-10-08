package edu.bupt.wangfu.info.device;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

/**
 * 集群控制器信息
 *
 * @author caoming
 */
@Data
@Component
public class Controller {
    @Value("${GroupController:10.108.166.14}")
    private String GroupControllerAddr;

    @Value("${adminName:G1}")
    private String adminName;

    //本地集群名
    @Value("${localGroupName:G1}")
    private String localGroupName;

    //本地集群地址
    @Value("10.108.166.57")
    private String localAddr;

    @Value("30001")
    private int adminPort;

    @Value("30002")
    private int sysPort;

    @Value("30003")
    private int notifyPort;

    @Value("FF0E:0000:0000:0000:0001:2345:6789:ABCD")
    private String sysV6Addr;

    @Value("FF01:0000:0000:0000:0001:2345:6789:ABCD")
    private String adminV6Addr;

    //本地交换机，key是swtId
    @Value("${switches}")
    private Map<String, Switch> switches;

    //本地拥有对外端口的交换机，key是交换机id，value是对应的交换机
    @Value("${outSwitches}")
    private Map<String, Switch> outSwitches;

//    private LsdbMsg lsdb;

//    //存储邻居集群控制器信息，key为集群名称，value为集群控制器信息
//    private Map<String, Controller> neighbors;

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
    private Map<String, Set<Flow>> notifyFlows;

    //控制器所在交换机id
    @Value("${localSwtId:139329991887403}")
    private String localSwtId;
}
