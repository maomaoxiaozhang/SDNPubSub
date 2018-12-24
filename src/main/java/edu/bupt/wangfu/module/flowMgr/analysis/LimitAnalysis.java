package edu.bupt.wangfu.module.flowMgr.analysis;

import edu.bupt.wangfu.info.device.FlowInfo;
import edu.bupt.wangfu.module.flowMgr.Util.AnalysisResult;

/**
 * Created by lenovo on 2017/5/2.
 * 流量上限分析
 * 比较bytes（传输总流量）和流量上限（bytesLimit）的大小关系，超出时视为超过流量上限，
 * 返回值为overLimit
 */
public class LimitAnalysis {
	public static void limitAnalysis(String portId,long bytesLimite, FlowInfo flowInfo){
		AnalysisResult result = AnalysisResult.normal;
		Long bytes = flowInfo.getBytes();
		if(bytes > bytesLimite){
			result = AnalysisResult.overLimit;
		}
		//FlowCtrl.ctrl(portId,result);
	}
}
