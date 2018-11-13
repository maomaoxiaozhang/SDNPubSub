package edu.bupt.wangfu.info.device;

import lombok.Data;

/**
 * 主机信息
 *
 * @author caoming
 */
@Data
public class Host extends DevInfo{
    private String ip;
    private String swId;
    private String port;
    private String sysInfo;
    private String sysName;
    private String mac;
    private String sysMemory;
    private String cpuRate;
    private int ifNum;
    private long lastSeen;

}
