package edu.bupt.wangfu.module.queueMgr;

import edu.bupt.wangfu.module.queueMgr.util.QueueAdjust;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;

/**
 * 队列管理，分为两部分：
 *      1. 本地带宽调整
 *          根据时延、带宽约束值分配队列带宽
 *          controller --> switch
 *      2. 全局质量控制
 *          根据用户输入的时延、带宽需求，结合订阅路径，计算并下发约束值结果
 *          user --> wsn --> controller --> manager --> controller --> wsn --> user
 *
 * <p>
 *     本地带宽调整和全局质量控制相互配合，行成两级队列带宽控制机制，控制器负责本地调整，而管理员负责全局控制
 *     本地调整的时延约束值由管理员根据用户需求来设定
 *     管理员时延约束值计算的根据是控制器的实时反馈
 * </p>
 *
 * @see QueueAdjust
 */
@Component
public class QueueMgr {

    @Autowired
    QueueAdjust queueAdjust;

    public void start(ExecutorService exec) {
        exec.execute(queueAdjust);
        exec.shutdown();
    }
}
