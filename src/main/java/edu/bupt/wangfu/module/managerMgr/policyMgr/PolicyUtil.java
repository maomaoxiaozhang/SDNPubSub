package edu.bupt.wangfu.module.managerMgr.policyMgr;

import edu.bupt.wangfu.module.managerMgr.util.Policy;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.*;

public class PolicyUtil {

    private static final String POLICYMSG = "./policyMsg.xml";

    public static Map<String,Policy> readXMLFile() {
        //得到dom解析器的工厂实例
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder bulid = null;
        try{
            //从dom工厂对象获取dom解析器
            bulid = dbf.newDocumentBuilder();
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }

        Document doc = null;
        try{
            //解析xml文档的document对象
            doc = bulid.parse(POLICYMSG);
            //去掉xml文档中格式化内容空白
            doc.normalize();
        }catch(Exception dom){
            dom.printStackTrace();
        }

        Map<String,Policy> policyMap = new HashMap<>();
        Policy policy = new Policy();
        Element root = doc.getDocumentElement();
        //获取xml文档元素节点集合
        //Element policyList= (Element)root.getElementsByTagName("policyList").item(0);
        NodeList nodeList = root.getElementsByTagName("policy");
        for(int i=0;i<nodeList.getLength();i++){
            System.out.println("start");
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

    public static void updateXMLFile(Map<String,Policy> policyMap) throws Exception{
        //得到dom解析器的工厂实例
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder bulid = null;
        try{
            //从dom工厂对象获取dom解析器
            bulid = dbf.newDocumentBuilder();
        }catch(Exception e){
            e.printStackTrace();
        }

        Document doc = null;
        try{
            //解析xml文档的document对象
            doc = bulid.parse(POLICYMSG);
            //去掉xml文档中格式化内容空白
            doc.normalize();
        }catch(Exception dom){
            dom.printStackTrace();
        }
        Element root = doc.getDocumentElement();
        //获取xml文档元素节点集合
        //Element policyList= (Element)root.getElementsByTagName("policyList").item(0);
        NodeList nodeList = root.getElementsByTagName("policy");

        while(nodeList.getLength()>0){
            root.removeChild(nodeList.item(0));
        }
        for(Policy policy : policyMap.values()){
            Element element = doc.createElement("policy");
            root.appendChild(element);
            Element targetTopic = doc.createElement("targetTopic");
            element.appendChild(targetTopic);
            Text tTopic = doc.createTextNode(policy.getTargetTopic());
            targetTopic.appendChild(tTopic);
            Element targetGroup = doc.createElement("targetGroup");
            element.appendChild(targetGroup);
            for(int j=0;j<policy.getTargetGroups().size();j++){
                Element  group = doc.createElement("group");
                targetGroup.appendChild(group);
                Text tGroup = doc.createTextNode(""+policy.getTargetGroups().get(j));
                group.appendChild(tGroup);
            }
        }
        domDocToFile(doc);

    }

    /***
     * 用dom写xml文档，把策略信息以xml文档的形式存储
     * @param outFile
     * @param policyMap
     * @return domDocToFile(doc, outFile, outFile)
     * @throws Exception
     */
    public static String wirteXMLFile(String outFile,Map<String,Policy> policyMap) throws Exception{
        //得到dom解析器的工厂实例
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder bulid = null;
        try{
            //从dom工厂对象获取dom解析器
            bulid = dbf.newDocumentBuilder();
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
        //新建一个空白文档
        Document doc = bulid.newDocument();
        //建立根元素employees
        Element root = doc.createElement("policyList");
        //将根节点添加进入文档
        doc.appendChild(root);
        //循环获取员工的信息
        for(Policy policy : policyMap.values()){
            Element element = doc.createElement("policy");
            root.appendChild(element);
            Element targetTopic = doc.createElement("targetTopic");
            element.appendChild(targetTopic);
            Text tTopic = doc.createTextNode(policy.getTargetTopic());
            targetTopic.appendChild(tTopic);
            Element targetGroup = doc.createElement("targetGroup");
            element.appendChild(targetGroup);
            for(int j=0;j<policy.getTargetGroups().size();j++){
                Element  group = doc.createElement("group");
                targetGroup.appendChild(group);
                Text tGroup = doc.createTextNode(""+policy.getTargetGroups().get(j));
                group.appendChild(tGroup);
            }
        }
        return domDocToFile(doc);

    }

    /***
     * 使用jaxp将dom对象写入xml文档中
     * @param doc
     * @return file.getAbsolutePath()
     * @throws TransformerException
     */
    public static String domDocToFile(Document doc) throws TransformerException{
        //为了得到xslt引擎创建对象
        TransformerFactory tff = TransformerFactory.newInstance();
        //创建xslt引擎对象输出xml文档
        Transformer tf = tff.newTransformer();
        //获取属性对象
        Properties pro = tf.getOutputProperties();
        //设置编码格式和输出格式
        pro.setProperty(OutputKeys.ENCODING, POLICYMSG);
        pro.setProperty(OutputKeys.METHOD, "xml");
        tf.setOutputProperties(pro);
        //创建资源对象
        DOMSource source = new DOMSource(doc);
        //创建文件对象
        File file = new File(POLICYMSG);

        //获得输出对象
        StreamResult result = new StreamResult(file);
        //结果输出到控制台
        StreamResult result1 = new StreamResult(System.out);
        //执行dom文档到xml文件转换
        tf.transform(source, result);
        //文档输出到控制台
        tf.transform(source, result1);
        System.out.println();
        //将输出文件的路径返回
        return file.getAbsolutePath();
    }

//    public static void main(String[] args) {
//        try{
//            PolicyUtil util = new PolicyUtil();
//            Map<String,Policy> policyMap = util.readXMLFile();
//            policyMap.get("all:E").getTargetGroups().add("G2");
//            util.updateXMLFile(policyMap);
//            /*for(Policy policy:policyMap.values()){
//                System.out.print(policy.getTargetTopic());
//                for(String group:policy.getTargetGroups()){
//                    System.out.print(group+" ");
//                }
//                System.out.println();
//
//            }*/
//            System.out.println("输出到控制台的xml文档：");
//            //System.out.println("文件（包括路径和后缀）："+PolicyUtil.wirteXMLFile(, policyMap));
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//    }

}
