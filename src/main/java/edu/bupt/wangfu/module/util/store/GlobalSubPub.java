package edu.bupt.wangfu.module.util.store;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 全局发布订阅情况
 */
@Data
public class GlobalSubPub {
    //全局订阅表，key —— 集群名，value —— 订阅主题
    private Map<String, List<String>> globalSubMap = new HashMap<>();

    //全局发布表，key —— 集群名， value —— 发布主题
    private Map<String, List<String>> globalPubMap = new HashMap<>();
}
