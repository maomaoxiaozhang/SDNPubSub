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

    //增量差法数据
    private Long inRate = 0L;
    private Long outRate = 0L;
    private Long lastInRate = 0L;
    private Long lastOutRate = 0L;
    private Double avgQ = 0.0;
    private String uuid;
    private Long nowRate;
}
