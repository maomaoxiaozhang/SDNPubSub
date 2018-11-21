package edu.bupt.wangfu.test;

import edu.bupt.wangfu.module.managerMgr.design.PSManagerUI;
import edu.bupt.wangfu.module.managerMgr.util.AllGroups;
import org.springframework.beans.factory.annotation.Autowired;

public class Test {

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
