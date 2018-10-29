package edu.bupt.wangfu.module.topologyMgr.util;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 链路状态数据库，key是groupName，当前网络中所有集群的连接情况
 */
@Data
@Component
public class Lsdb {
    //保存本地收到的lsa消息，key —— 集群名，value —— lsa
    private Map<String, Lsa> LSDB = new HashMap<>();
}
