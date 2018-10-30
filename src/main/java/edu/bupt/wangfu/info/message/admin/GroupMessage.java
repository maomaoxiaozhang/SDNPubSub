package edu.bupt.wangfu.info.message.admin;

import edu.bupt.wangfu.info.device.Switch;

import java.util.List;

/**
 * 集群内部消息，通过管理路径上报
 */
public class GroupMessage {
    //集群名
    String groupName;

    //集群内的交换机列表
    List<Switch> switchList;

    //to-do 添加集群信息

}
