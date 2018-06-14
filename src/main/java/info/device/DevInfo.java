package info.device;

import lombok.Data;

import java.util.Map;

/**
 * 设备（交换机）信息
 *
 * @author caoming
 */
@Data
public class DevInfo {
    private String mac;
    //key是端口号，value是设备信息，只包含集群内的邻接设备
    private Map<String, DevInfo> neighbors;
}
