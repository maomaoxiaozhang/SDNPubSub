package edu.bupt.wangfu.module.queueMgr.util;

import edu.bupt.wangfu.info.device.Queue;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 工具类，提供获取端口队列运行情况方法
 */
public class GetInfo {

    //获取端口对应的队列情况，每个端口包含三个队列
    public static Map<Integer, List<Queue>> getQueueInfo(String str, int port) {
        List<Queue> queueList = new LinkedList<>();
        Map<Integer, List<Queue>> queueMap = new HashMap<>();
        String[] strings = str.split("burst");
        for (int i = 1; i < strings.length; i++) {
            Queue queue = new Queue();
            String[] temp = strings[i].split("\n\t");
            long max_rate = Long.parseLong(temp[1].split(": ")[1]);
            long min_rate = Long.parseLong(temp[2].split(": ")[1]);
            long packets = Long.parseLong(temp[4].split(": ")[1]);
            long bytes = Long.parseLong(temp[5].split(": ")[1]);
            long errors = Long.parseLong(temp[6].split("\n")[0].split(": ")[1]);
//            System.out.println(i + "\t" +  max_rate + "\t" + min_rate + "\t" + packets + "\t" + bytes + "\t" + errors);
            queue.setId(i-1);
            queue.setPort(port);
            queue.setPackets(packets);
            queue.setBytes(bytes);
            queue.setErrors(errors);
            queue.setBrandWidth(max_rate);
            queueList.add(queue);
        }
        queueMap.put(port, queueList);
        return queueMap;
    }
}
