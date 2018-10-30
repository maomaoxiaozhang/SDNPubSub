package edu.bupt.wangfu.module.managerMgr.util;

import edu.bupt.wangfu.info.message.admin.GroupMessage;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
public class AllGroups {
    //key——集群名，value——GroupMessage集群信息
    Map<String, GroupMessage> allGroups;
}
