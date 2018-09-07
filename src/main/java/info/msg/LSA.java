package info.msg;

import lombok.Data;

import java.util.ArrayList;
import java.util.Map;

/**
 * 链路状态信息
 */
@Data
public class LSA {
    private long id;  //这条LSA消息的序号，避免后发先至
    private String localGroupName; //本地集群名
    private int syn; // 0为普通LSA，1为同步LSA
    private long updateTime;
    private String v6Addr;
    private Map<String, Integer> dist2NbrGrps; //与邻居集群的距离
    private ArrayList<String> lostGroup; // 丢失集群，若无丢失则为空
    private ArrayList<String> subsTopics; // 发送源的订阅
    private ArrayList<String> cancelTopics; //发送源取消的订阅

}
