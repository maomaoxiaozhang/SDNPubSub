package edu.bupt.wangfu.module.topicMgr.ldap;


import edu.bupt.wangfu.module.topicTreeMgr.topicTree.TopicTreeEntry;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

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
        root.setPath(element.getPath());
        readTopic(element,root);
        return root;
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

    public void readTopic(Element element,TopicTreeEntry root){
        Iterator<Element> subElement = element.elementIterator();
        if(subElement.hasNext()) {
            ArrayList<TopicTreeEntry> subs = new ArrayList<>();
            while (subElement.hasNext()) {
                Element ele = (Element) subElement.next();
                TopicTreeEntry node = new TopicTreeEntry();
                node.setTopic(ele.attributeValue( "name" ));
                node.setPath(ele.getPath());
                node.setLayer(root.getLayer()+1);
                node.setParent(root);
                readTopic(ele, node);
                subs.add(node);
            }
            root.setChildList(subs);
        }
    }
    public void searchTopic(TopicTreeEntry node) throws DocumentException {
        SAXReader saxReader = new SAXReader();
        Document doc = null;
        doc = saxReader.read(TOPIC);
        Element element = doc.getRootElement();
        System.out.println(element.getPath());
        Element targetElement = (Element)element.selectSingleNode(node.getPath());
        System.out.println( targetElement.toString() );
//        targetElement.getParent().remove( targetElement );
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
    }

    public void addTopic(TopicTreeEntry node,String name) throws DocumentException, IOException {
        SAXReader saxReader = new SAXReader();
        Document doc = null;
        doc = saxReader.read( TOPIC );
        Element element = doc.getRootElement();
        Element targetElement = (Element)element.selectSingleNode(node.getPath());
        String layer = targetElement.getQualifiedName();
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
    }


    public static void main(String[] args) {
        TopicTreeEntry root = new TopicTreeEntry();
        TopicTreeEntry node = new TopicTreeEntry();
        node.setPath("/all/layer-1[@name=\"test1\"]/layer-2[@name=\"test1-1\"]/layer-3[@name=\"test1-1-1\"]");
        try {
            TopicUtil topicUtil = new TopicUtil();
            topicUtil.addTopic( node,"newnew" );
//            topicUtil.renameTopic( node,"test4" );
//            root = topicUtil.readRoot();
//            System.out.println(root);
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

}
