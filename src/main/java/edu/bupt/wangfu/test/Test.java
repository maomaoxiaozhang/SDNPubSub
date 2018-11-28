package edu.bupt.wangfu.test;

import edu.bupt.wangfu.module.managerMgr.design.PSManagerUI;
import edu.bupt.wangfu.module.managerMgr.util.AllGroups;
import org.springframework.beans.factory.annotation.Autowired;

public class Test {
    private static final String receiveAddr = "http://10.108.166.14:9002/wsn-subscribe";
    private static final String sendAddr = "http://10.108.166.14:9004/wsn-core-subscriber";
    private static final String wsnAddr = "http://10.108.166.14:9010";

    @Autowired
    AllGroups allGroups;

    @Autowired
    PSManagerUI ui;

    public void start() {
        try {
            ui.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
       Test test = new Test();
       test.start();
    }
}
