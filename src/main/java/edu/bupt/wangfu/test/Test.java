package edu.bupt.wangfu.test;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Test {
    public static void main(String[] args) {
        Map<String, Set<String>> map = new HashMap<>();
        Set<String> set = new HashSet<>();
        map.put("aa", set);
        Set<String> other = map.get("aa");
        other.add("lalala");
        System.out.println(map.get("aa"));
    }
}
