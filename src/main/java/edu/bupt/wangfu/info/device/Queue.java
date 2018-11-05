package edu.bupt.wangfu.info.device;

import lombok.Data;

/**
 * 保存队列信息
 */
@Data
public class Queue {

    //队列id
    int id;

    //端口id
    int port;

    //传输数据包数量
    long packets;

    //传输字节数目
    long bytes;

    //传输失败的数量
    long errors;

    //队列带宽
    long brandWidth;
}
