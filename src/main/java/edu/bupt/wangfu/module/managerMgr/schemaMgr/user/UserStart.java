package edu.bupt.wangfu.module.managerMgr.schemaMgr.user;

public class UserStart {
    private String managerAddr = "http://10.108.166.57:55557/schemaMgr";
    String localAddr = "10.108.166.57";
    public void start() {
        SendSchemaCommand send= new SendSchemaCommand( managerAddr );
//        send.check( "test",localAddr );
        send.add( "test","xs:element[@name=\"test\"]/xs:complexType/xs:sequence","xs:element","test","xs:string",localAddr );
    }

    public static void main(String[] args) {
        UserStart userStart = new UserStart();
        userStart.start();
    }
}
