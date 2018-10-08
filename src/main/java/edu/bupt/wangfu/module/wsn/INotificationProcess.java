package edu.bupt.wangfu.module.wsn;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * <b>function</b>: WSN调用处理通知消息服务的接口
 * @author 柴兆航
 * @version 1.0
 *
 */
@WebService(targetNamespace = "http://module.wsn",name = "INotificationProcess")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface INotificationProcess{
	public void notificationProcess(@WebParam(partName = "Notification", name = "notificationProcess", targetNamespace = "http://module.wsn") String notification);
}