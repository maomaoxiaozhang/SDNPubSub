package edu.bupt.wangfu.module.managerMgr.policyMgr.webService;

import edu.bupt.wangfu.module.managerMgr.design.PSManagerUI;
import edu.bupt.wangfu.module.managerMgr.policyMgr.PolicyUtil;
import edu.bupt.wangfu.module.managerMgr.util.Policy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jws.WebService;
import javax.swing.*;
import javax.xml.ws.Endpoint;
import java.util.Arrays;
import java.util.List;

/**
 * @see edu.bupt.wangfu.module.managerMgr.policyMgr.webService.PolicyRequestProcessImpl
 */
@WebService(endpointInterface= "edu.bupt.wangfu.module.managerMgr.policyMgr.webService.PolicyRequestProcess",
        serviceName="PolicyRequestProcessImpl")
@Component
public class PolicyRequestProcessImpl implements PolicyRequestProcess {
    @Autowired
    PSManagerUI ui;
    PolicyUtil util = new PolicyUtil();
    @Override
    public String PolicyRequestProcess(String request) {
        System.out.println( "收到消息：" + request );
        String topic,groups;
        List<String> groupList;
        Policy policy;
        topic = splitString( request, "<topic>", "</topic>" );
        groups = splitString( request, "<group>", "</group>" );
        switch (getType( request )) {
            case "check":
                String allPolicy = util.readAll();
                return allPolicy;
            case "add":
                groupList = Arrays.asList( groups.split( "," ) );
                policy = new Policy();
                policy.setTargetTopic( topic );
                policy.setTargetGroups( groupList );
                util.addNewPolicy( policy );
                JOptionPane.showConfirmDialog(ui.frame, "新增策略信息，主题树："+topic, "策略",JOptionPane.WARNING_MESSAGE);
                ui.reflashCurrentPolicy();

                return "add success!";
            case "modify":
                //修改
                groupList= Arrays.asList( groups.split( "," ) );
                policy = new Policy();
                policy.setTargetTopic( topic );
                policy.setTargetGroups( groupList );
                util.modifypolicy( policy );
                JOptionPane.showConfirmDialog(ui.frame, "策略信息修改，主题树："+topic, "策略",JOptionPane.WARNING_MESSAGE);
                ui.reflashCurrentPolicy();
                return "modify success!";
            case "delete":
                //删除
                groupList= Arrays.asList( groups.split( "," ) );
                policy = new Policy();
                policy.setTargetTopic( topic );
                policy.setTargetGroups( groupList );
                util.deletePolicy( policy );
                JOptionPane.showConfirmDialog(ui.frame, "删除策略信息，主题树："+topic, "策略",JOptionPane.WARNING_MESSAGE);
                ui.reflashCurrentPolicy();
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
        String localAddr = "http://10.108.166.57:55556/policyMgr";
        Endpoint endpint = Endpoint.publish(localAddr, policyRequestProcess);
    }
}

