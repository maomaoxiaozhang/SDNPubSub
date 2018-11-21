package edu.bupt.wangfu.module.managerMgr.util;

import edu.bupt.wangfu.info.message.admin.GroupMessage;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Data
@Component
public class AllGroups {
    //key——集群名，value——GroupMessage集群信息
    @Value("${allGroups:#{null}}")
    Map<String, GroupMessage> allGroups = new HashMap<>();
}
