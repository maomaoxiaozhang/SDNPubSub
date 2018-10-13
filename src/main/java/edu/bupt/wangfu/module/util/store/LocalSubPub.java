package edu.bupt.wangfu.module.util.store;

import edu.bupt.wangfu.info.device.User;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 本地发布订阅情况
 */
@Data
public class LocalSubPub {
    //本地订阅表，key —— 用户信息，value —— 订阅主题
    private Map<User, List<String>> localSubMap = new HashMap<>();

    //本地发布表，key —— 用户信息，value —— 发布主题
    private Map<User, List<String>> localPubMap = new HashMap<>();
}
