package edu.bupt.wangfu.info.device;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 设备信息，抽象父类
 *
 * @author caoming
 */
@Data
public abstract class DevInfo implements Serializable{
    private static final long serialVersionUID = 1L;

    private String mac;
    //key是端口号，value是设备信息，只包含集群内的邻接设备
    private Map<String, DevInfo> neighbors;
}
