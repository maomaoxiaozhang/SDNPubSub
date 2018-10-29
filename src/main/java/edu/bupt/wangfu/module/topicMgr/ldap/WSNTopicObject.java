package edu.bupt.wangfu.module.topicMgr.ldap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WSNTopicObject implements Serializable {

    private static final long serialVersionUID = 1L;

    private TopicEntry topicentry;

    private WSNTopicObject parent;

    private List<WSNTopicObject> childrens;

    private List<String> subscribeAddress;

    public WSNTopicObject(TopicEntry _topicentry, WSNTopicObject _parent) {
        this.topicentry = _topicentry;
        this.parent = _parent;
        this.childrens = new ArrayList<>();
        this.subscribeAddress = new ArrayList<>();
    }

    public TopicEntry getTopicentry() {
        return topicentry;
    }


    public void setTopicentry(TopicEntry topicentry) {
        this.topicentry = topicentry;
    }

    public WSNTopicObject getParent() {
        return parent;
    }

    public void setParent(WSNTopicObject parent) {
        this.parent = parent;
    }

    public List<WSNTopicObject> getChildrens() {
        return childrens;
    }

    public void setChildrens(List<WSNTopicObject> childrens) {
        this.childrens = childrens;
    }

    public List<String> getSubscribeAddress() {
        return subscribeAddress;
    }

    public void setSubscribeAddress(List<String> subscribeAddress) {
        this.subscribeAddress = subscribeAddress;
    }


}