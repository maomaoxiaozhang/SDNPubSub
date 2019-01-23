package edu.bupt.wangfu.module.managerMgr.schemaMgr;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class XSDUtil {

    public XSDNode readXSDRoot(String xsd)throws Exception{
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
        xsdNode.setPath("");
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
                if(ele.getQualifiedName().equals( "xs:element" )) {
                    if(xsdNode.getPath().equals( "" )){
                        node.setPath(ele.getQualifiedName()+"[@name=\"" + ele.attributeValue( "name" ) + "\"]");
                    }
                    else{
                        node.setPath(xsdNode.getPath()+"/"+ele.getQualifiedName()+"[@name=\"" + ele.attributeValue( "name" ) + "\"]");
                    }
                }
                else{
                    if(xsdNode.getPath().equals( "" )){
                        node.setPath(ele.getQualifiedName());
                    }
                    else{
                        node.setPath(xsdNode.getPath()+"/"+ele.getQualifiedName());
                    }

                }
                readXSD(ele, node);
                subs.add(node);
            }
            xsdNode.setSubXSDNode(subs);
        }
    }

    public String readAll(String xsd) throws DocumentException {
        SAXReader saxReader = new SAXReader();
        Document doc = null;
        doc = saxReader.read( xsd );
        return doc.asXML();
    }

    public void modify(String xsd,XSDNode node,String attr,String value) throws DocumentException, IOException {
        SAXReader saxReader = new SAXReader();
        Document doc = null;
        doc = saxReader.read(xsd);
        Element element = doc.getRootElement();
        Element targetElement = (Element)element.selectSingleNode(node.getPath());
        targetElement.attribute( attr ).setValue( value );
        XMLWriter xmlWriter = new XMLWriter(new FileWriter(xsd)); //dom4j提供了专门写入文件的对象XMLWriter
        xmlWriter.write(doc);
        xmlWriter.close();
    }

    public void addTag(String xsd,XSDNode node,XSDNode newNode) throws DocumentException, IOException {
        SAXReader saxReader = new SAXReader();
        Document doc = null;
        doc = saxReader.read( xsd );
        Element element = doc.getRootElement();
        Element targetElement = null;
        if(node.getPath().equals( "" )) targetElement = element;
        else targetElement= (Element)element.selectSingleNode(node.getPath());
        Element newElement=targetElement.addElement(newNode.getTagType());
        if(newNode.getName() != ""){
            newElement.addAttribute( "name",newNode.getName() );
        }
        if(newNode.getType() != ""){
            newElement.addAttribute( "type",newNode.getType() );
        }
        OutputFormat outputFormat = OutputFormat.createPrettyPrint();
        outputFormat.setIndent(true); //设置是否缩进
        outputFormat.setIndent("    "); //以四个空格方式实现缩进
        outputFormat.setNewlines(true); //设置是否换行
        XMLWriter xmlWriter = new XMLWriter(new FileWriter(xsd),outputFormat); //dom4j提供了专门写入文件的对象XMLWriter
        xmlWriter.write(doc);
        xmlWriter.close();

    }


    public void deleteTag(String xsd,XSDNode node) throws DocumentException, IOException {
        SAXReader saxReader = new SAXReader();
        Document doc = null;
        doc = saxReader.read(xsd);
        Element element = doc.getRootElement();
        Element targetElement = (Element)element.selectSingleNode(node.getPath());
        targetElement.getParent().remove( targetElement );
        XMLWriter xmlWriter = new XMLWriter(new FileWriter(xsd)); //dom4j提供了专门写入文件的对象XMLWriter
        xmlWriter.write(doc);
        xmlWriter.close();
    }

    public static void main(String[] args) {
        XSDNode node = new XSDNode();
//        node.setPath("xs:element[@name=\"test\"]/xs:complexType/xs:sequence");
//        XSDNode newNode = new XSDNode();
//        newNode.setTagType( "xs:element" );
//        newNode.setName( "test" );
//        newNode.setType( "xs:string" );
        node.setPath("xs:element[@name=\"test1\"]");
        try {
            //String realPath = XSDUtil.class.getResource("/").getPath();
            XSDUtil xsdUtil = new XSDUtil();
            xsdUtil.deleteTag( "./schema/test.xsd",node);
            //xsdUtil.addTag("./schema/test.xsd",node,newNode);
            //xsdUtil.addTreeTag( "./schema/test.xsd",newNode );
            //xsdUtil.rename("./schema/test.xsd",node,"from1");
            //XSDNode xsdNode = xsdUtil.readXSDRoot("./schema/test.xsd" );
            //System.out.println(xsdNode);
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
