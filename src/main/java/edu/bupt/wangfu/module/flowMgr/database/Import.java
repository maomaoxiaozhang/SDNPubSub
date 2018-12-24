package edu.bupt.wangfu.module.flowMgr.database;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Tang Lu on 2017/3/14.
 */
public class Import
{
//    public void collect(FlowInfos flowInfos){
//        Map<String, FlowInfo> flowInfo = new ConcurrentHashMap<>();
//        Map<String, Integer> num = new ConcurrentHashMap<>();
//        DataCollect dc = new DataCollect();
//        if(flowInfos.isNewDay()){
//            dc.updateMonth(flowInfos.getControllerId());
//            if(flowInfos.isNewMonth()){
//                if(flowInfos.getMonth() ==1){
//                    dc.updateYear(flowInfos.getControllerId(),12);
//                }
//                else{
//                    dc.updateYear(flowInfos.getControllerId(),flowInfos.getMonth()-1);
//                }
//                if(flowInfos.isNewYear()){
//                    dc.updateSum(flowInfos.getControllerId(),flowInfos.getYear()-1);
//                }
//            }
//        }
//        dc.deleteDay(flowInfos.getControllerId());
//        dc.deleteMonth(flowInfos.getControllerId());
//        dc.deleteYear(flowInfos.getControllerId());
//
//        for(ArrayList<FlowInfo> flows : flowInfos.getFlowMap().values()){
//            for(FlowInfo flow : flows){
//                String port = flow.getPortId();
//                if((flowInfo.get(port) == null)||(num.get(port) == null)){
//                    flowInfo.put(flow.getPortId(),flow);
//                    num.put(flow.getPortId(),0);
//                }
//                else{
//                    num.put(flow.getPortId(),num.get(port)+1);
//                    long speed = flowInfo.get(port).getSpeed();
//                    float lossRate = flowInfo.get(port).getLossRate();
//                    flowInfo.get(port).setSpeed(speed+flow.getSpeed());
//                    flowInfo.get(port).setLossRate(flow.getLossRate()+lossRate);
//                    if((flow.getTransmitted()>=flowInfo.get(port).getTransmitted()&&(flow.getReceived()>=flowInfo.get(port).getReceived()))){
//                        flowInfo.get(port).setTransmitted(flow.getTransmitted());
//                        flowInfo.get(port).setReceived(flow.getReceived());
//                        flowInfo.get(port).setBytes(flow.getBytes());
//                    }
//                }
//            }
//        }
//        for(FlowInfo flow: flowInfo.values()){
//            flow.setSpeed(flow.getSpeed()/num.get(flow.getPortId()));
//            flow.setLossRate(flow.getLossRate()/num.get(flow.getPortId()));
//            dc.insertDay(flow);
//        }
//    }
}
