package edu.bupt.wangfu.module.managerMgr.schemaMgr;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.*;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class XSDUtil {

    public XSDNode connectXSD(String xsd)throws Exception{
        XSDNode xsdNode = new XSDNode();
        SAXReader saxReader = new SAXReader();
        Document doc = null;
        doc = saxReader.read( xsd );
        Element element = doc.getRootElement();
        String description = "";
        description+=element.getQualifiedName()+" ";
        for(int i = 0;i<element.attributes().size();i++){
            description+=element.attribute(i).getName()+"="+element.attributeValue( element.attribute( i ).getQName() )+" ";
        }
        xsdNode.setDescription(description);
        xsdNode.setPath(element.getName());
        readXSD(element,xsdNode);
        return xsdNode;
    }

    public void readXSD(Element element,XSDNode xsdNode){
        Iterator<Element> subElement = element.elementIterator();
        if(subElement.hasNext()) {
            ArrayList<XSDNode> subs = new ArrayList<>();
            while (subElement.hasNext()) {
                Element ele = (Element) subElement.next();
                XSDNode node = new XSDNode();
                String description = "";
                description+=ele.getQualifiedName()+" ";
                for(int i = 0;i<ele.attributes().size();i++){
                    description+=ele.attribute(i).getName()+"="+ele.attributeValue( ele.attribute( i ).getQName() )+" ";
                }
                node.setDescription(description);
                node.setPath(xsdNode.getPath()+"/"+ele.getName());
                readXSD(ele, node);
                subs.add(node);
            }
            xsdNode.setSubXSDNode(subs);
        }
    }

    public void addNewTag(XSDNode xsdNode, String xsd,String name) throws DocumentException, IOException {
        String[] paths = xsdNode.getPath().split( "/" );
        //System.out.println(paths);
        SAXReader saxReader = new SAXReader();
        Document doc = null;
        doc = saxReader.read( xsd );
        Element element = doc.getRootElement();
        for(int i = 1;i<paths.length;i++){
            element = element.element(paths[i]);
        }
        element.addElement(name);
        XMLWriter xmlWriter = new XMLWriter(new FileWriter(xsd)); //dom4j提供了专门写入文件的对象XMLWriter
        xmlWriter.write(doc);
        xmlWriter.close();
    }



    public static void main(String[] args) {
        XSDNode node = new XSDNode();
        node.setPath("schema/element/complexType/sequence");
        try {
            //String realPath = XSDUtil.class.getResource("/").getPath();
            XSDUtil xsdUtil = new XSDUtil();
            xsdUtil.addNewTag(node,"./schema/test.xsd","xs:element");
            //XSDNode xsdNode = xsdUtil.connectXSD("./schema/test.xsd" );
            //System.out.println(xsdNode);
//            for(Element ele : elements){
//                System.out.println(ele);
//            }
//            List<XSDNode> nodes = xsdReader.paserXSD("./schema/test.xsd");
//            for (XSDNode node : nodes) {
//                System.out.println(node.getUnboundedXpath());
//            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
