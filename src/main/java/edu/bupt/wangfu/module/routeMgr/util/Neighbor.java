package edu.bupt.wangfu.module.routeMgr.util;

import lombok.Data;

/**
 * @author caoming
 */
@Data
public class Neighbor {
    //邻居节点
    public Node node;

    //与邻居节点的距离
    public int distance;
}