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

public class TopicUtil {
    private static final String TOPIC = "./topicMsg.xml";

    public TopicTreeEntry readRoot() throws DocumentException {
        TopicTreeEntry root = new TopicTreeEntry();
        SAXReader saxReader = new SAXReader();
        Document doc = null;
        doc = saxReader.read( TOPIC );
        Element element = doc.getRootElement();
        root.setTopic(element.getQualifiedName());
        root.setPath(element.getQualifiedName());
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
                node.setTopic(ele.getQualifiedName());
                node.setPath(root.getPath()+"/"+ele.getQualifiedName());
                node.setParent(root);
                readTopic(ele, node);
                subs.add(node);
            }
            root.setChildList(subs);
        }
    }

    public void renameTopic(TopicTreeEntry node,String name) throws DocumentException, IOException{
        String[] paths = node .getPath().split( "/" );
        SAXReader saxReader = new SAXReader();
        Document doc = null;
        doc = saxReader.read( TOPIC );
        Element element = doc.getRootElement();
        for(int i = 1;i<paths.length;i++){
            element = element.element(paths[i]);
        }
        element.setName(name);
        XMLWriter xmlWriter = new XMLWriter(new FileWriter(TOPIC)); //dom4j提供了专门写入文件的对象XMLWriter
        xmlWriter.write(doc);
        xmlWriter.close();
    }

    public void addTopic(TopicTreeEntry node,String name) throws DocumentException, IOException {
        String[] paths = node .getPath().split( "/" );
        SAXReader saxReader = new SAXReader();
        Document doc = null;
        doc = saxReader.read( TOPIC );
        Element element = doc.getRootElement();
        for(int i = 1;i<paths.length;i++){
            element = element.element(paths[i]);
        }
        element.addElement( name );
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
        element.addElement( name );
        OutputFormat outputFormat = OutputFormat.createPrettyPrint();
        outputFormat.setIndent(true); //设置是否缩进
        outputFormat.setIndent("    "); //以四个空格方式实现缩进
        outputFormat.setNewlines(true); //设置是否换行
        XMLWriter xmlWriter = new XMLWriter(new FileWriter(TOPIC),outputFormat); //dom4j提供了专门写入文件的对象XMLWriter
        xmlWriter.write(doc);
        xmlWriter.close();
    }

    public void deleteTopic(TopicTreeEntry node) throws DocumentException, IOException {
        String[] paths = node .getPath().split( "/" );
        SAXReader saxReader = new SAXReader();
        Document doc = null;
        doc = saxReader.read(TOPIC);
        Element element = doc.getRootElement();
        Element prevent = null;
        for(int i = 1;i<paths.length;i++){
            prevent= element;
            element = element.element(paths[i]);
        }
        prevent.remove( element );
        XMLWriter xmlWriter = new XMLWriter(new FileWriter(TOPIC)); //dom4j提供了专门写入文件的对象XMLWriter
        xmlWriter.write(doc);
        xmlWriter.close();

    }


    public static void main(String[] args) {
        TopicTreeEntry root = new TopicTreeEntry();
        TopicTreeEntry node = new TopicTreeEntry();
        node.setPath("all/test3");
        try {
            TopicUtil topicUtil = new TopicUtil();
            topicUtil.renameTopic( node,"test4" );
            root = topicUtil.readRoot();
            System.out.println(root);
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

}
