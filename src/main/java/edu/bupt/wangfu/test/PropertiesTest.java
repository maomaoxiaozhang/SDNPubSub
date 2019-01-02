package edu.bupt.wangfu.test;

import edu.bupt.wangfu.module.util.Constant;

import java.io.*;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Properties;

/**
 * 配置化测试，将常量转换为可配置信息
 */
public class PropertiesTest {

    public static final String file = "src/main/resources/constant.properties";

    //更新constant 类中的属性值
    public static void refreshPro() {
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(file));
            Properties pro = new Properties();
            pro.load(in);
            Class<?> klass = Constant.class;
            Constant constant = new Constant();
            for (Map.Entry<Object, Object> entry : pro.entrySet()) {
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();
                System.out.println("key: " + key + "\tvalue: " + value);
                Field field = klass.getField(key);
                if (field != null) {
                    field.set(constant, value);
                }
            }
        } catch (IllegalAccessException e) {
            System.out.println("private 访问权限！");
        } catch (FileNotFoundException e) {
            System.out.println("properties 文件未找到！");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            System.out.println("无该项属性值！");
        }
    }

    public static void main(String[] args) {
        refreshPro();
    }
}
