package edu.bupt.wangfu.module.flowMgr.database;


import edu.bupt.wangfu.info.device.Controller;
import edu.bupt.wangfu.info.device.FlowInfo;
import edu.bupt.wangfu.info.device.Switch;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @ Created by HanB on 2016/7/12 0012.
 */
public class DataCollect {
//    public static void insertController(Controller controller) {
//        String url = controller.getLocalAddr();
//        //if (selectController(controller) != null) return;
//        String sql = "insert controller (groupName,url) VALUE ('" + controller.getLocalGroupName() +"','"+ url + "')";
//        DBConnector connector = new DBConnector(sql);
//        try {
//            connector.pst.execute(sql);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        finally {
//            connector.close();
//        }
//    }
//
//    public static ResultSet selectController(Controller controller) {
//        String url = controller.getLocalAddr();
//        if (!url.startsWith("http://")) {
//            url = "http://" + url;
//        }
//        String sql = "select * from controller WHERE url = '" + url + "'";
//        DBConnector connector = new DBConnector(sql);
//        ResultSet resultSet = null;
//        try {
//            resultSet = connector.pst.executeQuery(sql);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return resultSet;
//    }
//
//    public static void updateController (Controller controller) {
//        String sql = "update controller set url = '" + controller.getLocalAddr() + "' where groupName = '" + controller.getLocalGroupName() + "'";
//        DBConnector connector = new DBConnector(sql);
//        try {
//            connector.pst.execute(sql);
//        }catch (Exception e ) {
//            e.printStackTrace();
//        }finally {
//            connector.close();
//        }
//    }
//
//    public static void deleteController(Controller controller) {
//        String sql = "delete from controller where groupName = '" + controller.getLocalGroupName() + "'";
//        DBConnector connector = new DBConnector(sql);
//        try {
//            connector.pst.execute(sql);
//        }catch (Exception e) {
//            e.printStackTrace();
//        }finally {
//            connector.close();
//        }
//    }
//
//    public static void insertSwitch(Switch s) {
//        String sql = "insert switch (dpid,controllerUrl) values ('"+ s.getId() + "','"+ s.get + "')";
//        DBConnector connector = new DBConnector(sql);
//        try {
//            connector.pst.execute(sql);
//        }catch (Exception e) {
//            e.printStackTrace();
//        }finally {
//            connector.close();
//        }
//    }
//
//    public static ResultSet selectSwitch (Switch s) {
//        ResultSet rs = null;
//        // 查询交换机
//        return rs;
//    }
//
//    public static void updateSwitch (Switch s) {
//
//    }
//
//    public static void deleteSwitch (Switch s) {
//
//    }
//
//    public static void insertPort(Port p) {
//        String sql = "insert port (portName,tpid,switchId,enport,deport) values ('" + p.getPortName() + "','" + p.getTpid() +
//                "','" + p.getSwitchId() + "','" + p.getEnport() + "','" + p.getDeport() + "')";
//        DBConnector connector = new DBConnector(sql);
//        try {
//            connector.pst.execute(sql);
//        }catch (Exception e) {
//            e.printStackTrace();
//        }finally {
//            connector.close();
//        }
//    }
//
//    public static ResultSet selectPort (Port p) {
//        ResultSet rs = null;
//        // 查询端口
//        return rs;
//    }
//
//    public static void updatePort (Port p) {
//
//    }
//
//    public static void deletePort (Port p) {
//
//    }
//
//    public static void insertHost(WSNHost host) {
//        String sql = "insert host (mac,ip,switchId,portId,lastseen) values ('" + host.getMac() + "','" + host.getIpAddr() + "','"
//                + host.getSwitch_id() + "','" + host.getPort() + "','" + host.getLastSeen() + "')";
//        DBConnector connector = new DBConnector(sql);
//        try {
//            connector.pst.execute(sql);
//        }catch (Exception e) {
//            e.printStackTrace();
//        }finally {
//            connector.close();
//        }
//    }
//
//    public static ResultSet selectHost (WSNHost h) {
//        ResultSet rs = null;
//        // 查询主机
//        return rs;
//    }
//
//    public static void updateHost (WSNHost h) {
//
//    }
//
//    public static void deleteHost (WSNHost h) {
//
//    }
//
//    public static void insertQueue(Queue q) {
//        String sql = "insert queue (queueName,portId,enqueue,dequeue,brandWidth,queueLength) values ('" + q.getQueueName() +
//                "','" + q.getPortId() + "','" + q.getEnqueue() + "','" + q.getDequeue() + "','" + q.getBrandWidth() +
//                "','" + q.getQueueLength() + "')";
//        DBConnector connector = new DBConnector(sql);
//        try {
//            connector.pst.execute(sql);
//        }catch (Exception e ) {
//            e.printStackTrace();
//        }finally {
//            connector.close();
//        }
//    }
//
//    public static ResultSet selectQueue (Queue q) {
//        ResultSet rs = null;
//        // 查询队列
//        return rs;
//    }
//
//    public static void updateQueue (Queue q) {
//
//    }
//
//    public static void deleteQueue (Queue q) {
//
//    }
//
//    public static void insertLink (Link l) {
//        String sql = "insert link (linkId,src,dest) values ('" + l.getLinkId() + "','" + l.getSrc() + "','" + l.getDest() + "')";
//        DBConnector connector = new DBConnector(sql);
//        try {
//            connector.pst.execute(sql);
//        }catch (Exception e ) {
//            e.printStackTrace();
//        }finally {
//            connector.close();
//        }
//    }
//
//    public static ResultSet selectLink (Link l) {
//        ResultSet rs = null;
//        // 查询连接关系
//        return rs;
//    }
//
//    public static void updateLink (Link l) {
//
//    }
//
//    public static void deleteLink (Link l) {
//
//    }

