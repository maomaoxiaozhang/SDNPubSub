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
    private String id;
    private double load;
    //端口对应的队列集合
    private Map<Integer, List<Queue>> queues;
}
