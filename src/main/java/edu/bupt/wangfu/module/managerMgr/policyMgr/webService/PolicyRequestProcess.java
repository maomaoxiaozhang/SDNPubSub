package edu.bupt.wangfu.module.managerMgr.policyMgr.webService;

import org.xml.sax.SAXException;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 *  处理主题消息服务的接口
 * @author tangl
 *
 */
@WebService(targetNamespace = "http://edu.bupt.wangfu.module.managerMgr.policyMgr.webService",name = "PolicyRequestProcess")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface PolicyRequestProcess{
    public String PolicyRequestProcess(@WebParam(partName = "Policy", name = "PolicyProcess",
            targetNamespace = "http://edu.bupt.wangfu.module.managerMgr.policyMgr.user") String request) throws IOException, SAXException, ParserConfigurationException;
}
