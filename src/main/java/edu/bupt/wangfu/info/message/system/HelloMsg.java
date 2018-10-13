package edu.bupt.wangfu.info.message.system;

import lombok.Data;
import lombok.NoArgsConstructor;
import edu.bupt.wangfu.module.topologyMgr.ospf.State;

import java.io.Serializable;

/**
 * 通过Hello消息探测邻居，同时在Hello消息中封装了LSA内容
 * 通过Hello消息进行心跳维护、定时交换LSA
 */

@Data
@NoArgsConstructor
public class HelloMsg extends SysMessage{
    //Hello消息发送集群
    private String startGroup;
    //Hello消息接收集群
    private String endGroup;

    //消息发送方边界switchId
    private String startBorderSwtId;
    //消息接收方边界switchId
    private String endBorderSwtId;

    //消息发送方出端口
    private String startOutPort;
    //消息接收方进端口
    private String endOutPort;

    //Hello消息失效时间
    private long reHelloPeriod;

    //Hello消息的状态
    private State state = State.down;

    //Hello消息中包含LSA信息
    private LsaMsg lsa = new LsaMsg();
}
