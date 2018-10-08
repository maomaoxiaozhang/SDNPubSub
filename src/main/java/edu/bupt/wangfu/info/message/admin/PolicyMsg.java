package edu.bupt.wangfu.info.message.admin;

import lombok.Data;

@Data
public class PolicyMsg extends AdminMessage {
    //相关策略信息
    private String policy;
}
