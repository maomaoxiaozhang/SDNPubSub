package edu.bupt.wangfu.module.managerMgr.policyMgr.user;

public class UserStart {
    private String managerAddr = "http://10.108.166.57:55556/policyMgr";
    String localAddr = "10.108.166.57";
    public void start() {
        SendPolicyCommand send= new SendPolicyCommand( managerAddr );
        System.out.print(  send.check( " "," ",localAddr ));
    }

    public static void main(String[] args) {
        UserStart userStart = new UserStart();
        userStart.start();
    }
}
