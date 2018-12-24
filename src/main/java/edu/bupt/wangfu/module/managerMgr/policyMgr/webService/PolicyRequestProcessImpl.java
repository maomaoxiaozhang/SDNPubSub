package edu.bupt.wangfu.module.managerMgr.policyMgr.webService;

import edu.bupt.wangfu.module.managerMgr.policyMgr.PolicyUtil;
import edu.bupt.wangfu.module.managerMgr.util.Policy;
import org.dom4j.DocumentException;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import javax.jws.WebService;
import javax.naming.NamingException;
import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.ws.Endpoint;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @see edu.bupt.wangfu.module.topicMgr.ldap.webService.TopicRequestProcessImpl
 */
@WebService(endpointInterface= "edu.bupt.wangfu.module.managerMgr.policyMgr.webService.PolicyRequestProcess",
        serviceName="PolicyRequestProcessImpl")
@Component
public class PolicyRequestProcessImpl implements PolicyRequestProcess {
    PolicyUtil util = new PolicyUtil();
    @Override
    public String PolicyRequestProcess(String request) throws IOException, SAXException, ParserConfigurationException {
        System.out.println( "收到消息：" + request );
        String topic,groups;
        List<String> groupList;
        Policy policy;
        topic = splitString( request, "<topic>", "</topic>" );
        groups = splitString( request, "<group>", "</group>" );
        switch (getType( request )) {
            case "check":
//                String allPolicy = util.readAll();
//                return allPolicy;
            case "add":
                groupList = Arrays.asList( groups.split( "," ) );
                policy = new Policy();
                policy.setTargetTopic( topic );
                policy.setTargetGroups( groupList );
                util.addNewPolicy( policy );
                //JOptionPane.showConfirmDialog(ui.frame, "主题树更改", "主题树",JOptionPane.WARNING_MESSAGE);
                return "add success!";
            case "modify":
                //修改
                groupList= Arrays.asList( groups.split( "," ) );
                policy = new Policy();
                policy.setTargetTopic( topic );
                policy.setTargetGroups( groupList );
                util.modifypolicy( policy );
                //JOptionPane.showConfirmDialog(ui.frame, "主题树更改", "主题树",JOptionPane.WARNING_MESSAGE);
                return "modify success!";
            case "delete":
                //删除
                groupList= Arrays.asList( groups.split( "," ) );
                policy = new Policy();
                policy.setTargetTopic( topic );
                policy.setTargetGroups( groupList );
                util.deletePolicy( policy );
                //JOptionPane.showConfirmDialog(ui.frame, "主题树更改", "主题树",JOptionPane.WARNING_MESSAGE);
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
        PolicyRequestProcessImpl policyRequestProcess = new PolicyRequestProcessImpl();
        String localAddr = "http://10.108.166.57:55555/topicMgr";
        Endpoint endpint = Endpoint.publish(localAddr, policyRequestProcess);
    }
}

