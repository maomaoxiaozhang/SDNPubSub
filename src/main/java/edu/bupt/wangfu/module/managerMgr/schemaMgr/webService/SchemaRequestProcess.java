package edu.bupt.wangfu.module.managerMgr.schemaMgr.webService;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 *  处理schema消息服务的接口
 * @author tangl
 *
 */
@WebService(targetNamespace = "http://edu.bupt.wangfu.module.managerMgr.schemaMgr.webService",name = "SchemaRequestProcess")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface SchemaRequestProcess{
    public String SchemaRequestProcess(@WebParam(partName = "Schema", name = "SchemaProcess",
            targetNamespace = "http://edu.bupt.wangfu.module.managerMgr.schemaMgr.user") String request);
}
