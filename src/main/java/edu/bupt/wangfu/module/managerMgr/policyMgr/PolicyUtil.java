package edu.bupt.wangfu.module.managerMgr.policyMgr;

import edu.bupt.wangfu.module.managerMgr.util.Policy;
import java.io.File;
import java.io.IOException;
import java.util.*;

import org.dom4j.DocumentException;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.*;
import javax.xml.xpath.*;
public class PolicyUtil {

    private static final String POLICYMSG = "./policyMsg.xml";

    public static Map<String,Policy> readXMLFile()  {
        //得到dom解析器的工厂实例
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder bulid = null;
        //从dom工厂对象获取dom解析器
        try {
            bulid = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document doc = null;
        //解析xml文档的document对象
        try {
            doc = bulid.parse(POLICYMSG);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //去掉xml文档中格式化内容空白
        doc.normalize();
        Map<String,Policy> policyMap = new HashMap<>();
        Policy policy = new Policy();
        Element root = doc.getDocumentElement();
        NodeList nodeList = root.getElementsByTagName("policy");
        for(int i=0;i<nodeList.getLength();i++){
            Element element = (Element) nodeList.item(i);
            policy = new Policy();
            policy.setTargetTopic(element.getElementsByTagName("targetTopic").item(0).getTextContent());
            List<String> groups = new ArrayList<>();
            Element targetGroup = (Element)element.getElementsByTagName("targetGroup").item(0);
            for(int j=0;j<targetGroup.getElementsByTagName("group").getLength();j++){
                groups.add(element.getElementsByTagName("group").item(j).getTextContent());
            }
            policy.setTargetGroups(groups);
            policyMap.put(policy.getTargetTopic(),policy);
        }
        return policyMap;
    }

    public void addNewPolicy(Policy policy) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder bulid = null;
        try {
            bulid = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document doc = null;
        try {
            doc = bulid.parse(POLICYMSG);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        doc.normalize();
        Element root = doc.getDocumentElement();
        Element newPolicy=doc.createElement("policy");
        Element targetTopic = doc.createElement("targetTopic");
        targetTopic.setTextContent( policy.getTargetTopic() );
        Element targetGroup = doc.createElement("targetGroup");
        for(String groupName : policy.getTargetGroups()){
            Element group = doc.createElement("group");
            group.setTextContent(groupName);
            targetGroup.appendChild( group );
        }
        root.appendChild(newPolicy);
        newPolicy.appendChild(targetTopic);
        newPolicy.appendChild(targetGroup);

        output(doc);
    }

    public String readAll() {
        String allPolicy = "";
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder bulid = null;
        try {
            bulid = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document doc = null;
        try {
            doc = bulid.parse(POLICYMSG);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        doc.normalize();
        Policy policy;
        Element root = doc.getDocumentElement();
        NodeList nodeList = root.getElementsByTagName("policy");
        for(int i=0;i<nodeList.getLength();i++){
            Element element = (Element) nodeList.item(i);
            policy = new Policy();
            policy.setTargetTopic(element.getElementsByTagName("targetTopic").item(0).getTextContent());
            List<String> groups = new ArrayList<>();
            Element targetGroup = (Element)element.getElementsByTagName("targetGroup").item(0);
            for(int j=0;j<targetGroup.getElementsByTagName("group").getLength();j++){
                groups.add(element.getElementsByTagName("group").item(j).getTextContent());
            }
            policy.setTargetGroups(groups);
            allPolicy += policy.toString()+"\n";
        }
        return allPolicy;
    }

    public void deletePolicy(Policy policy) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder bulid = null;
        try {
            bulid = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document doc = null;
        try {
            doc = bulid.parse(POLICYMSG);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        doc.normalize();
        Element root = doc.getDocumentElement();
        Element targetPolicy = (Element) selectSingleNode("/policyList/policy[@targetTopic='"+policy.getTargetTopic()+"']", root);
        targetPolicy.getParentNode().removeChild(targetPolicy);
        output(doc);
    }

    public void modifypolicy(Policy policy) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder bulid = null;
        try {
            bulid = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document doc = null;
        try {
            doc = bulid.parse(POLICYMSG);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        doc.normalize();
        Element root = doc.getDocumentElement();
        Element targetPolicy = (Element) selectSingleNode("/policyList/policy[@targetTopic='"+policy.getTargetTopic()+"']", root);
        targetPolicy.removeChild((Element) targetPolicy.getElementsByTagName( "targetGroup" ) );
        Element targetGroup = doc.createElement("targetGroup");
        for(String groupName : policy.getTargetGroups()){
            Element group = doc.createElement("group");
            group.setTextContent(groupName);
            targetGroup.appendChild( group );
        }
        targetPolicy.appendChild(targetGroup);
        output(doc);
    }

    public Node selectSingleNode(String express, Object source) {//查找节点，并返回第一个符合条件节点
        Node result=null;
        XPathFactory xpathFactory=XPathFactory.newInstance();
        XPath xpath=xpathFactory.newXPath();
        try {
            result=(Node) xpath.evaluate(express, source, XPathConstants.NODE);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void output(Document doc) {//将node的XML字符串输出到控制台
        TransformerFactory transFactory=TransformerFactory.newInstance();
        try {
            Transformer transformer = transFactory.newTransformer();
            transformer.setOutputProperty("encoding", "utf-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            DOMSource source=new DOMSource(doc);
            StreamResult result=new StreamResult(new File(POLICYMSG));
            transformer.transform(source, result);
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }


//    public static void main(String[] args) {
//        try{
//            String groups = "G1,G2";
//            PolicyUtil util = new PolicyUtil();
//            Policy policy = new Policy();
//            List groupList = Arrays.asList( groups.split( "," ) );
//            policy = new Policy();
//            policy.setTargetTopic( "test" );
//            policy.setTargetGroups( groupList );
//            util.addNewPolicy( policy );
////            String all = util.readAll();
////            Map<String,Policy> policyMap = util.readXMLFile();
////            policyMap.get("all:E").getTargetGroups().add("G2");
////            util.updateXMLFile(policyMap);
//            /*for(Policy policy:policyMap.values()){
//                System.out.print(policy.getTargetTopic());
//                for(String group:policy.getTargetGroups()){
//                    System.out.print(group+" ");
//                }
//                System.out.println();
//
//            }*/
////            System.out.println(all);
//            //System.out.println("文件（包括路径和后缀）："+PolicyUtil.wirteXMLFile(, policyMap));
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//    }

}
