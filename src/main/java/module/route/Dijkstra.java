package module.route;

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
    private TreeMap<String, Node> open = new TreeMap<>();
    private TreeMap<String, Node> close = new TreeMap<>();
    private ArrayList<String> goal = new ArrayList<>();
    private ArrayList<String> reach = new ArrayList<>();
    // 封装路径信息
    private Map<String, String> pathInfo = new HashMap<String, String>();
    private Map<String, Integer> path = new HashMap<String, Integer>();
    private int MAX_CHILDREN = 4;
    private String groupName;

    /**
     *
     * @param start
     *         起始节点
     */
    public void dijkstra(Node start) {
        path.put(start.getName(), new Integer(0));
        pathInfo.put(start.getName(), start.getName());
        boolean cal = true;
        while (!reach.isEmpty() || cal) {
            ArrayList<Neighbor> neighbor = null;
            int shstl = MAX_VALUE;
            Node shstn = null;
            String father = "";
            for (Node reached : close.values()) {
                neighbor = reached.getNeighbors();
                if(neighbor.isEmpty()) {
                    continue;
                }
                for (Neighbor nbr : neighbor) {
                    if (open.containsKey(nbr.neighbor.getName())) {// 如果子节点在open中
                        Integer newCompute = path.get(reached.getName())
                                + nbr.distance;
                        if (shstl > newCompute) {// 之前设置的距离大于新计算出来的距离
                            shstl = newCompute;
                            shstn = nbr.neighbor;
                            father = reached.getName();
                        }
                    }
                }
            }
            if(shstn == null) {
                break;
            }
            close.put(shstn.getName(), shstn);
            open.remove(shstn.getName());
            pathInfo.put(shstn.getName(), father);
            path.put(shstn.getName(), shstl);
            Node fa = close.get(father);
            int sum = fa.getSum()+1;
            fa.setSum(sum);
            if (reach.contains(shstn.getName())) {
                reach.remove(shstn.getName());
            }
            //若fa节点所能容纳的goal集合内的孩子已经达到上限，则在close中删除fa以及以它为上一跳的节点，重新放入open中
            if (sum >= MAX_CHILDREN) {
                close.remove(fa.getName());
                if(fa.getName().equals(groupName)) {
                    cal = false;
                }
            }
        }
    }
}
