package info.device;

import info.msg.LSDB;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 集群控制器信息
 *
 * @author caoming
 */
@Data
@Component
public class Controller {
    @Value("${GroupController:10.108.166.14}")
    private String GroupController;

    @Value("${adminName:G1}")
    private String adminName;

    //本地集群名
    @Value("${localGroupName:G1}")
    private String localGroupName;

    //本地集群地址
    @Value("10.108.166.57")
    private String localAddr;

    @Value("30001")
    private int portToAdmin;

    @Value("30002")
    private int sysPort;

    @Value("30003")
    private int notifyPort;

    @Value("FF0E:0000:0000:0000:0001:2345:6789:ABCD")
    private String sysV6Addr;

    @Value("FF01:0000:0000:0000:0001:2345:6789:ABCD")
    private String notifyV6Addr;

    //本地交换机，key是swtId
    @Value("${switches}")
    private Map<String, Switch> switches;

    //拥有对外端口的交换机，key是集群，value是对应的swtId
    @Value("${outSwitches}")
    private Map<String, String> outSwitches;

    private LSDB lsdb;

    //存储邻居集群控制器信息，key为集群名称，value为集群控制器信息
    private Map<String, Controller> neighbors;

    @Value("${firstHelloDelay:30000}")
    private long firstHelloDelay;

    @Value("${helloTaskPeriod:90000}")
    private long helloTaskPeriod;

    @Value("${helloPeriod:50000}")
    private long helloPeriod;

    @Value("${reHelloPeriod:40000}")
    private long reHelloPeriod;

    @Value("${nbrGrpExpiration:120000}")
    private long nbrGrpExpiration;
}
