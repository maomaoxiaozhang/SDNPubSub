package edu.bupt.wangfu;

import edu.bupt.wangfu.config.ControllerConfig;
import edu.bupt.wangfu.info.device.Controller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class Start {

    public static int count1 = 20;
    public static int count2 = 0;

    private static Start start = new Start();

    public Start() {
        count1++;
        count2++;
    }

    public static Start getInstance() {
        return start;
    }

    public static void main(String[] args) {
        Start start = getInstance();
        System.out.println(start.count1);
        System.out.println(start.count2);
    }
}
