package edu.bupt.wangfu.info.message.system;

import edu.bupt.wangfu.module.topologyMgr.util.Lsa;
import lombok.Data;
import lombok.NoArgsConstructor;
import edu.bupt.wangfu.module.topologyMgr.ospf.State;

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

    //Hello消息的状态
    private State state = State.down;

    //Hello消息中包含LSA信息
    private Lsa lsa;

    //类型：拓扑发现、心跳、发布订阅更新
    private String type;

    //角色，分为控制器、管理员
    private String role;
}
