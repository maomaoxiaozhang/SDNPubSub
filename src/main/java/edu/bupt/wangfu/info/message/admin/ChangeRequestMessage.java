package edu.bupt.wangfu.info.message.admin;

import lombok.Data;

import java.io.Serializable;

@Data
public class ChangeRequestMessage implements Serializable {
    private String topic;
    private String groupName;
    private double minDelay;

    public ChangeRequestMessage(String topic, String groupName, double minDelay) {
        this.topic = topic;
        this.groupName = groupName;
        this.minDelay = minDelay;
    }
}
