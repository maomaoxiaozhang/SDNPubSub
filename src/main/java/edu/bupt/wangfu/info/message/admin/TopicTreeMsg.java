package edu.bupt.wangfu.info.message.admin;

import lombok.Data;

@Data
public class TopicTreeMsg extends AdminMessage{
    //主题树信息
    private String topicTree;
}
