package edu.bupt.wangfu.test;

import java.lang.reflect.Field;

public class Test {
    public static void main(String[] args) throws ClassNotFoundException {
        System.out.println(Parent.class.getClassLoader());
    }
}

class Parent {
    public Parent() {
        System.out.println("parent");
    }
}

class Child extends Parent {
    public Child() {
        System.out.println("child");
    }
}
