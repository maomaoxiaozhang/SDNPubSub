package edu.bupt.wangfu.module.managerMgr.util;

import lombok.Data;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

@Data
public class Policy {
    String targetTopic;
    List<String> targetGroups;

    @XmlElement(name="targetTopic")
    public String getTargetTopic() {
        return targetTopic;
    }

    public void setTargetTopic(String targetTopic) {
        this.targetTopic = targetTopic;
    }

    @XmlElementWrapper(name="targetGroups")
    @XmlElement(name = "group")
    public List<String> getTargetGroups() {
        return targetGroups;
    }

    public void setTargetGroups(List<String> targetGroups) {
        this.targetGroups = targetGroups;
    }
}
