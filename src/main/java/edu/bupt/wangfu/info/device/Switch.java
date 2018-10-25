package edu.bupt.wangfu.info.device;

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

    //存储所有对外端口，这里为了快速查找，使用map存储，key -- 交换机id
    private Map<String, String> outPorts;

    private String id;

    private double load;
    private String userName;
    //交换机地址
    private String address;
    //交换机登录密码
    private String password;

    //端口对应的队列集合
    private Map<Integer, List<Queue>> queues;
}
