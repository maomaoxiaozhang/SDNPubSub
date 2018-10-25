package edu.bupt.wangfu.module.topicTreeMgr.util;

import edu.bupt.wangfu.module.topicTreeMgr.topicTree.EncodeTopicTreeEntry;
import edu.bupt.wangfu.module.topicTreeMgr.topicTree.EncodeTopicTree;
import edu.bupt.wangfu.module.topicTreeMgr.topicTree.TopicTreeEntry;

import java.util.LinkedList;
import java.util.List;

/**
 * 将主题进行编码，并将编码转换为对应的ipv6地址
 *
 * ipv6地址：固定前缀（8bit）+ 标记位（4bit）+ 作用域（4bit）+ 类型（2bit）+ 编码长度（7bit）+ 编码（103bit）
 * 基本格式为：ff0e:0190:*
 */
public class EncodeUtil {
    /**
     * 在当前节点的编码基础上，遍历子节点，依次添加编码为0 —— 01 —— 011 —— 0111 等
     * 预留100bit编码，不足补1
     * @param root
     *          当前节点
     * @param encodeRoot
     *          编码后的节点
     */
    public static List<EncodeTopicTreeEntry> encode(TopicTreeEntry root, EncodeTopicTreeEntry encodeRoot) {
        List<EncodeTopicTreeEntry> result = new LinkedList<>();
        result.add(encodeRoot);
        LinkedList<TopicTreeEntry> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            root = queue.poll();
            List<TopicTreeEntry> nodes = root.getChildList();
            List<EncodeTopicTreeEntry> encodeNodes = new LinkedList<>();
            String prefix = encodeRoot.getEncode();
            for (int i = 0; i < nodes.size(); i++) {
                queue.offer(nodes.get(i));
                EncodeTopicTreeEntry entry = new EncodeTopicTreeEntry();
                entry.setTopic(nodes.get(i).getTopic());
                String encode = prefix + "0" + convert(i);
                entry.setEncode(encode);
                entry.setAddress(binary2v6(getAddress(encode)));
                entry.setEncodeParent(encodeRoot);
                encodeNodes.add(entry);
            }
            encodeRoot.setEncodeChildList(encodeNodes);
            result.addAll(encodeNodes);
        }
        return result;
    }

    /**
     * 将输入参数转换为对应的编码格式
     * @param i
     * @return
     */
    public static String convert(int i) {
        StringBuffer sb = new StringBuffer();
        while (i > 0) {
            sb.append("1");
            i--;
        }
        return sb.toString();
    }

    /**
     * 层次遍历编码主题树，当主题相同时停止，返回对应的编码主题节点
     *
     * @param topicName
     * @return
     */
    public static EncodeTopicTreeEntry getEncodeEntry(String topicName, EncodeTopicTree encodeTopicTree) {
        List<EncodeTopicTreeEntry> queue = new LinkedList<>();
        EncodeTopicTreeEntry root = encodeTopicTree.getRoot();
        queue.add(root);
        while (!queue.isEmpty()) {
            root = queue.get(0);
            if (root.getTopic().equals(topicName))
                return root;
            queue.remove(0);
            for (EncodeTopicTreeEntry entry : root.getEncodeChildList())
                queue.add(entry);
        }
        return null;
    }

    /**
     * 将主题编码转换为对应的ipv6地址
     * @param encode
     * @return
     */
    public static String getAddress(String encode) {
        String address = "";
        String prefix = "11111111";
        String flags = "0000";
        String scope = "1110";
        String type = "00";
        String length = Integer.toBinaryString(encode.length());
        String rest = getRest(length);
        String suffix = getSuffix(encode);
        address = prefix + flags + scope + type + rest + length + encode + suffix;
        return address;
    }

    public static String getRest(String length) {
        StringBuffer sb = new StringBuffer();
        for (int i = 7; i > length.length(); i--) {
            sb.append("0");
        }
        return sb.toString();
    }

    public static String getSuffix(String encode) {
        StringBuffer sb = new StringBuffer();
        for (int i = 103; i > encode.length(); i--) {
            sb.append("0");
        }
        return sb.toString();
    }

    /**
     * 二进制地址转换为v6地址
     * @param address
     * @return
     */
    public static String binary2v6(String address) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < address.length(); i+=4) {
            if (i%16 == 0 && i != 0) {
                result.append(":");
            }
            String subString = address.substring(i, i+4);
            result.append(parseByte2HexStr(subString));
        }
        return result.toString();
    }

    //将二进制转为16进制
    public static String parseByte2HexStr(String str) {
        StringBuffer sb = new StringBuffer();
        switch (str) {
            case "0000":
                return "0";
            case "0001":
                return "1";
            case "0010":
                return "2";
            case "0011":
                return "3";
            case "0100":
                return "4";
            case "0101":
                return "5";
            case "0110":
                return "6";
            case "0111":
                return "7";
            case "1000":
                return "8";
            case "1001":
                return "9";
            case "1010":
                return "A";
            case "1011":
                return "B";
            case "1100":
                return "C";
            case "1101":
                return "D";
            case "1110":
                return "E";
            case "1111":
                return "F";
            default:
                return "X";
        }
    }

}
