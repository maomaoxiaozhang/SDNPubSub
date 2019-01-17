package edu.bupt.wangfu.module.routeMgr.util;

import edu.bupt.wangfu.info.device.Flow;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

/**
 * 保存所有的流表
 */
@Component
@Data
public class AllFlows {
    List<Flow> flowList = new LinkedList<>();
}
