package edu.bupt.wangfu.module.managerMgr.schemaMgr.webService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.ws.Endpoint;

@Component
public class SchemaWSMgr {
    @Autowired
    SchemaRequestProcessImpl schemaRequestProcess;
    private String localAddr = "http://localhost:55557/schemaMgr";
    public void start() {
        //开启发布订阅注册服务
        Endpoint endpint = Endpoint.publish(localAddr, schemaRequestProcess);
    }
}
