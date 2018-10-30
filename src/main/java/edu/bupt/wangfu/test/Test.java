package edu.bupt.wangfu.test;

import edu.bupt.wangfu.config.ControllerConfig;
import edu.bupt.wangfu.info.device.Controller;
import edu.bupt.wangfu.info.device.Manager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Test {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ControllerConfig.class);
        Controller controller = (Controller) context.getBean("controller");
        System.out.println(controller.getAdminName());
    }
}
