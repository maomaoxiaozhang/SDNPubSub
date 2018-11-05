package edu.bupt.wangfu.info.message.admin;

import lombok.Data;

/**
 * 管理员反馈
 */
@Data
public class RequestFeedBackMsg extends AdminMessage{

    //队列
    int queue;

    //端口
    int port;

    //带宽
    double bind;
}
