package edu.bupt.wangfu.module.util;

/**
 * 常量
 */
public class Constant {
    //Hello 消息类型
    public static final String HELLO = "hello";

    public static final String RE_HELLO = "re_hello";

    public static final String FINAL_HELLO = "final_hello";

    public static final String HEART = "heart";

    public static final String SUB_PUB_NOTIFY = "sub_pub_notify";

    //Hello 消息角色
    public static final String CONTROLLER = "controller";

    public static final String SYSTEM = "system";

    public static final String ADMIN = "admin";

    public static final String WSN = "wsn";

    //默认邻居集群间距离
    public static final int DISTANCE = 1;

    public static final String LOCAL = "LOCAL";

    //订阅
    public static final String SUBSCRIBE = "subscribe";

    //发布
    public static final String PUBLISH = "publish";

    //注册
    public static final String REGISTER = "register";

    //取消发布
    public static final String CAL_PUBLISH = "cal_publish";

    //取消订阅
    public static final String CAL_SUBSCRIBE = "cal_subscribe";

    //用户配置
    public static final String CONFIG = "config";

    //未识别
    public static final String UNKNOWN = "unknown";

    //二级流表，流表id
    public static final String TABLE_ID = "0";

    //流表项优先级
    public static final String PRIORITY = "50";

    //流表操作 -- 添加
    public static final String ADD = "add";

    //流表操作 -- 删除
    public static final String DELETE = "delete";

    //流表操作 -- 查看
    public static final String DUMP = "dump";

    public static final String SWITCH_ADDRESS = "192.168.10.11";

    public static final String ADD_FLOW = "ovs-ofctl add-flow br0 ";

    public static final String DEL_FLOW = "ovs-ofctl del-flows br0";

    public static final String DUMP_FLOW = "ovs-ofctl dump-flow br0 ";

    public static final String DUMP_QUEUES = "ovs-appctl qos/show ge-1/1/%d";

    public static final String INIT_QUEUES = "ovs-vsctl -- set port ge-1/1/%d qos=@newqos -- --id=@newqos create qos type=PRONTO_STRICT queues=0=@q0,1=@q1,2=@q2 -- --id=@q0 create queue other-config:min-rate=60000000 other-config:max-rate=60000000 -- --id=@q1 create queue other-config:min-rate=30000000 other-config:max-rate=30000000  -- --id=@q2 create queue other-config:min-rate=10000000 other-config:max-rate=10000000";
}
