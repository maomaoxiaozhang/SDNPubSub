package edu.bupt.wangfu.module.topologyMgr.util;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;

/**
 * 链路状态信息，这里用于保存本地LSA
 */
@Data
@Component
public class Lsa implements Serializable{
    private static final long serialVersionUID = 1L;

    //这条LSA消息的序号，避免后发先至
    private Long id;

    //本集群名
    private String groupName;

    //本集群控制器所在v6地址，systemAddress
    private String address;

    //本集群控制器的监听端口
    private int port;

    //与邻居集群的距离，key —— 邻居集群名，value —— 与邻居集群的距离
    private Map<String, Integer> dist2NbrGrps = new HashMap<>();

    // 丢失集群，若无丢失则为空
    private List<String> lostGroup = new LinkedList<>();

    // 发送源的订阅
    private List<String> subTopics = new LinkedList<>();

    // 发送源的发布
    private List<String> pubTopics = new LinkedList<>();

    //发送源取消的订阅
    private List<String> cancelTopics = new LinkedList<>();

    //发送时间，太久则lsa失效
    private Long sendTime;

    //当有新的订阅时需要的编码地址
    private String encodeAddress;

    //当有新的订阅时的主题
    private String topic;
}
