package edu.bupt.wangfu.module.topicMgr.ldap.webService;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 *  处理主题消息服务的接口
 * @author tangl
 *
 */
@WebService(targetNamespace = "http://edu.bupt.wangfu.module.topicMgr.ldap",name = "TopicRequestProcess")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface TopicRequestProcess{
    public String TopicRequestProcess(@WebParam(partName = "Topic", name = "TopicProcess",
            targetNamespace = "http://edu.bupt.wangfu.module.topicMgr.ldap.user") String request);
}