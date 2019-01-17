package edu.bupt.wangfu.module.routeMgr.algorithm;

import edu.bupt.wangfu.module.routeMgr.util.*;

import java.util.*;

/**
 * <p>
 *     steiner 最小生成树算法，步骤包括：
 *         1. 使用 Floyed 算法计算完全连通图
 *         2. 求所有发布、订阅节点构成的最小生成树（kruskal）
 *         3. 求扩展子图G2的最小生成树
 *		   4. 删除度为1的顶点
 * </p>
 *
 * <p>
 *     G：图中所有节点的集合，由allNodes可以得到
 *     D：所选节点的集合，由giveNodes给予点表示
 *     e：所有边的集合
 *     g：最小生成树采用的边的集合
 *     G2：最小生成树经过的节点集合
 * </p>
 *
 * @see Edge
 * @see Degree
 * @see Kruskal
 */

public class Steiner {

//	/**
//	 * 测试专用，可生成指定数目的节点
//	 *
//	 * @param number
//	 * @return
//	 */
//	public static Set<Node> genNode(int number){
//		Set<Node> allNodes = new HashSet<>();
//		//生成number个Node
//		for(int i = 0; i < number; i++){
//			Node no = new Node(String.valueOf(i));
//			allNodes.add(no);
//		}
//		int ran;
//		//用随机数的方法互连
//		for(int i = 0; i < number; i++){
//			Set<Integer> conn = new HashSet<Integer>(number);
//			//取number个随机数
////			for(int j = 0; j < number; j++) {
////				//获取0到number - 1之间的随机数
////				ran = (int)(0 + Math.random()*(number - 1 - 0 + 1));
////			    conn.add(ran);
////			}
//			for(int j = 0; j < number; j++){
//				ran = (int)(0 + Math.random()*(2));
//				if(ran != 0)
//					conn.add(j);
//			}
//			conn.remove(i);
//			for(int inSet : conn) {
//				Node startNode = getNode(i, allNodes);
//				Node endNode = getNode(inSet, allNodes);
//				boolean isIn = false;
//				for(int m = 0; m < startNode.getNeighbors().size(); m++){
//					if(startNode.getNeighbors().get(m).node == endNode){
//						isIn = true;
//						break;
//					}
//				}
//				if(isIn){
//					//之前有过，不添加
//				}
//				else{
//					// 将两个group的距离设置为随机数（1~10）
//					int tempRan = (int)(1 + Math.random()*(10));
//					Neighbor endNe = new Neighbor(endNode, tempRan);
//					Neighbor startNe = new Neighbor(startNode, tempRan);
//					startNode.addNeighbor(endNe);
//					endNode.addNeighbor(startNe);
//				}
//			}
//			conn.clear();
//		}
//		return allNodes;
//	}

    public static Node getNode(int value, Set<Node> allNodes){
        Node temp = null;
        for(Node no : allNodes){
            if(no.getName().equals(String.valueOf(value))){
                temp = no;
                break;
            }
        }
        return temp;
    }

    public static Set<Edge> steiner(Set<Node> allNodes, Set<Node> select){
        if (select.size() <= 1)  {
            System.out.println("只有一个节点，无需下发流表");
            return new HashSet<>();
        }
        int max = Integer.MAX_VALUE;
        int dist[][] = new int[allNodes.size()][allNodes.size()];
        Dijkstra dijkstra = new Dijkstra();
        //初始化距离向量
        for(int i = 0; i < allNodes.size(); i++){
            for(int j = 0; j < allNodes.size(); j++){
                if(i == j){
                    dist[i][j] = 0;
                }
                else{
                    dist[i][j] = max;
                }
            }
        }
        Node[] all = allNodes.toArray(new Node[allNodes.size()]);
        for(int i = 0; i < all.length; i++){
            List<Neighbor> neighbors = all[i].getNeighbors();
            for(Neighbor ne : neighbors){
                int seq = calSeq(ne, all);
                dist[i][seq] = ne.distance;
            }
        }

        /*		第一步，求得完全连通图*/

        ///Floyed算法求得完全连通图
        for(int i = 0; i < dist.length; i++)
            for(int j = 0; j < dist.length; j++)
                for(int k = 0; k < dist.length; k++)
                    if((dist[i][k] != Integer.MAX_VALUE && dist[k][j] != Integer.MAX_VALUE) &&
                            (dist[i][j] == Integer.MAX_VALUE || dist[i][j] > dist[i][k] + dist[k][j])) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                    }

        /*      第二步，求D的最小生成树*/

        //生成边的集合e
        TreeSet<Edge> e = new TreeSet<>();
        for(int i = 0; i < dist.length; i++)
            for(int j = i + 1; j < dist.length; j++){
                if(dist[i][j] != Integer.MAX_VALUE){
                    Edge ed = new Edge(all[i], all[j], dist[i][j]);
                    e.add(ed);
                }
            }
        //g为最小生成树的边集合
        Set<Edge> g = Kruskal.KRUSKAL(select, e);
        //G2为最小生成树经过的节点集合
        Set<Node> G2 = new HashSet<>();
        //将最小生成树的边起始点和终止点取出，计算最短路径经过的节点，加入G2中
        for(Edge ed : g){
            Node startNode = ed.getStartNode();
            Node endNode = ed.getEndNode();
            List<String> temp = dijkstra.dijkstra(startNode, endNode, allNodes);
            for(String no : temp){
                G2.add(BuildTopology.find(no, allNodes));
            }
        }
        select.addAll(G2);

        /*		第三步，求扩展子图G2的最小生成树*/

        //g2为新的最小生成树的边集合
        Set<Edge> g2 = Kruskal.KRUSKAL(select, e);
        //G3为采用的节点集合
        Set<Node> G3 = new HashSet<>();
        for(Edge ed : g2){
            Node startNode = ed.getStartNode();
            Node endNode = ed.getEndNode();
            G3.add(startNode);
            G3.add(endNode);
        }

        /*		第四步，删除度为1的顶点*/

        Iterator<Node> nodeIterator = G3.iterator();
        while (nodeIterator.hasNext()) {
            Node no = nodeIterator.next();
            if (!select.contains(no)) {
                int de = Degree.DEGREE(no, g2);
                if(de == 1){
                    nodeIterator.remove();
                    Iterator<Edge> edgeIterator = g2.iterator();
                    while (edgeIterator.hasNext()){
                        Edge edge = edgeIterator.next();
                        if (edge.getStartNode().getName().equals(no.getName()) ||
                                edge.getEndNode().getName().equals(no.getName())) {
                            edgeIterator.remove();
                        }
                    }
                }
            }
        }
        return g2;
    }

    //将Node转换为数组中的序号
    public static int calSeq(Neighbor ne, Node[] all){
        for(int i = 0; i < all.length; i++){
            if(all[i].getName().equals(ne.getName())){
                return i;
            }
        }
        //不存在
        return -1;
    }
}
