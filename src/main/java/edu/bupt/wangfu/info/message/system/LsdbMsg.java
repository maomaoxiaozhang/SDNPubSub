package edu.bupt.wangfu.info.message.system;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 链路状态数据库，key是groupName，当前网络中所有集群的连接情况
 */
@Data
public class LsdbMsg extends SysMessage{

    //保存本地收到的lsa消息，key —— 集群名，value —— lsa
    private Map<String, LsaMsg> LSDB = new HashMap<>();
}
