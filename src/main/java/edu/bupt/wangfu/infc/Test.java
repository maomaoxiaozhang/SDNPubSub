package edu.bupt.wangfu.infc;

import java.util.concurrent.TimeUnit;

/**
 * 单机测试
 */
public class Test {

    public static String sendAddr = "http://127.0.0.1:9018/wsn-send";

    public static String receiveAddr = "http://127.0.0.1:9016/wsn-subscribe";

    public static String wsnAddr = "http://127.0.0.1:9010/wsn-core";

    public static void main(String[] args) throws InterruptedException {
        Init.main(args);
        TimeUnit.SECONDS.sleep(2);
        System.out.println("start");
        Subscribe sub = new Subscribe(receiveAddr, wsnAddr, "sub_id", "test1");

        Publish pub = new Publish(wsnAddr, sendAddr, "pub_id", "test1");
        pub.sendMethod("query");
    }
}
