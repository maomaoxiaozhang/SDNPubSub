package edu.bupt.wangfu.module.routeMgr.util;

import lombok.Data;

import java.util.ArrayList;

/**
 * 将网络拓扑中的集群抽象为节点
 *
 * @author caoming
 * @see Neighbor
 */
@Data
public class Node {
    //集群名
    private String name;

    //邻居节点
    private ArrayList<Neighbor> neighbors;

    //保存当前节点与目标节点的距离，中间会有过路节点
    private int sum;
}
