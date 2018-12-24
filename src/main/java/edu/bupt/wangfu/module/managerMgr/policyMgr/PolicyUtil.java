package edu.bupt.wangfu.module.managerMgr.policyMgr;

import edu.bupt.wangfu.module.managerMgr.util.Policy;
import java.io.File;
import java.io.IOException;
import java.util.*;

import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.*;
import javax.xml.xpath.*;
public class PolicyUtil {

    private static final String POLICYMSG = "./policyMsg.xml";

    public static Map<String,Policy> readXMLFile() throws ParserConfigurationException, IOException, SAXException {
        //得到dom解析器的工厂实例
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder bulid = null;
        //从dom工厂对象获取dom解析器
        bulid = dbf.newDocumentBuilder();
        Document doc = null;
        //解析xml文档的document对象
        doc = bulid.parse(POLICYMSG);
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

    public static void addNewPolicy(Policy policy) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder bulid = dbf.newDocumentBuilder();
        Document doc = bulid.parse(POLICYMSG);
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
        newPolicy.appendChild(targetGroup);
        newPolicy.appendChild(targetTopic);
        root.appendChild(newPolicy);
        output(doc);
    }

    public static void deletePolicy(Policy policy) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder bulid = dbf.newDocumentBuilder();
        Document doc = bulid.parse(POLICYMSG);
        doc.normalize();
        Element root = doc.getDocumentElement();
        Element targetPolicy = (Element) selectSingleNode("/policyList/policy[@targetTopic='"+policy.getTargetTopic()+"']", root);
        targetPolicy.getParentNode().removeChild(targetPolicy);
        output(doc);
    }

    public void modifypolicy(Policy policy) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder bulid = dbf.newDocumentBuilder();
        Document doc = bulid.parse(POLICYMSG);
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

    public static Node selectSingleNode(String express, Object source) {//查找节点，并返回第一个符合条件节点
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

    public static void output(Node node) {//将node的XML字符串输出到控制台
        TransformerFactory transFactory=TransformerFactory.newInstance();
        try {
            Transformer transformer = transFactory.newTransformer();
            transformer.setOutputProperty("encoding", "utf-8");
            transformer.setOutputProperty("indent", "yes");
            DOMSource source=new DOMSource();
            source.setNode(node);
            StreamResult result=new StreamResult();
            result.setOutputStream(System.out);

            transformer.transform(source, result);
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }


//    public static void updateXMLFile(Map<String,Policy> policyMap) throws Exception{
//        //得到dom解析器的工厂实例
//        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//        DocumentBuilder bulid = null;
//        try{
//            //从dom工厂对象获取dom解析器
//            bulid = dbf.newDocumentBuilder();
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//
//        Document doc = null;
//        try{
//            //解析xml文档的document对象
//            doc = bulid.parse(POLICYMSG);
//            //去掉xml文档中格式化内容空白
//            doc.normalize();
//        }catch(Exception dom){
//            dom.printStackTrace();
//        }
//        Element root = doc.getDocumentElement();
//        //获取xml文档元素节点集合
//        //Element policyList= (Element)root.getElementsByTagName("policyList").item(0);
//        NodeList nodeList = root.getElementsByTagName("policy");
//
//        while(nodeList.getLength()>0){
//            root.removeChild(nodeList.item(0));
//        }
//        for(Policy policy : policyMap.values()){
//            Element element = doc.createElement("policy");
//            root.appendChild(element);
//            Element targetTopic = doc.createElement("targetTopic");
//            element.appendChild(targetTopic);
//            Text tTopic = doc.createTextNode(policy.getTargetTopic());
//            targetTopic.appendChild(tTopic);
//            Element targetGroup = doc.createElement("targetGroup");
//            element.appendChild(targetGroup);
//            for(int j=0;j<policy.getTargetGroups().size();j++){
//                Element  group = doc.createElement("group");
//                targetGroup.appendChild(group);
//                Text tGroup = doc.createTextNode(""+policy.getTargetGroups().get(j));
//                group.appendChild(tGroup);
//            }
//        }
//        domDocToFile(doc);
//
//    }

    public static void main(String[] args) {
        try{
            PolicyUtil util = new PolicyUtil();
//            String all = util.readAll();
//            Map<String,Policy> policyMap = util.readXMLFile();
//            policyMap.get("all:E").getTargetGroups().add("G2");
//            util.updateXMLFile(policyMap);
            /*for(Policy policy:policyMap.values()){
                System.out.print(policy.getTargetTopic());
                for(String group:policy.getTargetGroups()){
                    System.out.print(group+" ");
                }
                System.out.println();

            }*/
//            System.out.println(all);
            //System.out.println("文件（包括路径和后缀）："+PolicyUtil.wirteXMLFile(, policyMap));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
