package edu.bupt.wangfu;

import edu.bupt.wangfu.config.ControllerConfig;
import edu.bupt.wangfu.module.topicMgr.ldap.LdapUtil;
import edu.bupt.wangfu.module.topicMgr.ldap.TopicEntry;
import edu.bupt.wangfu.module.topicTreeMgr.TopicTreeMgr;
import edu.bupt.wangfu.module.topicTreeMgr.topicTree.TopicTreeEntry;
import edu.bupt.wangfu.module.topologyMgr.TopoMgr;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.naming.NamingException;
import java.net.Inet6Address;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;


public class Start {
    public static void main(String[] args) throws UnknownHostException {

        ApplicationContext context = new AnnotationConfigApplicationContext(ControllerConfig.class);
        TopicTreeMgr topicTreeMgr = (TopicTreeMgr) context.getBean("topicTreeMgr");
        topicTreeMgr.buildTopicTree();
        System.out.println(topicTreeMgr.getEncodeTopicTree());
//        TopoMgr topoMgr = (TopoMgr) context.getBean("topoMgr");
//        topoMgr.start();

//        String str = "1234";
//        System.out.println(str.substring(0, 4));
    }

}
