package module.route.manageRoute;


import module.route.Neighbor;
import module.route.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static java.lang.Integer.MAX_VALUE;

/**
 * 计算单源最短路径
 *
 * @author caoming
 */
public class Dijkstra {
    TreeMap<String,TreeMap<String, Integer>> dists;
    Map<String, String> pathInfo = new HashMap<String, String>();
    Map<String, Integer> path = new HashMap<String, Integer>();


    public  Dijkstra(TreeMap<String,TreeMap<String, Integer>> dists) {
        this.dists = dists;
    }

    /**
     * @param start 起始节点
     */
    public void dijkstra(String start) {
        //String[] prev = new String[size];
        //int[] dist = new int[size];
        //boolean[] flag = new boolean[size];

        // 初始化
        path.put(start, new Integer(0));
        pathInfo.put(start, start);

        // 遍历mVexs.length-1次；每次找出一个顶点的最短路径。
        //int dest = 0;
        //String group;
        //boolean cal = true;
        while (pathInfo.size() != dists.size()) {
            System.out.println("start");
            int shstl = MAX_VALUE;
            String shstn = null;
            String father = "";
            for (String group : pathInfo.keySet()) {
                for (String curGroup : dists.get(group).keySet()) {
                    if (pathInfo.containsKey(curGroup)) {
                        continue;
                    }
                    Integer newCompute = path.get(group)
                                + dists.get(group).get(curGroup);
                    if (shstl > newCompute) {// 之前设置的距离大于新计算出来的距离
                        shstl = newCompute;
                        shstn = curGroup;
                        father = group;
                        //System.out.println(shstl+" "+shstn);
                    }
                }
            }
            if (shstn == null) {
                break;
            }
            pathInfo.put(shstn, father);
            path.put(shstn, shstl);
        }

    }

}
