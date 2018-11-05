package edu.bupt.wangfu.role.controller.listener;

import edu.bupt.wangfu.info.device.Controller;
import edu.bupt.wangfu.info.message.admin.AdminMessage;
import edu.bupt.wangfu.info.message.admin.EncodeTopicTreeMsg;
import edu.bupt.wangfu.info.message.admin.RequestFeedBackMsg;
import edu.bupt.wangfu.module.switchMgr.odl.OvsProcess;
import edu.bupt.wangfu.module.topicTreeMgr.topicTree.EncodeTopicTree;
import edu.bupt.wangfu.module.util.MultiHandler;
import edu.bupt.wangfu.role.controller.ControllerStart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import static edu.bupt.wangfu.module.util.Constant.PRIORITY;

/**
 * 控制器监听管理路径消息，包括主题树编码、队列带宽分配信息
 */
@Component
public class AdminListener implements Runnable{

    @Autowired
    Controller controller;

    @Autowired
    @Lazy
    ControllerStart controllerStart;

    @Autowired
    OvsProcess ovsProcess;

    @Override
    public void run() {
        System.out.println("控制器 admin 监听启动~~");
        int adminPort = controller.getAdminPort();
        String address = controller.getAdminV6Addr();
        MultiHandler handler = new MultiHandler(adminPort, address);
        while (true) {
            Object msg = handler.v6Receive();
            onMsgReceive(msg);
        }
    }

    /**
     * 收到消息后的处理措施：
     *     1. 更新本地主题编码树
     *     2. 向wsn发送主题树
     * @param msg
     */
    private void onMsgReceive(Object msg) {
        if (msg instanceof AdminMessage) {
            if (msg instanceof EncodeTopicTreeMsg) {
                System.out.println("EncodeTopicTreeMsg");
                EncodeTopicTree encodeTopicTree = ((EncodeTopicTreeMsg) msg).getEncodeTopicTree();
                controllerStart.setEncodeTopicTree(encodeTopicTree);

                //事件驱动，发送接收到的主题树编码
                int wsnPort = controller.getWsnPort();
                String address = controller.getWsnV6Addr();
                MultiHandler handler = new MultiHandler(wsnPort, address);
                handler.v6Send(encodeTopicTree);
            }else if (msg instanceof RequestFeedBackMsg) {
                //下发具体带宽分配结果
                RequestFeedBackMsg feedBackMsg = (RequestFeedBackMsg) msg;
                System.out.println("RequestFeedBackMsg\t" + feedBackMsg.getBind());
                handleFeedBack(feedBackMsg);
            }
        }
    }

    /**
     * 管理员根据用户需求下发带宽分配情况，控制器执行流表下发
     * @param feedBackMsg
     */
    public void handleFeedBack(RequestFeedBackMsg feedBackMsg) {
        int queue = feedBackMsg.getQueue();
        double bind = feedBackMsg.getBind();



    }
}
