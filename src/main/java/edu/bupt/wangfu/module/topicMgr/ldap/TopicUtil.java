package edu.bupt.wangfu.module.topicMgr.ldap;



import edu.bupt.wangfu.module.managerMgr.schemaMgr.XSDUtil;
import edu.bupt.wangfu.module.topicTreeMgr.topicTree.TopicTreeEntry;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class TopicUtil {
    private static final String TOPIC = "./topicMsg.xml";

    public String readAll() throws DocumentException {
        SAXReader saxReader = new SAXReader();
        Document doc = null;
        doc = saxReader.read( TOPIC );
        return doc.asXML();
    }

    public TopicTreeEntry readRoot() throws DocumentException {
        TopicTreeEntry root = new TopicTreeEntry();
        SAXReader saxReader = new SAXReader();
        Document doc = null;
        doc = saxReader.read( TOPIC );
        Element element = doc.getRootElement();
        root.setTopic( "all");
        root.setLayer( 0 );
        root.setPath("/all");
        readTopic(element,root);
        return root;
    }

    public void readTopic(Element element,TopicTreeEntry root){
        Iterator<Element> subElement = element.elementIterator();
        if(subElement.hasNext()) {
            ArrayList<TopicTreeEntry> subs = new ArrayList<>();
            while (subElement.hasNext()) {
                Element ele = (Element) subElement.next();
                TopicTreeEntry node = new TopicTreeEntry();
                node.setTopic(ele.attributeValue( "name" ));
                node.setLayer(root.getLayer()+1);
                node.setPath(root.getPath()+ "/layer-"+node.getLayer()+"[@name=\""+ele.attributeValue( "name" )+"\"]");

                node.setParent(root);
                readTopic(ele, node);
                subs.add(node);
            }
            root.setChildList(subs);
        }
    }

    public static List<TopicTreeEntry> getAllNodes(TopicTreeEntry root) {
        List<TopicTreeEntry> allNodes = new LinkedList<>();
        if (root != null) {
            LinkedList<TopicTreeEntry> queue = new LinkedList<>();
            queue.offerLast(root);
            while (!queue.isEmpty()) {
                root = queue.pollFirst();
                for (TopicTreeEntry entry : root.childList) {
                    queue.offerLast(entry);
                    allNodes.add(entry);
                }
            }
        }
        return allNodes;
    }

    public  List<String> getAllTopics(TopicTreeEntry root) {
        List<String> allTopics = new LinkedList<>();
        if (root != null) {
            LinkedList<TopicTreeEntry> queue = new LinkedList<>();
            queue.offerLast(root);
            while (!queue.isEmpty()) {
                root = queue.pollFirst();
                for (TopicTreeEntry entry : root.childList) {
                    queue.offerLast(entry);
                    allTopics.add(entry.getTopic());
                }
            }
        }
        return allTopics;
    }


    public String getTopicPath(String path) throws DocumentException {
        String result= "/all";
        String[] topics = path.split( "/" );

        for(int i = 1;i<topics.length;i++){
            result += "/layer-"+i+"[@name=\""+topics[i]+"\"]";
            System.out.println(topics[i]);
        }
        return result;
    }

    public void renameTopic(TopicTreeEntry node,String name) throws DocumentException, IOException{
        SAXReader saxReader = new SAXReader();
        Document doc = null;
        doc = saxReader.read( TOPIC );
        Element element = doc.getRootElement();
        Element targetElement = (Element)element.selectSingleNode(node.getPath());
        targetElement.attribute( "name" ).setValue( name );
        XMLWriter xmlWriter = new XMLWriter(new FileWriter(TOPIC)); //dom4j提供了专门写入文件的对象XMLWriter
        xmlWriter.write(doc);
        xmlWriter.close();
        File oldSchemaFile = new File("schema/"+node.getTopic()+".xsd");
        File newSchemaFile = new File("schema/"+name+".xsd");
        oldSchemaFile.renameTo(newSchemaFile);
    }

    public void addTopic(TopicTreeEntry node,String name) throws DocumentException, IOException {
        SAXReader saxReader = new SAXReader();
        Document doc = null;
        doc = saxReader.read( TOPIC );
        Element element = doc.getRootElement();
        Element targetElement = (Element)element.selectSingleNode(node.getPath());
        System.out.println(targetElement);
        String layer = targetElement.getName();
        String newLayer = "layer-"+(Integer.parseInt(layer.split( "-" )[1])+1);
        Element newElement = targetElement.addElement(newLayer);
        newElement.addAttribute( "name",name );
        OutputFormat outputFormat = OutputFormat.createPrettyPrint();
        outputFormat.setIndent(true); //设置是否缩进
        outputFormat.setIndent("    "); //以四个空格方式实现缩进
        outputFormat.setNewlines(true); //设置是否换行
        XMLWriter xmlWriter = new XMLWriter(new FileWriter(TOPIC),outputFormat); //dom4j提供了专门写入文件的对象XMLWriter
        xmlWriter.write(doc);
        xmlWriter.close();
        File SchemaFile = new File("schema/"+name+".xsd");
        SchemaFile.createNewFile();
        XSDUtil util = new XSDUtil();
        util.init( "schema/"+name+".xsd" );
    }

    public void addNewTree(String name) throws DocumentException, IOException {
        SAXReader saxReader = new SAXReader();
        Document doc = null;
        doc = saxReader.read( TOPIC );
        Element element = doc.getRootElement();
        Element newElement=element.addElement( "layer-1" );
        newElement.addAttribute( "name",name );
        OutputFormat outputFormat = OutputFormat.createPrettyPrint();
        outputFormat.setIndent(true); //设置是否缩进
        outputFormat.setIndent("    "); //以四个空格方式实现缩进
        outputFormat.setNewlines(true); //设置是否换行
        XMLWriter xmlWriter = new XMLWriter(new FileWriter(TOPIC),outputFormat); //dom4j提供了专门写入文件的对象XMLWriter
        xmlWriter.write(doc);
        xmlWriter.close();
        File SchemaFile = new File("schema/"+name+".xsd");
        SchemaFile.createNewFile();
        XSDUtil util = new XSDUtil();
        util.init( "schema/"+name+".xsd" );
        System.out.println("更新后的主题树：\n" + this.readAll());
    }

    public void deleteTopic(TopicTreeEntry node) throws DocumentException, IOException {
        SAXReader saxReader = new SAXReader();
        Document doc = null;
        doc = saxReader.read(TOPIC);
        Element element = doc.getRootElement();
        Element targetElement = (Element)element.selectSingleNode(node.getPath());
        targetElement.getParent().remove( targetElement );
        XMLWriter xmlWriter = new XMLWriter(new FileWriter(TOPIC)); //dom4j提供了专门写入文件的对象XMLWriter
        xmlWriter.write(doc);
        xmlWriter.close();
        File SchemaFile = new File("schema/"+node.getTopic()+".xsd");
        SchemaFile.delete();
    }


    public static void main(String[] args) {
//        TopicTreeEntry root = new TopicTreeEntry();
//        List<String> allTopics = new LinkedList<>();
        TopicTreeEntry node = new TopicTreeEntry();
        node.setPath("/all/layer-1[@name=\"test1\"]/layer-2[@name=\"test1-1\"]/layer-3[@name=\"test1-1-1\"]");
        try {
            TopicUtil topicUtil = new TopicUtil();
//            root = topicUtil.readRoot();
//            allTopics = topicUtil.getAllTopics( root );
            topicUtil.addTopic( node,"newTopic" );
//            root = topicUtil.readRoot();
//            System.out.println(allTopics);
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

}