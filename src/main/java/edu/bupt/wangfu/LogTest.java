package edu.bupt.wangfu;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 日志测试代码，分级为：trace <debug < info < warn < error，在log文件夹下生成对应日志文件
 * 配置文件为log4j2.xml
 */

public class LogTest {
    //邮件log入口，可以指定邮箱发送错误日志
    private static Logger mailLog = LogManager.getLogger("mail");

    //普通日志入口
    private static Logger logger = LogManager.getLogger(Start.class);

    public static void main(String[] args) {
        mailLog.error("Hello~");
        logger.error("test");
    }
}
