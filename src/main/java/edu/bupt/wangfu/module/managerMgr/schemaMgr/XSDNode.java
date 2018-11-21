package edu.bupt.wangfu.module.managerMgr.schemaMgr;

import lombok.Data;

import javax.xml.namespace.QName;
import java.util.List;

@Data
public class XSDNode {
    // 节点名称
    private String description;
    // 子节点XPath
    private List<XSDNode> subXSDNode;
    private String path;


//    @Override
//    public String toString(){
//        return getDescription();
//    }
}
