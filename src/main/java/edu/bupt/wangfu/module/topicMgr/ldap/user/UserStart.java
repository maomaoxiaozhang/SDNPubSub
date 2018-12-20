package edu.bupt.wangfu.module.topicMgr.ldap.user;

public class UserStart {

    private String managerAddr = "http://10.108.166.57:55555/topicMgr";
    String localAddr = "10.108.166.57";
       public void start() {
        SendTopicCommand send= new SendTopicCommand( managerAddr );
        //send.check( " "," ",localAddr );
        send.add( "/all/layer-1[@name=\"test1\"]/layer-2[@name=\"test1-1\"]/layer-3[@name=\"test1-1-1\"]","newTopic",localAddr );
    }

    public static void main(String[] args) {
        UserStart userStart = new UserStart();
        userStart.start();
    }
}
