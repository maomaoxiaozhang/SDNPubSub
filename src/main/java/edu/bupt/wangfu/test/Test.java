package edu.bupt.wangfu.test;

public class Test {
    public static void main(String[] args) throws NoSuchMethodException {
        Test test = new Test();
        test.help();
    }

    public void help() {
        try {
            while (true) {
                System.out.println("1");
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
