package info.device;

import info.msg.LSA;
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

    //本地集群名
    @Value("${localGroupName:G1}")
    private String localGroupName;

    //key是swtId，本集群所有拥有对外端口的swt的集合
    @Value("${outSwitches}")
    private Map<String, Switch> outSwitches;

    //链路状态数据库，key是groupName，当前网络中所有集群的连接情况
    private Map<String, LSA> LSDB;

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
