package edu.bupt.wangfu.module.queueMgr.util;

import edu.bupt.wangfu.info.device.Controller;
import edu.bupt.wangfu.info.device.Queue;
import edu.bupt.wangfu.info.device.Switch;
import edu.bupt.wangfu.module.switchMgr.odl.OvsProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.*;

import static edu.bupt.wangfu.module.util.Constant.*;

/**
 * 本地带宽调整，根据时延、带宽约束值，分配队列带宽
 */
@Component
public class QueueAdjust implements Runnable{

    @Autowired
    Controller controller;

    @Autowired
    OvsProcess ovsProcess;

    private static DecimalFormat df = new DecimalFormat("0.000");

    // 计算队列长度的list
    private long qLengthA = 0;
    private long qLengthB = 0;
    private long qLengthC = 0;
    private long qCount = 0;
    private List<Long> avgLenListA = new LinkedList<>();
    private List<Long> avgLenListB = new LinkedList<>();
    private List<Long> avgLenListC = new LinkedList<>();
    private int usualTime = 0;

    @Override
    public void run() {
        new Timer().schedule(new RefreshQueue(), 0, QUEUE_PERIOD);
        System.out.println("队列调整定时任务启动，刷新频率为" + QUEUE_PERIOD + "豪秒/次");
    }

    private class RefreshQueue extends TimerTask {
        @Override
        public void run() {
            for (Switch swt : controller.getOutSwitches().values()) {
                Map<Integer, List<Queue>> queueMap = swt.getQueueMap();
                for (Integer outPort : queueMap.keySet()) {
                    setValue(outPort, queueMap.get(outPort));
                }
            }
        }

        private void setValue(Integer outPort, List<Queue> queues) {
            // 获取队列的出入数据，计算出队列长度
            for (Queue queue : queues) {
                ovsProcess.getInAndOutRate(queue);
                getAvgQAndPktLossRate(queue);
            }
            analyseQueueByAvgLen(String.valueOf(outPort), queues);
            //analyseQueueByPktLossRate(queues);
        }

        /**
         * 增量差法和RED公式计算队列拥塞
         */
        private void getAvgQAndPktLossRate(Queue queue) {
            Double avgQue;
            if (queue.getInRate() == 0) {
                avgQue = 0.0;
            } else {
                //Long q = queue.getInRate() - queue.getOutRate();
                // 计算增量差
                long deltaIn = queue.getInRate() - queue.getLastInRate();
                long deltaOut = queue.getOutRate() - queue.getLastOutRate();
                //double pktLoss = deltaIn == 0 ? 0 : (deltaIn - deltaOut) / deltaIn;
                double q = 1.0 * deltaIn - deltaOut;
                avgQue = (1 - Wq) * queue.getAvgQ() + Wq * q;
                //pktLossRate = getPktLossRate(queue);
            }
            queue.setAvgQ(avgQue);
            //queue.setPktLossRate(pktLossRate);
        }

        private void analyseQueueByAvgLen(String outPort, List<Queue> queues) {
            qCount++;
            qLengthA += queues.get(0).getAvgQ();
            qLengthB += queues.get(1).getAvgQ();
            qLengthC += queues.get(2).getAvgQ();

            int gap = 5;
            if (qCount % gap == 0) {

                avgLenListA.add(qLengthA / gap);
                avgLenListB.add(qLengthB / gap);
                avgLenListC.add(qLengthC / gap);

                if (avgLenListA.size() == 5) {
                    double locA = getLevelOfCongestion(avgLenListA, 7);
                    double locB = getLevelOfCongestion(avgLenListB, 6);
                    double locC = getLevelOfCongestion(avgLenListC, 5);
                    System.out.println(outPort + ": " + df.format(locA*1000) + " -- " + df.format(locB*1000) +
                            " -- " + df.format(locC*1000));

                    adjustQueueWidth(queues, locA, locB, locC);

                    avgLenListA.clear();
                    avgLenListB.clear();
                    avgLenListC.clear();
                }
                qLengthA = 0;
                qLengthB = 0;
                qLengthC = 0;
            }
        }

        /**
         * 带宽分配算法
         */

