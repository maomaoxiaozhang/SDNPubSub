package edu.bupt.wangfu.info.message.system;

import lombok.Data;

import java.util.ArrayList;
import java.util.Map;

/**
 * 链路状态信息
 */
@Data
public class LsaMsg extends SysMessage{
    //这条LSA消息的序号，避免后发先至
    private Long id;

    // 0为普通LSA，1为同步LSA
    private int syn;

    private String v6Addr;

    //与邻居集群的距离，key -- 邻居集群名，value -- 与邻居集群的距离
    private Map<String, Integer> dist2NbrGrps;

    // 丢失集群，若无丢失则为空
    private ArrayList<String> lostGroup;

    // 发送源的订阅
    private ArrayList<String> subsTopics;

    //发送源取消的订阅
    private ArrayList<String> cancelTopics;

}
