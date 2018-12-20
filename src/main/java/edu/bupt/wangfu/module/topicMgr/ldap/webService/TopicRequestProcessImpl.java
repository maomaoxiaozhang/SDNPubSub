package edu.bupt.wangfu.module.topicMgr.ldap.webService;

import edu.bupt.wangfu.module.managerMgr.design.PSManagerUI;
import edu.bupt.wangfu.module.topicMgr.ldap.TopicUtil;
import edu.bupt.wangfu.module.topicTreeMgr.topicTree.TopicTreeEntry;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jws.WebService;
import javax.naming.NamingException;
import javax.swing.*;
import java.io.IOException;

/**
 * @see TopicRequestProcessImpl
 */
@WebService(endpointInterface= "edu.bupt.wangfu.module.topicMgr.ldap.webService.TopicRequestProcess",
        serviceName="TopicRequestProcessImpl")
@Component
public class TopicRequestProcessImpl implements TopicRequestProcess {
    @Autowired
    PSManagerUI ui;
    TopicUtil topicUtil;

    @Override
    public String TopicRequestProcess(String request) {
        System.out.println( "收到消息：" + request );
        topicUtil = new TopicUtil();
        TopicTreeEntry topicEdit = new TopicTreeEntry();
        String topic, newTopic, userAddress;
        topic = splitString( request, "<topic>", "</topic>" );
        newTopic = splitString( request, "<newTopic>", "</newTopic>" );
        switch (getType( request )) {
            case "check":
                //查询
                String allTopic = "";
                try {
                    allTopic = topicUtil.readAll();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
                return allTopic;
            case "add":
                //新建
                topicEdit.setPath( topic );
                try {
                    topicUtil.addTopic( topicEdit, newTopic );
                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                JOptionPane.showConfirmDialog(ui.frame, "主题树更改", "主题树",JOptionPane.WARNING_MESSAGE);
                try {
                    ui.topicTreeUI1.reload_LibTrees();
                } catch (NamingException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
                return "add success!";
            case "modify":
                //修改
                topicEdit.setPath( topic );
                try {
                    topicUtil.renameTopic( topicEdit, newTopic );
                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                JOptionPane.showConfirmDialog(ui.frame, "主题树更改", "主题树",JOptionPane.WARNING_MESSAGE);
                try {
                    ui.topicTreeUI1.reload_LibTrees();
                } catch (NamingException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
                return "modify success!";
            case "delete":
                //删除
                topicEdit.setPath( topic );
                try {
                    topicUtil.deleteTopic( topicEdit );
                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                JOptionPane.showConfirmDialog(ui.frame, "主题树更改", "主题树",JOptionPane.WARNING_MESSAGE);
                try {
                    ui.topicTreeUI1.reload_LibTrees();
                } catch (NamingException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
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
}

