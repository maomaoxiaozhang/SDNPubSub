package edu.bupt.wangfu.info.message.admin;

import lombok.Data;

@Data
public class LookUpMsg extends AdminMessage{
    //查询类型
    private String type;
}
