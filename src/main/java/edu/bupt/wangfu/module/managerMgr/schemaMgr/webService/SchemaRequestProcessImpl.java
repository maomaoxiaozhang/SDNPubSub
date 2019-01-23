package edu.bupt.wangfu.module.managerMgr.schemaMgr.webService;

import edu.bupt.wangfu.module.managerMgr.design.PSManagerUI;
import edu.bupt.wangfu.module.managerMgr.schemaMgr.XSDNode;
import edu.bupt.wangfu.module.managerMgr.schemaMgr.XSDUtil;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jws.WebService;
import javax.swing.*;
import javax.xml.ws.Endpoint;
import java.io.IOException;

/**
 * @see SchemaRequestProcessImpl
 */
@WebService(endpointInterface= "edu.bupt.wangfu.module.managerMgr.schemaMgr.webService.SchemaRequestProcess",
        serviceName="PolicyRequestProcessImpl")
@Component
public class SchemaRequestProcessImpl implements SchemaRequestProcess {
    @Autowired
    PSManagerUI ui;
    XSDUtil util = new XSDUtil();
    @Override
    public String SchemaRequestProcess(String request) {
        System.out.println( "收到消息：" + request );
        String topic,type,name,valueType,path;
        XSDNode node = new XSDNode();
        topic = splitString( request, "<topic>", "</topic>" );
        path = splitString( request, "<path>", "</path>" );
        type = splitString( request, "<type>", "</type>" );
        name = splitString( request, "<name>", "</name>" );
        valueType = splitString( request, "<valueType>", "</valueType>" );
        String file = "./schema/"+topic+".xsd";
        node.setPath( path );
        switch (getType( request )) {
            case "check":
                String schema = "";
                try {
                    schema = util.readAll(file);
                } catch (DocumentException e) {
                    e.printStackTrace();
                }

                return schema;
            case "add":
                XSDNode newNode = new XSDNode();
                newNode.setTagType( type );
                newNode.setName( name );
                newNode.setType( valueType );
                try {
                    util.addTag( file, node,newNode);
                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                JOptionPane.showConfirmDialog(ui.frame, "新增schema信息，主题："+topic, "schema",JOptionPane.WARNING_MESSAGE);
                try {
                    ui.schemaUI.reload_Schema( file );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return "add success!";
            case "modify":
                //修改
                if(name!= ""){
                    try {
                        util.modify( file,node,"name",name );
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(valueType != ""){
                    try {
                        util.modify( file,node,"type",valueType );
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                JOptionPane.showConfirmDialog(ui.frame, "schema信息修改，主题："+topic, "schema",JOptionPane.WARNING_MESSAGE);
                try {
                    ui.schemaUI.reload_Schema( file );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return "modify success!";
            case "delete":
                //删除
                try {
                    util.deleteTag( file,node );
                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                JOptionPane.showConfirmDialog(ui.frame, "删除schema信息，主题："+topic, "schema",JOptionPane.WARNING_MESSAGE);
                try {
                    ui.schemaUI.reload_Schema( file );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return "delete success!";
            default:
                System.out.println( "未识别消息类别！" );
                return "error message";
        }
    }

    public String splitString(String string, String start, String end) {
        int from = string.indexOf( start ) + start.length();
        int to = string.indexOf( end );
        return string.substring( from, to );
    }

    public String getType(String str) {
        if (str.startsWith("<wsnt:check")) {
            return "check";
        }else if ((str.startsWith("<wsnt:add"))) {
            return "add";
        }else if ((str.startsWith("<wsnt:delete"))) {
            return "delete";
        }
        else if ((str.startsWith("<wsnt:modify"))) {
            return "modify";
        }
        return "unknown";
    }

    public static void main(String[] args) {
        SchemaRequestProcessImpl schemaRequestProcess = new SchemaRequestProcessImpl();
        String localAddr = "http://10.108.166.57:55557/schemaMgr";
        Endpoint endpint = Endpoint.publish(localAddr, schemaRequestProcess);
    }
}
