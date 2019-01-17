package edu.bupt.wangfu.module.routeMgr.util;

/**
 * 边，存储起始和终止节点、长度
 */

public class Edge implements Comparable{
	private int length;
	private Node startNode;
	private Node endNode;

	public Edge() {
	}

	public Edge(Node startNode, Node endNode, int length) {
		// TODO Auto-generated constructor stub
		this.startNode = startNode;
		this.endNode = endNode;
		this.length = length;
	}

	public void setLength(int length){
		this.length = length;
	}
	
	public int getLength(){
		return this.length;
	}
	
	public void setStartNode(Node startNode){
		this.startNode = startNode;
	}
	
	public Node getStartNode(){
		return this.startNode;
	}
	
	public void setEndNode(Node endNode){
		this.endNode = endNode;
	}
	
	public Node getEndNode(){
		return this.endNode;
	}

	//根据边的长度升序排列
	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		Edge other = (Edge)o;
		if(this.length > other.length)
			return 1;
		else
			return -1;
	}

	@Override
	public String toString() {
		return "Edge{" +
				"length=" + length +
				", startNode=" + startNode +
				", endNode=" + endNode +
				'}';
	}
}