        // 带宽重分配核心算法实现
        private void adjustQueueWidth(List<Queue> queues, double locA, double locB, double locC) {

            // 拥塞程度为0，无需调整带宽
            if (locA*1000 <= ThresholdA && locB*1000 <= ThresholdB && locC*1000 <= ThresholdC) {
                usualTime ++;
                if (usualTime >= 3) {
                    // 连续多次队列无拥塞，则初始化队列带宽
                    String cmds = "/ovs/bin/ovs-vsctl set queue " + queues.get(0).getUuid() + " other-config=min-rate=" + aWidth + ",max-rate=" + aWidth +
                            " && /ovs/bin/ovs-vsctl set queue " + queues.get(1).getUuid() + " other-config=min-rate=" + bWidth + ",max-rate=" + bWidth +
                            " && /ovs/bin/ovs-vsctl set queue " + queues.get(2).getUuid() + " other-config=min-rate=" + cWidth + ",max-rate=" + cWidth;

                    ovsProcess.remoteExecuteCommand(cmds);
                    usualTime = 0;
                }
                return;
            }
            // 队列全部拥塞，则通知发布者降低发包频率
            if (locA*1000 > ThresholdA && locB*1000 > ThresholdB && locC*1000 > ThresholdC) {
                System.out.println("队列全部发生拥塞，正在降低发包频率......");
                System.out.println("Meter限速启动");

//                meterMgr.associateMeterWithQueue(queues.get(0));
                // 触发服务访问限制次数，降低25%
//                maxTimes -= maxTimes * 0.25;
            }
            if (locA * 1000 > ThresholdA && nowWidthA == portWidth - minWidthB - minWidthC) {
                System.out.println("队列7带宽已达上限，无力调整，正在启动Meter限速......");
//                meterMgr.associateMeterWithQueue(queues.get(0));
                System.out.println("Meter限速至" + (portWidth - minWidthB - minWidthC) + "bps.");
                System.out.println("通知发布者降低发包频率中......");
            }

            double min = Double.MAX_VALUE;
            long width7 = nowWidthA, width6 = nowWidthB, width5 = nowWidthC;
            double delay7 = 0, delay6 = 0, delay5 = 0;
            long portWd = portWidth / 1000000;
            double qLenA = locA * nowWidthA, qLenB = locB * nowWidthB, qLenC = locC * nowWidthC;
            for (long w7 = minWidthA; w7 < portWd; w7++) {
                for (long w6 = minWidthB; w6 < portWd; w6++) {
                    for (long w5 = portWd - w7 - w6; w5 >= minWidthC && w5 < portWd; w5--) {
                        double t7 = qLenA / (w7 * 1000);
                        double t6 = qLenB / (w6 * 1000);
                        double t5 = qLenC / (w5 * 1000);
                        //System.out.println("t7: " + t7 + ",t6: " + t6 + ",t5: " + t5);
                        double temp = Wa * t7 + Wb * t6 + Wc * t5;
                        if (temp < min /*&& t7 <= ThresholdA && t6 <= ThresholdB && t5 <= ThresholdC*/) {
                            min = temp;
                            width7 = w7;
                            width6 = w6;
                            width5 = w5;
                            delay7 = t7;
                            delay6 = t6;
                            delay5 = t5;
                        }
                    }
                }
            }
            if (width7 != nowWidthA) {
                width7 *= 1000000;
                nowWidthA = width7;
                queues.get(0).setNowRate(nowWidthA);
            }
            if (width6 != nowWidthB) {
                width6 *= 1000000;
                nowWidthB = width6;
                queues.get(1).setNowRate(nowWidthB);
            }
            if (width5 != nowWidthC) {
                width5 *= 1000000;
                nowWidthC = width5;
                queues.get(2).setNowRate(nowWidthC);
            }

            System.out.println("新带宽计算完毕，分别是：q7: " + width7 + ", q6: " + width6 + ", q5: " + width5);

            String cmds = "/ovs/bin/ovs-vsctl set queue " + queues.get(0).getUuid() + " other-config=min-rate=" + width7 + ",max-rate=" + width7 +
                    " && /ovs/bin/ovs-vsctl set queue " + queues.get(1).getUuid() + " other-config=min-rate=" + width6 + ",max-rate=" + width6 +
                    " && /ovs/bin/ovs-vsctl set queue " + queues.get(2).getUuid() + " other-config=min-rate=" + width5 + ",max-rate=" + width5;

            ovsProcess.remoteExecuteCommand(cmds);

            // 向管理员反馈
//            DelayFeedback.feedbackToAdmin(delay7/10, delay6/10, delay5/10);
        }

        // 获取队列拥塞程度
        private double getLevelOfCongestion(List<Long> data, int qID) {
            long max = Long.MIN_VALUE;
            long min = Long.MAX_VALUE;
            for (long n : data) {
                if (n <= 0) return 0.0;
                if (n > max) max = n;
                if (n < min) min = n;
            }
            if (max / min > 2) return 0.0;

            double level = 0;
            double avgLen = getAverage(data);
            if (qID == 7) {
                level = avgLen * 8 / nowWidthA;
            }
            if (qID == 6) {
                level = avgLen * 8 / nowWidthB;
            }
            if (qID == 5) {
                level = avgLen * 8 / nowWidthC;
            }
            return level;
        }

        private double getAverage(List<Long> data) {
            double avg = 0;
            for (Long n : data) {
                avg += n;
            }
            avg /= data.size();
            return avg;
        }
    }
}
