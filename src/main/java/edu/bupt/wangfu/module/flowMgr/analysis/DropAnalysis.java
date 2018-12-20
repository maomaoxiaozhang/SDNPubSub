package edu.bupt.wangfu.module.flowMgr.analysis;


import edu.bupt.wangfu.info.device.FlowInfo;
import edu.bupt.wangfu.module.flowMgr.Util.AnalysisResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2017/5/2.
 * 丢包率分析
 * 传入参数为历史流量信息FlowInfos和当前需要分析的流量信息FlowInfo
 * 计算当前对象每次收集的历史丢包率，若当前丢包率大于历史丢包率的2%，视为丢包率过大
 * 若累计有5%的丢包率过大，判断发生了丢包，进行相关的管控
 */
public class DropAnalysis {
	public static void dropAnalysis(String port, float rate, FlowInfo flowInfo, Map<Integer,FlowInfo> flowInfos){
		AnalysisResult result = AnalysisResult.normal;

		float lossRate = flowInfo.getLossRate();
		List<Float> lossRateList = new ArrayList<>();
		for(int i : flowInfos.keySet()){
			FlowInfo bf = flowInfos.get(i);
			FlowInfo af = flowInfos.get(i+1);
			if(bf != null && af != null){
				float loss = (af.getDrop() - bf.getDrop())/(af.getPackets() - bf.getPackets());
				lossRateList.add(loss);
			}
		}
		int num = 0;
		for(int i = 0; i < lossRateList.size(); i++){
			if((lossRate - lossRateList.get(i)) > rate*lossRateList.get(i)){
				num++;
			}
		}
		if(num > 0.05*lossRateList.size()){
			//FlowCtrl.ctrl(port,result);
		}
	}
}
