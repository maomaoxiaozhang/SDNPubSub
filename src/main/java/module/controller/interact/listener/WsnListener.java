package module.controller.interact.listener;

import module.controller.interact.handler.MsgHandler;

public class WsnListener implements Runnable{

    private String topic;
    private MsgHandler msgHandler;

    public WsnListener(String topic) {
        this.topic = topic;
        this.msgHandler = new MsgHandler(topicCode.get(topic), notifyPort);
        this.msgHandler.setInterface(localAddr2);
    }

    @Override
    public void run() {
        System.out.println("本地有新订阅，节点订阅监听启动，主题为：" + topic);
        while (true) {
            //System.out.println("new msg from " + localAddr2);
            String msg = (String) msgHandler.receive();
            WsnProcesser.run(msg);
        }
    }
}
