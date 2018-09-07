package info.device;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * 交换机信息
 *
 * @author caoming
 */
@Data
public class Switch extends DevInfo{
    //存储所有端口集合
    private Set<String> ports;
    //存储所有对外端口，key是集群，value是对应端口
    private Map<String, String> outPorts;
    private String id;
    private double load;
    private String userName;
    private String address;
    private String password;
    //端口对应的队列集合
    private Map<Integer, List<Queue>> queues;
}
