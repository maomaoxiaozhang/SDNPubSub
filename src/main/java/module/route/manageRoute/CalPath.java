package module.route.manageRoute;

import info.device.Controller;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.TreeMap;

public class CalPath {
    @Autowired
    private Controller controller;
    private Dijkstra dijkstra;
    private AddFlow add;
    TreeMap<String,TreeMap<String, Integer>> dists = new TreeMap<>();
    public void calculate() {
        for(String name : controller.getLsdb().getLSDB().keySet()){
            TreeMap<String,Integer> values = new TreeMap();
            //for(String group : controller.getLSDB().get(name).getDist2NbrGrps().keySet())
            values.putAll(controller.getLsdb().getLSDB().get(name).getDist2NbrGrps());
            dists.put(name,values);
        }
        dijkstra= new Dijkstra(dists);
        dijkstra.dijkstra(controller.getAdminName());
        add.add(dijkstra.pathInfo);
    }


    public static void main(String[] args) {
        TreeMap<String,TreeMap<String, Integer>> dists = new TreeMap<>();
        TreeMap<String,Integer> values1 = new TreeMap();
        values1.put("A2",6);
        values1.put("A3",3);
        dists.put("A1",values1);
        TreeMap<String,Integer> values2 = new TreeMap();
        values2.put("A1",6);
        values2.put("A3",2);
        values2.put("A4",5);
        dists.put("A2",values2);
        TreeMap<String,Integer> values3 = new TreeMap();
        values3.put("A1",3);
        values3.put("A2",2);
        values3.put("A4",3);
        values3.put("A5",4);
        dists.put("A3",values3);
        TreeMap<String,Integer> values4 = new TreeMap();
        values4.put("A2",5);
        values4.put("A3",3);
        values4.put("A5",5);
        values4.put("A6",3);
        dists.put("A4",values4);
        TreeMap<String,Integer> values5 = new TreeMap();
        values5.put("A3",4);
        values5.put("A4",5);
        values5.put("A6",5);
        dists.put("A5",values5);
        TreeMap<String,Integer> values6 = new TreeMap();
        values6.put("A4",3);
        values6.put("A5",5);
        dists.put("A6",values6);
        Dijkstra dijkstra = new Dijkstra(dists);
        dijkstra.dijkstra("A1");
        for(String group: dijkstra.pathInfo.keySet()){
            System.out.println(group+"  "+dijkstra.pathInfo.get(group));
        }
    }


}
