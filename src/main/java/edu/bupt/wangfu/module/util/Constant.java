package edu.bupt.wangfu.module.util;

/**
 * 申明全局常量
 */
public class Constant {
    //主题树存储路径
    public static String TOPIC = "./topicMsg.xml";

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
    public static String DISTANCE = "1";

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

    //未识别
    public static final String UNKNOWN = "unknown";

    //二级流表，流表id
    public static String TABLE_ID = "0";

    //流表项优先级
    public static String PRIORITY = "50";

    //流表操作 -- 添加
    public static final String ADD = "add";

    //流表操作 -- 删除
    public static final String DELETE = "delete";

    //流表操作 -- 查看
    public static final String DUMP = "dump";

    public static String SWITCH_ADDRESS = "192.168.100.10";

    public static String ADD_FLOW = "ovs-ofctl add-flow br0 ";

    public static String DEL_FLOW = "ovs-ofctl del-flows br0 ";

    public static String DUMP_FLOW = "ovs-ofctl dump-flow br0 ";

    public static String DUMP_QUEUES = "ovs-appctl qos/show ge-1/1/%d";

    public static String INIT_QUEUES = "ovs-vsctl -- set port ge-1/1/%d qos=@newqos -- --id=@newqos create qos type=PRONTO_STRICT queues=0=@q0,1=@q1,2=@q2 -- --id=@q0 create queue other-config:min-rate=60000000 other-config:max-rate=60000000 -- --id=@q1 create queue other-config:min-rate=30000000 other-config:max-rate=30000000  -- --id=@q2 create queue other-config:min-rate=10000000 other-config:max-rate=10000000";

    //wsn服务地址
    public static String wsnAddr = "http://127.0.0.1:9010/wsn-core";

    //wsn消息接收地址
    public static String publishAddr = "http://127.0.0.1:%d/wsn-publish";

    //消息接收端口，从10000开始，预先保留200个
    public static int publishPort = 10000;

    //订阅地址
    public static String receiveAddr = "http://127.0.0.1:9016/wsn-subscribe";

    public static String receiveTopic = "test1";

    public static String sendAddr = "http://192.168.100.100:9018/wsn-send";

    public static String sendTopic = "test1";

    //队列调度间隔
    public static String QUEUE_PERIOD = "100000";

    //默认时延
    public static String DELAY = "1000";

    //默认丢包率
    public static String LOST_RATE = "20.0";

    /*
    负载均衡策略相关配置
     */
    //阻塞，判断子队列任务数量为多少时，子队列发生了阻塞
    public static String blockSize = "20";

    //阈值，判断拥塞子队列占比多少时，主队列阻塞，需要暂停主队列
    public static String threshold = "0.2";

    //尾丢弃策略，抛弃子队列任务最大数量
    public static String drop = "5";

    //传输视频名称
    public static String FILE = "movie.Mjpeg";

    public static double Wq = 0.75;// 平均队列长度权值
    public static double ThresholdA = 60;//队列A的时延阈值
    public static double ThresholdB = 60;//队列B的时延阈值
    public static double ThresholdC = 60;//队列C的时延阈值
    public static long aWidth;//A队列的初始带宽
    public static long bWidth;//B队列的初始带宽
    public static long cWidth;//C队列的初始带宽
    public static long nowWidthA;//A队列的当前带宽速率
    public static long nowWidthB;//B队列的当前带宽速率
    public static long nowWidthC;//C队列的当前带宽速率
    public static long portWidth;//某个端口的qos带宽
    public static int minWidthA;
    public static int minWidthB;
    public static int minWidthC;
    // 队列带宽调整权值
    public static double Wa;
    public static double Wb;
    public static double Wc;
}
