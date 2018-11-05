package edu.bupt.wangfu.info.message.admin;

import edu.bupt.wangfu.info.device.Controller;
import edu.bupt.wangfu.info.device.Switch;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 集群内部消息，通过管理路径上报
 * 控制器 --> 管理员
 *
 * @see EncodeTopicTreeMsg
 */
@Data
public class GroupMessage extends AdminMessage{
    //集群名
    String groupName;

    //集群内的交换机列表
    Map<String, Switch> switchMap;

    //集群内控制器信息
    Controller controller;

    //to-do 添加集群信息

}