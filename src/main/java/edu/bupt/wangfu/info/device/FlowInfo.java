package edu.bupt.wangfu.info.device;

import lombok.Data;

@Data
public class FlowInfo {
    private String controllerId, switchId, portId;
    private Long transmitted, received, bytes, speed, packets, drop;
    private Float lossRate, avgSpeed;
}
