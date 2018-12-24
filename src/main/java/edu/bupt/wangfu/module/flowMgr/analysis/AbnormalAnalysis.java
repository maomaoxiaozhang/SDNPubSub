package edu.bupt.wangfu.module.flowMgr.analysis;


import edu.bupt.wangfu.info.device.FlowInfo;
import edu.bupt.wangfu.module.flowMgr.Util.AnalysisResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2017/5/2.
 * 异常流量分析
 * 判断FlowInfo中当前速率speed和速度阈值threshold的大小，超出视为发生异常，返回值为overThreshold
 * 计算历史数据中speed的平均值、标准差，判断当前速率是否在平均值的两倍方差之内，若不在，视为异常数据点，
 * 若连续出现两个异常数据点，视为异常数据流，返回值为abnormal
 */
public class AbnormalAnalysis {
	public static void abnormalAnalysis(String port, long threshold, FlowInfo flowInfo, Map<Integer,FlowInfo> flowInfos){
		AnalysisResult result = AnalysisResult.normal;
		Long speed =flowInfo.getSpeed();
		List<Long> speedList = new ArrayList<>();
		for(int i : flowInfos.keySet()){
			FlowInfo temp = flowInfos.get(i);
			speedList.add(temp.getSpeed());
		}

		if(speed > threshold){
			result = AnalysisResult.overThreshold;
			//FlowCtrl.ctrl(port,result);
		}

		Long sum = 0L;
		for(Long sp : speedList){
			sum += sp;
		}
		Long avg = sum/speedList.size();
		sum = 0L;
		for(Long sp : speedList){
			sum += (sp - avg)*(sp - avg);
		}
		Long div = sum/speedList.size();
		if(Math.abs(speed - avg) > 2*div){
			result = AnalysisResult.abnormal;
		}

		//FlowCtrl.ctrl(port,result);
	}
}
