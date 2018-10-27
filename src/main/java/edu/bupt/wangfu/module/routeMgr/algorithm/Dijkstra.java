package edu.bupt.wangfu.module.routeMgr.algorithm;

import edu.bupt.wangfu.info.device.DevInfo;
import edu.bupt.wangfu.info.device.Switch;
import edu.bupt.wangfu.module.routeMgr.util.BuildTopology;
import edu.bupt.wangfu.module.routeMgr.util.Neighbor;
import edu.bupt.wangfu.module.routeMgr.util.Node;

import java.util.*;

import static java.lang.Integer.MAX_VALUE;

/**
 * 计算单源最短路径
 *
 * @author caoming
 */
public class Dijkstra {
    //未加入节点
    private TreeMap<String, Node> open = new TreeMap<>();
    //已加入节点
    private TreeMap<String, Node> close = new TreeMap<>();
    //想要计算的节点
    private ArrayList<String> reach = new ArrayList<>();
    // 封装路径信息
    private Map<String, String> pathInfo = new HashMap<>();
    private Map<String, Integer> path = new HashMap<>();

    /**
     * dijkstra 通用算法，用于计算单源与所有节点的最短路径
     * 用于生成管理路径
     *
     * @param root
     *         起始节点
     */
    public static Map dijkstra(Node root, Set<Node> nodes) {
        Map<String, List<String>> temp = new HashMap<>();
        Map<String, Integer> length = new HashMap<>();
        Map<String, List<String>> adminPath = new HashMap<>();
        List<String> path = new LinkedList<>();
        path.add(root.getName());
        adminPath.put(root.getName(), path);
        Set<Node> fir = new HashSet<>();
        Set<Node> sec = new HashSet<>();
        fir.add(root);
        sec.addAll(nodes);
        sec.remove(root);
        for (Node node : sec) {
            if (root.getNeighbors().contains(node)) {
                for (int i = 0; i < root.getNeighbors().size(); i++) {
                    if (root.getNeighbors().get(i).getName().equals(node.getName())) {
                        length.put(node.getName(), root.getNeighbors().get(i).distance);
                        break;
                    }
                }
                path = temp.get(root.getName());
                path.add(root.getName());
                temp.put(node.getName(), path);
            }else {
                length.put(node.getName(), Integer.MAX_VALUE);
            }
        }
        while (!sec.isEmpty()) {
            int min = Integer.MAX_VALUE;
            Node loc = null;
            for (Node node : sec) {
                int len = length.get(node.getName());
                if (len < min) {
                    min = len;
                    loc = node;
                }
            }
            root = loc;
            fir.add(root);
            sec.remove(root);
            path = new LinkedList<>(temp.get(root.getName()));
            path.add(root.getName());
            adminPath.put(root.getName(), path);
        }
        return adminPath;
    }