    /*public static void insertFlowInfo (FlowInfo flowInfo) {
        String sql = "insert flowInfo (dpid,flowCount,lastseen) values ('" + flowInfo.getDpid() + "','" + flowInfo.getFlowCount() + "','" + flowInfo.getLastseen() +"')";
        DBConnector connector = new DBConnector(sql);
        try {
            connector.pst.execute(sql);
        }catch (Exception e ) {
            e.printStackTrace();
        }
        // 检查相关交换机记录数
        ResultSet rs = selectFlowInfoByDPID(flowInfo.getDpid());
        try {
            rs.last();
            if (rs.getRow() > 10) {
                rs.first();
                deleteFlowInfo(rs.getInt(1));
            }
        }catch (Exception e) { e.printStackTrace(); }
    }*/

    public static ResultSet selectFlowInfoByDPID(String dpid) {

        String sql = "select * from flowInfo WHERE dpid = '" + dpid + "' order by lastseen ASC";
        DBConnector connector = new DBConnector(sql);
        ResultSet resultSet = null;
        try {
            resultSet = connector.pst.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public static void deleteFlowInfo(int flowInfoId) {
        String sql = "DELETE from flowInfo where flowInfoId = " + flowInfoId;
        DBConnector connector = new DBConnector(sql);
        try {
            connector.pst.execute(sql);
        }catch (Exception e ) {
            e.printStackTrace();
        }finally {
            connector.close();
        }
    }


    public static ResultSet selectMonth() {

        String sql = "select distinct controller, switch,node from  `odl-info`.`day`";
        DBConnector connector = new DBConnector(sql);
        ResultSet resultSet = null;
        try {
            resultSet = connector.pst.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public static void  updateMonth(String controller) {
        DBConnector connector;
        String sql1 = "select distinct controller,switchId,node,days from `odl-info`.`day` where controller = '"+controller+"'";
        String switchId=null; String node=null;
        int received=0,transmitted=0,bytes=0,i;
        float lossRate=0,speed=0;
        ResultSet resultSet1 = null;
        ResultSet resultSet2 = null;
        connector= new DBConnector(sql1);
        try {
            resultSet1 = connector.pst.executeQuery(sql1);
            while(resultSet1.next()){
                switchId = resultSet1.getString("switchId");
                node=resultSet1.getString("node");
                String sql2 = "select * from `odl-info`.day  where controller = '" + controller + "'and switchId = '" +switchId + "' and node ='" + node + "'";
                connector= new DBConnector(sql2);
                resultSet2 = connector.pst.executeQuery(sql2);
                i=0;
                while(resultSet2.next()){
                    speed=speed+resultSet2.getFloat("speed");
                    lossRate=resultSet2.getFloat("lossRate")+lossRate;
                    if((resultSet2.getInt("transmitted")>=transmitted&&(resultSet2.getInt("received")>=received))){
                        transmitted=resultSet2.getInt("transmitted");
                        received=resultSet2.getInt("received");
                        bytes=resultSet2.getInt("bytes");
                    }
                    i++;
                }
                speed=speed/i;
                lossRate=lossRate/i;
                String sql3 = "insert into `odl-info`.month (dates, controller, switchId, node, received, transmitted, bytes, speed, lossRate) values ( '" +resultSet1.getString("days") +"', '"+ controller +"', '"+switchId+"', '"+node+"', '"+received+"', '"+transmitted+"', '"+bytes+"', '"+speed+"', '"+lossRate+"')";
                connector= new DBConnector(sql3);
                connector.pst.execute(sql3);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connector.close();
        }
    }

    public static void  updateYear(String controller,int month) {
        DBConnector connector;
        String sql1 = "select distinct controller,switchId,node from `odl-info`.`month` where controller = '"+controller+"'";
        String switchId=null; String node=null;
        int received=0,transmitted=0,bytes=0,i;
        float lossRate=0,speed=0;
        ResultSet resultSet1 = null;
        ResultSet resultSet2 = null;
        connector= new DBConnector(sql1);
        try {
            resultSet1 = connector.pst.executeQuery(sql1);
            while(resultSet1.next()){
                switchId = resultSet1.getString("switchId");
                node=resultSet1.getString("node");
                String sql2 = "select * from `odl-info`.month  where controller = '" + controller + "'and switchId = '" +switchId + "' and node ='" + node + "'";
                connector= new DBConnector(sql2);
                resultSet2 = connector.pst.executeQuery(sql2);
                i=0;
                while(resultSet2.next()){
                    speed=speed+resultSet2.getFloat("speed");
                    lossRate=resultSet2.getFloat("lossRate")+lossRate;
                    if((resultSet2.getInt("transmitted")>=transmitted&&(resultSet2.getInt("received")>=received))){
                        transmitted=resultSet2.getInt("transmitted");
                        received=resultSet2.getInt("received");
                        bytes=resultSet2.getInt("bytes");
                    }
                    i++;
                }
                speed=speed/i;
                lossRate=lossRate/i;
                String sql3 = "insert into `odl-info`.year (month, controller, switchId, node, received, transmitted, bytes, speed, lossRate) values ( '" +month +"', '"+ controller +"', '"+switchId+"', '"+node+"', '"+received+"', '"+transmitted+"', '"+bytes+"', '"+speed+"', '"+lossRate+"')";
                connector= new DBConnector(sql3);
                connector.pst.execute(sql3);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connector.close();
        }
    }

    public static void  updateSum(String controller,int year) {
        DBConnector connector;
        String sql1 = "select distinct controller,switchId,node from `odl-info`.`year` where controller = '"+controller+"'";
        String switchId=null; String node=null;
        int received=0,transmitted=0,bytes=0,i;
        float lossRate=0,speed=0;
        ResultSet resultSet1 = null;
        ResultSet resultSet2 = null;
        connector= new DBConnector(sql1);
        try {
            resultSet1 = connector.pst.executeQuery(sql1);
            while(resultSet1.next()){
                switchId = resultSet1.getString("switchId");
                node=resultSet1.getString("node");
                String sql2 = "select * from `odl-info`.year  where controller = '" + controller + "'and switchId = '" +switchId + "' and node ='" + node + "'";
                connector= new DBConnector(sql2);
                resultSet2 = connector.pst.executeQuery(sql2);
                i=0;
                while(resultSet2.next()){
                    speed=speed+resultSet2.getFloat("speed");
                    lossRate=resultSet2.getFloat("lossRate")+lossRate;
                    if((resultSet2.getInt("transmitted")>=transmitted&&(resultSet2.getInt("received")>=received))){
                        transmitted=resultSet2.getInt("transmitted");
                        received=resultSet2.getInt("received");
                        bytes=resultSet2.getInt("bytes");
                    }
                    i++;
                }
                speed=speed/i;
                lossRate=lossRate/i;
                String sql3 = "insert into `odl-info`.sum (year, controller, switchId, node, received, transmitted, bytes, speed, lossRate) values ( '" +year +"', '"+ controller +"', '"+switchId+"', '"+node+"', '"+received+"', '"+transmitted+"', '"+bytes+"', '"+speed+"', '"+lossRate+"')";
                connector= new DBConnector(sql3);
                connector.pst.execute(sql3);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connector.close();
        }
    }


    public static void insertDay(FlowInfo flow) {
        String sql = "INSERT INTO `odl-info`.`day` (controller,switch,node,received,transmitted,bytes,speed,lossRate) VALUES( '" + flow.getControllerId() +"','"+flow.getSwitchId()+"','"+flow.getPortId()+"','"+flow.getReceived()+"','"+flow.getTransmitted()+"','"+flow.getBytes()+"','"+flow.getSpeed()+"','"+flow.getLossRate()+"'";
        DBConnector connector = new DBConnector(sql);
        try {
            connector.pst.execute(sql);
        }catch (Exception e ) {
            e.printStackTrace();
        }finally {
            connector.close();
        }
    }

    public static void deleteDay(String  controller) {
        String sql = "DELETE from  `odl-info`.`day` where controller = " + controller;
        DBConnector connector = new DBConnector(sql);
        try {
            connector.pst.execute(sql);
        }catch (Exception e ) {
            e.printStackTrace();
        }finally {
            connector.close();
        }
    }

    public static void deleteMonth(String  controller) {
        String sql = "DELETE from  `odl-info`.`monyh` where controller = " + controller;
        DBConnector connector = new DBConnector(sql);
        try {
            connector.pst.execute(sql);
        }catch (Exception e ) {
            e.printStackTrace();
        }finally {
            connector.close();
        }
    }

    public static void deleteYear(String  controller) {
        String sql = "DELETE from  `odl-info`.`year` where controller = " + controller;
        DBConnector connector = new DBConnector(sql);
        try {
            connector.pst.execute(sql);
        }catch (Exception e ) {
            e.printStackTrace();
        }finally {
            connector.close();
        }
    }

    public static String checkUser(String cUrl, String sId, String portId) {
        String result = "normal";
        String sql = "select * from 'odl-user' WHERE controller = '" + cUrl + "' switch = "+ sId  +"and port='"+portId+"'";
        DBConnector connector = new DBConnector(sql);
        ResultSet resultSet = null;
        try {
            resultSet = connector.pst.executeQuery(sql);
            if(!resultSet.next()) result = "unavailableUser";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void selectByPort (String cUrl, String sId, String portId,ArrayList<String> day ,ArrayList<Double> bytes,ArrayList<Double> avgSpeed,ArrayList<Double> lossRate){
        String port = sId+":"+portId;
        String sql = "SELECT dates, bytes, speed, lossRate FROM `odl-info`.month WHERE controller = '" + cUrl + "'and switchId = '" +sId + "' and node ='" + port + "'ORDER BY dates ASC";
        DBConnector connector = new DBConnector(sql);
        ResultSet resultSet;
        try {
            resultSet = connector.pst.executeQuery(sql);
            while(resultSet.next()){
                day.add(resultSet.getString("dates"));
                bytes.add((double) resultSet.getInt("bytes"));
                avgSpeed.add((double)resultSet.getFloat("speed"));
                lossRate.add((double)resultSet.getFloat("lossRate"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void select (String cUrl, String sId, String portId,ArrayList<String> day ,ArrayList<Double> element,String type,String table){
        String port = sId+":"+portId;
        String sql;
        if(type.equals("流量总数")) type = "bytes";
        else if(type.equals("平均速率")) type = "speed";
        else if(type.equals("丢包率")) type = "lossRate";

        if(table.equals("日表")) table = "`odl-info`.month";
        else if(table.equals("月表")) table = "`odl-info`.year";
        else if(table.equals("日表")) table = "`odl-info`.sum";

        sql  = "SELECT dates,"+type+" FROM"+table+" WHERE controller = '" + cUrl + "'and switchId = '" +sId + "' and node ='" + port + "'ORDER BY dates ASC";
        DBConnector connector = new DBConnector(sql);
        ResultSet resultSet = null;
        try {
            resultSet = connector.pst.executeQuery(sql);
            while(resultSet.next()){
                day.add(resultSet.getString(1));
                if(type.equals("bytes")) element.add((double) resultSet.getInt(2));
                else element.add((double) resultSet.getFloat(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static long allBytes(String cUrl, String sId, String portId){
        long sum = 0;
        String port = sId+":"+portId;
        String sql = "SELECT bytes FROM `odl-info`.day WHERE controller = '" + cUrl + "'and switchId = '" +sId + "' and node ='" + port + "'";
        DBConnector connector = new DBConnector(sql);
        ResultSet resultSet = null;
        try {
            resultSet = connector.pst.executeQuery(sql);
            if(resultSet.next()){
                while(resultSet.next()){
                    if(sum < (long) resultSet.getInt("bytes")){
                        sum = (long) resultSet.getInt("bytes");
                    }
                }
                return sum;
            }

            else{
                String sql1 = "SELECT bytes FROM `odl-info`.month WHERE controller = '" + cUrl + "'and switchId = '" +sId + "' and node ='" + port + "'";
                connector = new DBConnector(sql1);
                ResultSet resultSet1 = connector.pst.executeQuery(sql1);
                while(resultSet1.next()){
                    if(sum < (long) resultSet1.getInt("bytes")){
                        sum = (long) resultSet1.getInt("bytes");
                    }

                }
                return sum;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return sum;
    }


    public static void updateTopo(String table, String action,String des){
        String sql = null;
        if(action.equals("add")){
            if(table.equals("switch")) {
                sql = "INSERT INTO `odl-info`.`switch` (id,ipAddr,hardware,software,manufacturer,controllerAddr) VALUES( '" + des.split(",")[0] + "','" + des.split(",")[1] + "','" + des.split(",")[2] + "','" + des.split(",")[3] + "','" + des.split(",")[4] + "','" + des.split(",")[5] + "'";
            }
            else if(table.equals("port")){
                sql = "INSERT INTO `odl-info`.`port` (pid,switchId,controllerAddr) VALUES( '" + des.split(",")[0] + "','" + des.split(",")[1] + "','" + des.split(",")[2] + "'";
            }
            else if(table.equals("link")){
                sql = "INSERT INTO `odl-info`.`switch` (linkId,des,src,controllerAddr) VALUES( '" + des.split(",")[0] + "','" + des.split(",")[1] + "','" + des.split(",")[2] + "','" + des.split(",")[3]  + "'";
            }
            DBConnector connector = new DBConnector(sql);
            try {
                connector.pst.execute(sql);
            }catch (Exception e ) {
                e.printStackTrace();
            }finally {
                connector.close();
            }
        }
        if(action.equals("delete")){
            if(table.equals("switch")) {
                sql = "DELETE from  `odl-info`.`switch` where id = " + des.split(",")[0]+"controllerAddr ="+des.split(",")[1];
            }
            if(table.equals("port")){
                sql = "DELETE from  `odl-info`.`switch` where pid = " + des.split(",")[0]+"switchId ="+ des.split(",")[1]+"controllerAddr ="+des.split(",")[2];
            }
            if(table.equals("link")){
                sql = "DELETE from  `odl-info`.`switch` where linkId = " + des.split(",")[0]+"controllerAddr ="+des.split(",")[1];
            }

            DBConnector connector = new DBConnector(sql);
            try {
                connector.pst.execute(sql);
            }catch (Exception e ) {
                e.printStackTrace();
            }finally {
                connector.close();
            }
        }
        if(action.equals("update")){
            sql = "update `odl-info`.`switch` set"+ des.split(",")[0].split(":")[0]+"  = '" + des.split(",")[0].split(":")[1] + "' where id = '" +des.split(",")[1]+ "'controllerAddr ='"+des.split(",")[2]+ "'";
            DBConnector connector = new DBConnector(sql);
            try {
                connector.pst.execute(sql);
            }catch (Exception e ) {
                e.printStackTrace();
            }finally {
                connector.close();
            }

        }

    }


    public static void main(String[] args) {

        /*ArrayList<String> day = new ArrayList<>();
        ArrayList<Double> element = new ArrayList<>();
        DataCollect.select("http://10.108.165.188:8181","openflow:143366665789517","2",day,element,"流量总数","日表");
        for(int i = 0 ;i<day.size();i++){
            System.out.println(day.get(i)+"   "+element.get(i));
        }*/

        ArrayList<String> day = new ArrayList<>();
        ArrayList<Double> bytes= new ArrayList<>();
        ArrayList<Double> avgSpeed= new ArrayList<>();
        ArrayList<Double> lossRate = new ArrayList<>();
        DataCollect.selectByPort("http://10.108.165.188:8181","openflow:143366665789517","2",day,bytes,avgSpeed,lossRate);
        for(int i = 0 ;i<day.size();i++){
            System.out.println(day.get(i)+"   "+bytes.get(i)+"   "+avgSpeed.get(i)+"   "+lossRate.get(i));
        }


        //updateMonth("1",1);
        //deleteDay("1");

        //FlowInfo[] flowInfos = new FlowInfo[15];
        //for (int i = 0; i < flowInfos.length; i ++) {
          //  flowInfos[i] = new FlowInfo();
           // flowInfos[i].setDpid("openflow:151943505461327");
            //flowInfos[i].setFlowCount(Integer.toString((i+1) * 452));
            //flowInfos[i].setLastseen(Long.toString(System.currentTimeMillis()));
            //insertFlowInfo(flowInfos[i]);
            //try {
              //  Thread.sleep(5000);
            //}catch ( Exception e) { e.printStackTrace(); }
        //}
        /*Controller ctrl = new Controller("http://10.108.165.188:8181");
        ctrl.setGroupName("G10");
        DataCollect.insertController(ctrl);

        Switch swch = new Switch();
        swch.setController(ctrl.getUrl());
        swch.setDPID("openflow:16359272205130");
        DataCollect.insertSwitch(swch);

        Port port = new Port();
        port.setPortName("tap2");
        port.setTpid(swch.getDPID() + ":3");
        port.setSwitchId(swch.getDPID());
        port.setEnport("4651564");
        port.setDeport("2456185");
        DataCollect.insertPort(port);

        WSNHost host = new WSNHost();
        host.setMac("52:54:00:b4:46:50");
        host.setIpAddr("192.168.100.50");
        host.setSwitch_id(swch.getDPID());
        host.setPort(swch.getDPID() + ":1");
        host.setLastSeen("1467795754278");
        DataCollect.insertHost(host);

        Queue qu = new Queue();
        qu.setQueueName("queue1");
        qu.setPortId(port.getTpid());
        qu.setEnqueue("561564");
        qu.setDequeue("153743");
        qu.setBrandWidth("200000000");
        qu.setQueueLength(Integer.toString(Integer.parseInt(qu.getEnqueue()) - Integer.parseInt(qu.getDequeue())));
        DataCollect.insertQueue(qu);

        Link lk = new Link();
        lk.setLinkId(swch.getDPID() +"/"+ host.getMac());
        lk.setSrc(swch.getDPID());
        lk.setDest(swch.getDPID());
        DataCollect.insertLink(lk);*/
    }
}

