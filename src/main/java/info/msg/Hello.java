package info.msg;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Hello implements Serializable{
    private static final long serialVersionUID = 1L;

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
}
