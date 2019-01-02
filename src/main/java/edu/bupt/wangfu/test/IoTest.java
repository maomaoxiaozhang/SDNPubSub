package edu.bupt.wangfu.test;

import java.io.*;

/**
 * 文件持久化测试，将数据写入文件中
 */
public class IoTest {
    //保存在project 工程目录下，与src 同级
    public static final String outputFile = "taskOutputFile_2.out";

    public static void string2file(String str) throws IOException {
        PrintWriter out = null;
        out = new PrintWriter(new BufferedWriter(new FileWriter(outputFile)));
        out.println(str);
        out.close();
    }

    public static void file2file(String fileName) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(fileName));
        PrintWriter out = new PrintWriter(outputFile);
        int lineCount = 1;
        String s;
        while ((s = in.readLine()) != null) {
            out.println(lineCount + ": " + s);
        }
        out.close();
    }

    public static void main(String[] args) throws IOException {
//        string2file("Today is a lucky day.");
//        file2file("taskOutputFile.out");
    }
}
