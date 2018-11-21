package edu.bupt.wangfu.test;

<<<<<<< HEAD
import edu.bupt.wangfu.module.managerMgr.design.PSManagerUI;
import edu.bupt.wangfu.module.managerMgr.util.AllGroups;
import org.springframework.beans.factory.annotation.Autowired;
=======
import edu.bupt.wangfu.module.wsnMgr.util.soap.SendWSNCommand;
import edu.bupt.wangfu.module.wsnMgr.util.soap.wsn.WsnNotificationProcessImpl;

import javax.xml.ws.Endpoint;
>>>>>>> 3d85c23b45c035e776e0988d40984bf3ca0096d2

public class Test {
    private static final String receiveAddr = "http://10.108.166.14:9002/wsn-subscribe";
    private static final String sendAddr = "http://10.108.166.14:9004/wsn-core-subscriber";
    private static final String wsnAddr = "http://10.108.166.14:9010";

<<<<<<< HEAD
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
=======
    public static void main(String[] args) {
        Endpoint endpoint = Endpoint.publish(wsnAddr, new WsnNotificationProcessImpl());
        SendWSNCommand sendWSNCommand = new SendWSNCommand(receiveAddr, wsnAddr);
        sendWSNCommand.subscribe("1", "all", receiveAddr);
    }
>>>>>>> 3d85c23b45c035e776e0988d40984bf3ca0096d2
}