    /**
     * 返回当前发布节点与所有订阅节点的距离之和
     *
     * @param start
     *          发布节点
     * @param subNodes
     *          所有的订阅节点
     * @return
     *          当前发布节点与所有订阅节点的距离之和
     */
    public int dijkstra(Node start, List<Node> subNodes, Set<Node> allNodes) {
        int result = 0;
        path.put(start.getName(), new Integer(0));
        pathInfo.put(start.getName(), start.getName());
        boolean cal = true;
        while (!reach.isEmpty() || cal) {
            List<Neighbor> neighbor = null;
            int shstl = MAX_VALUE;
            Node shstn = null;
            String father = "";
            for (Node reached : close.values()) {
                neighbor = reached.getNeighbors();
                if(neighbor.isEmpty()) {
                    continue;
                }
                for (Neighbor nbr : neighbor) {
                    // 如果子节点在open中
                    if (open.containsKey(nbr.getName())) {
                        Integer newCompute = path.get(reached.getName())
                                + nbr.distance;
                        // 之前设置的距离大于新计算出来的距离
                        if (shstl > newCompute) {
                            shstl = newCompute;
                            shstn = BuildTopology.find(nbr.getName(), allNodes);
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
            if (subNodes.contains(shstn))
                result += shstn.getSum();
            Node fa = close.get(father);
            int sum = fa.getSum()+1;
            fa.setSum(sum);
            if (reach.contains(shstn.getName())) {
                reach.remove(shstn.getName());
            }
        }
        return result;
    }

    public List<String> dijkstra(Node startNode, Node endNode, Set<Node> allNodes){
        Set<Node> op = new HashSet<>();
        op.addAll(allNodes);
        Set<Node> close = new HashSet<>();
        op.remove(startNode);
        close.add(endNode);
        //distance存储当前节点到目标节点的距离
        Map<String, Integer> distance = new HashMap<>();
        //path存储当前节点到达目标节点经过的节点信息
        Map<String, ArrayList<String>> path = new HashMap<>();

        //初始化distance，不相邻则为-1
        for (Node no : op) {
            distance.put(no.getName(), -1);
        }
        //设置path信息
        for (Neighbor ne : startNode.getNeighbors()) {
            if(allNodes.contains(BuildTopology.find(ne.getName(), allNodes))){
                distance.put(ne.getName(), ne.distance);
                path.put(ne.getName(), null);
            }
        }
        Node nearest = startNode;
        while(nearest != endNode){
            nearest = getNearestNode(distance, op);
            op.remove(nearest);
            close.add(nearest);
            //dis_1记录nearestNode到startNode的距离
            int dis_1 = distance.get(nearest.getName());
            //更新distance中信息
            for(Node no : op){
                //dis_2记录当前节点到startNode的距离
                int dis_2 = distance.get(no.getName());
                //dis_3存储当前节点到nearest的距离
                int dis_3 = -1;
                for(Neighbor ne : nearest.getNeighbors()){
                    if(ne.getName().equals(no.getName())){
                        dis_3 = BuildTopology.find(ne.getName(), allNodes).getSum();
                        break;
                    }
                }

                if(dis_3 == -1){
                    //当前节点不相邻，无操作
                }
                else if(dis_2 == -1 || dis_2 > dis_1 + dis_3){
                    //更新之
                    distance.put(no.getName(), dis_1 + dis_3);
                    ArrayList<String> temp_1 = path.get(nearest.getName());
                    ArrayList<String> temp = new ArrayList<>();
                    if (!(temp_1 == null)) {
                        temp.addAll(temp_1);
                    }
                    temp.add(nearest.getName());
                    path.put(no.getName(), temp);
                }
            }
        }
        return path.get(endNode.getName()) == null ? new LinkedList<>() : path.get(endNode.getName());
    }

    //返回最近节点
    public Node getNearestNode(Map<String, Integer> distance, Set<Node> op){
        Node res = null;
        int minDis = Integer.MAX_VALUE;
        for(Node no : op){
            int dis = distance.get(no.getName());
            if(dis == -1){
                //当前节点不相邻，无操作
            }
            else if(dis < minDis){
                minDis = dis;
                res = no;
            }
        }
        return res;
    }


    public static List<String> dijkstra(String startSwtId, String endSwtId, Map<String, Switch> switchMap) {
        if (startSwtId.equals(endSwtId)) {
            List<String> res = new ArrayList<>();
            res.add(startSwtId);
            return res;
        }
        Set<Switch> op = new HashSet<>();
        //将所有switch存储在op集合中
        for (String st : switchMap.keySet()) {
            op.add(switchMap.get(st));
        }
        Switch startSwt = switchMap.get(startSwtId);
        Switch endSwt = switchMap.get(endSwtId);

        Set<Switch> open = new HashSet<>();
        open.addAll(op);
        op.remove(startSwt);
        Set<Switch> close = new HashSet<>();
        close.add(startSwt);
        //dis存储其他节点到startSwit节点的距离
        Map<String, Integer> dis = new HashMap<>();
        //path存储其他节点到startSwit经过的的节点
        Map<String, List<String>> path = new HashMap<>();
        //初始化dis，与startSwit节点不相邻则为-1
        for (Switch sw : open) {
            dis.put(sw.getId(), -1);
        }
        //设置与startSwit节点直接相邻的节点距离
        for (Switch sw : open) {
            for (DevInfo nbr : startSwt.getNeighbors().values()) {
                if (nbr instanceof Switch) {
                    Switch swtNbr = (Switch) nbr;
                    if (swtNbr.getId().equals(sw.getId())) {
//				dis.put(sw.id, startSwt.getNeighbors().get(sw.id).distance);
                        //默认相邻节点间距离为1
                        dis.put(sw.getId(), 1);
                        path.put(sw.getId(), null);
                    }
                }
            }
        }

        Switch nearest;
        while (!op.isEmpty()) {
            //查询距离startSwt最近的节点
            nearest = getNearestSwitch(dis, op);
            close.add(nearest);
            op.remove(nearest);
            //dis_1记录最近节点到startSwt的距离
            int dis_1 = dis.get(nearest.getId());
            //更新dis中的距离信息
            for (Switch sw : op) {
                //dis_2当前节点到startSwt的距离
                int dis_2 = dis.get(sw.getId());
                //dis_3记录当前节点到nearest节点的距离
                int dis_3 = 0;
                Map<String, DevInfo> neighbors = nearest.getNeighbors();
                for (String st : neighbors.keySet()) {
                    DevInfo dev = neighbors.get(st);
                    if (dev instanceof Switch) {
                        Switch swi = (Switch) dev;
                        if (swi.getId().equals(sw.getId())) {
//							dis_3 = neighbors.get(sw.id).distance;
                            //默认相邻交换机距离为1
                            dis_3 = 1;
                            break;
                        } else {
                            dis_3 = -1;
                        }
                    }
                }

                if (dis_3 == -1) {
                    //当前节点没有与nearest节点直接相邻，不操作
                } else if (dis_2 == -1 || dis_2 > dis_1 + dis_3) {
                    //当前节点没有与start节点相邻或者通过nearest节点的距离更短，更新
                    dis.put(sw.getId(), dis_1 + dis_3);
                    //当前节点需通过nearest节点到达目标
                    List<String> temp_1 = path.get(nearest.getId());
                    List<String> temp = new ArrayList<>();
                    if (!(temp_1 == null)) {
                        temp.addAll(temp_1);
                    }
                    temp.add(nearest.getId());
                    path.put(sw.getId(), temp);
                }
            }
        }
        ArrayList<String> across = new ArrayList<>();
        across.add(startSwtId);
        if (!(path.get(endSwtId) == null)) {
            across.addAll(path.get(endSwtId));
        }
        across.add(endSwtId);
        return across;
    }

    //返回dis中距离startSwt交换机最近的节点
    public static Switch getNearestSwitch(Map<String, Integer> dis, Set<Switch> op) {
        Switch res = null;
        int minDis = Integer.MAX_VALUE;
        //返回op集合中最小距离对应的节点
        for (Switch sw : op) {
            int distance = dis.get(sw.getId());
            if (distance == -1) {
                //当前节点并未与startSwt相邻，不操作
            } else if (distance < minDis) {
                minDis = distance;
                res = sw;
            }
        }
        return res;
    }

}
