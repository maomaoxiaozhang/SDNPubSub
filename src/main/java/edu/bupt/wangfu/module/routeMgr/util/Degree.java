package edu.bupt.wangfu.module.routeMgr.util;

import java.util.Set;

/**
 * 计算节点的度
 */

public class Degree {
	public static int DEGREE(Node node, Set<Edge> g2){
		int de = 0;
		for(Edge ed : g2){
			if(ed.getStartNode() == node || ed.getEndNode() == node){
				de++;
			}
		}
		return de;
	}
}
