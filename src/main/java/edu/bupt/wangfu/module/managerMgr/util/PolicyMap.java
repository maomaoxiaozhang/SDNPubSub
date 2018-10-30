package edu.bupt.wangfu.module.managerMgr.util;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Data
@Component
public class PolicyMap {
    //key——主题名，value——policy策略
    public Map<String, Policy> policyMap = new HashMap<>();
}
