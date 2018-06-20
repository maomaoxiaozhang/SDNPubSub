package info.msg;

import lombok.Data;

import java.util.Map;

/**
 * 链路状态信息
 */
@Data
public class LSA {
    //这条LSA消息的序号，避免后发先至
    private long id;
    //本地集群名
    private String localGroupName;
    private long updateTime;
    private String groupName;
    //与邻居集群的距离
    private Map<String, Integer> dist2NbrGrps;
}
