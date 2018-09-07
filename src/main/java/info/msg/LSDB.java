package info.msg;
import lombok.Data;

import java.util.Map;

/**
 * //链路状态数据库，key是groupName，当前网络中所有集群的连接情况
 */
@Data
public class LSDB {

    private Map<String, LSA> LSDB;
}
