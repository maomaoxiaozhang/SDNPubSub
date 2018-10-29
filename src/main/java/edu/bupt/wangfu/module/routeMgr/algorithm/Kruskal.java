package edu.bupt.wangfu.module.routeMgr.algorithm;

import edu.bupt.wangfu.module.routeMgr.util.Edge;
import edu.bupt.wangfu.module.routeMgr.util.Node;

import java.util.Set;
import java.util.*;

public class Kruskal {

    //返回订阅节点的最小生成树构成的边
    public static Set<Edge> KRUSKAL(Set<Node> select, Set<Edge> e) {
        //存储最小生成树的边
        Set<Edge> g = new TreeSet<>();
        //初始时，每个顶点自成一个连通分量
        ArrayList<HashSet> sets = new ArrayList<>();
        for (Node node : select) {
            HashSet set = new HashSet();
            //存储节点的值
            set.add(node.getName());
            sets.add(set);
        }

        //升序遍历每条边
        for (Edge ed : e) {
            String edgeStart = ed.getStartNode().getName();
            String edgeFinish = ed.getEndNode().getName();
            int edgeValue = ed.getLength();
            //记录两节点所属连通分量位置（属于一个联通分量则构成环）
            int loca_1 = -1, loca_2 = -2;
            for (int j = 0; j < sets.size(); j++) {
                HashSet set = sets.get(j);
                if (set.contains(edgeStart)) {
                    loca_1 = j;
                }
                if (set.contains(edgeFinish)) {
                    loca_2 = j;
                }
            }
            if (loca_1 == loca_2) {
                //属于一个连通分量，舍去这条边
            } else if ((loca_1 == -1 && loca_2 != -2) || (loca_1 != -1 && loca_2 == -2)){
                g.add(ed);
            } else{
                System.out.println("选择边：" + edgeStart + "  ->  " + edgeFinish + "  长度为：" + edgeValue);
                HashSet set_1 = new HashSet();
                set_1 = sets.get(loca_1);
                HashSet set_2 = new HashSet();
                set_2 = sets.get(loca_2);

                if (loca_1 > loca_2) {
                    sets.remove(loca_1);
                    sets.remove(loca_2);
                } else {
                    sets.remove(loca_2);
                    sets.remove(loca_1);
                }

                set_2.addAll(set_1);
                sets.add(set_2);
                g.add(ed);
            }
        }
        return g;
    }
}

