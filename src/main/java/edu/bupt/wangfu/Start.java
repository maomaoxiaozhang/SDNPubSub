package edu.bupt.wangfu;

import edu.bupt.wangfu.config.ControllerConfig;
import edu.bupt.wangfu.info.device.Controller;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.net.UnknownHostException;



public class Start {
    public static void main(String[] args) throws UnknownHostException {

        ApplicationContext context = new AnnotationConfigApplicationContext(ControllerConfig.class);
//        TopicTreeMgr topicTreeMgr = (TopicTreeMgr) context.getBean("topicTreeMgr");
//        topicTreeMgr.buildTopicTree();
//        System.out.println(topicTreeMgr.getEncodeTopicTree());
//        TopoMgr topoMgr = (TopoMgr) context.getBean("topoMgr");
//        topoMgr.start();
        Controller controller = (Controller) context.getBean("controller");
        System.out.println(controller.getLocalGroupName());

//        String str = "1234";
//        System.out.println(str.substring(0, 4));
    }

}
