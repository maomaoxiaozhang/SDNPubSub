package edu.bupt.wangfu;

import edu.bupt.wangfu.config.ControllerConfig;
import edu.bupt.wangfu.info.device.Controller;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.net.UnknownHostException;



public class Start {
    public static void main(String[] args) throws NoSuchMethodException {
        Start start = new Start();
        start.help();
    }

    public void help() {
        try {
            while (true) {
                System.out.println("1");
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
