package module.route;

import lombok.Data;

import java.util.ArrayList;

/**
 * 将网络拓扑中的集群抽象为节点
 *
 * @author caoming
 */
@Data
public class Node {
    private String name;
    private ArrayList<Neighbor> neighbors;
    private int sum;
}
