package edu.bupt.wangfu.test;

import edu.bupt.wangfu.module.wsnMgr.util.soap.SendWSNCommand;
import edu.bupt.wangfu.module.wsnMgr.util.soap.wsn.WsnNotificationProcessImpl;

import javax.xml.ws.Endpoint;

public class Test {
    private static final String receiveAddr = "http://10.108.166.14:9002/wsn-subscribe";
    private static final String sendAddr = "http://10.108.166.14:9004/wsn-core-subscriber";
    private static final String wsnAddr = "http://10.108.166.14:9010";

    public static void main(String[] args) {
        Endpoint endpoint = Endpoint.publish(wsnAddr, new WsnNotificationProcessImpl());
        SendWSNCommand sendWSNCommand = new SendWSNCommand(receiveAddr, wsnAddr);
        sendWSNCommand.subscribe("1", "all", receiveAddr);
    }
}
