package edu.bupt.wangfu.module.managerMgr.design;

import edu.bupt.wangfu.module.topicMgr.ldap.TopicUtil;
import edu.bupt.wangfu.module.topicTreeMgr.topicTree.TopicTreeEntry;
import org.dom4j.DocumentException;

import javax.naming.NamingException;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.tree.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class TopicTreeUI1 {

    public static JTree LibTreeForSchema = null;
    private static Dimension screenSize;

    static {
        Toolkit kit = Toolkit.getDefaultToolkit();
        screenSize = kit.getScreenSize();
    }

    public TopicUtil topicUtil = null;
    public DefaultMutableTreeNode lib_root = new DefaultMutableTreeNode("root");
    DefaultTreeModel libmodel = new DefaultTreeModel(lib_root);
    JFrame editingJFrame = new JFrame("重命名");
    JFrame addJFrame = new JFrame("添加主题");
    JFrame newTreeFrame = new JFrame("新建主题树");
    ImageIcon addimage = new ImageIcon("./img/icon/add.jpg");
    ImageIcon deleteimage = new ImageIcon("./img/icon/delete.jpg");
    ImageIcon modifyimage = new ImageIcon("./img/icon/modify.jpg");
    ImageIcon newtreeimage = new ImageIcon("./img/icon/newtree.png");
    ImageIcon saveimage = new ImageIcon("./img/icon/save.jpg");
    ImageIcon reflashimage = new ImageIcon("./img/icon/reflash.png");
    ImageIcon treeopenimage = new ImageIcon("./img/icon/open.jpg");
    ImageIcon treecloseimage = new ImageIcon("./img/icon/close.jpg");
    ImageIcon leafimage = new ImageIcon("./img/icon/composite.jpg");
    ImageIcon strategyImage = new ImageIcon("./img/icon/strategy.jpg");
    //ImageIcon schemaimage = new ImageIcon("./img/icon/schema.png");
    private JPanel TTFrame = null;
    private JMenuBar TTMenuBar = null;
    private JToolBar TTToolBar = null;
    private JTree TTTree = null;
    private JTree LibTree = null;
    private DefaultMutableTreeNode root = null;
    private DefaultTreeModel model = null;
    private JPopupMenu popMenu = null;

    private TransformerFactory tFactory;
    private Transformer transformer;
    private DOMSource source;
    private StreamResult result;

    PSManagerUI ui;

    public TopicTreeUI1(PSManagerUI ui) {
        this.ui = ui;
        TTTree = new JTree(model);
        LibTree = new JTree(libmodel);
        LibTreeForSchema = new JTree(libmodel);
        LibTreeForSchema.setAutoscrolls(true);
        LibTree.setAutoscrolls(true);
    }


    private void frame_init() throws NamingException, DocumentException {
        TTFrame.setLayout( new BorderLayout() );
        TTFrame.setSize( new Dimension( 600, 520 ) );
        TTFrame.setPreferredSize( new Dimension( 600, 520 ) );
        TTFrame.setOpaque( false );
        TTFrame.setBounds( screenSize.width / 4, screenSize.height / 8, 1100, 630 );

        tFactory = TransformerFactory.newInstance();
        try {
            transformer = tFactory.newTransformer();
            source = new DOMSource();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }
        topicUtil = new TopicUtil();

        OPFrame_init();//增加、修改主题的对话框初始化
        MenuBar_init();//菜单栏初始化
        ToolBar_init();//工具栏初始化
        PopMenu_init();//右键菜单初始化
        LibTree_init();//左侧树结构初始化
        Database_init();//LDAP数据初始化
        Tree_init();//右侧树结构初始化
    }

    public void Database_init() throws NamingException, DocumentException {
        reload_LibTrees();
    }

    //增加、修改主题的对话框初始化
    private void OPFrame_init() { //增加、修改主题的对话框初始化

        editingJFrame.setBounds(screenSize.width / 2, screenSize.height / 2, 300, 150);
        editingJFrame.setLayout(null);
        final JTextField editArea = new JTextField();
        editArea.setBounds(30, 20, 220, 30);
        editingJFrame.add(editArea);
        JButton editconfirm = new JButton("确认修改");
        editconfirm.setBounds(30, 70, 100, 30);
        editconfirm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                String newName = editArea.getText().trim();
                try {
                    rename_node(newName);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (NamingException e) {
                    e.printStackTrace();
                }
                editingJFrame.setVisible(false);
            }
        });
        JButton editcancel = new JButton("取消修改");
        editcancel.setBounds(150, 70, 100, 30);
        editcancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editingJFrame.setVisible(false);
            }
        });
        editingJFrame.add(editconfirm);
        editingJFrame.add(editcancel);
        editingJFrame.setVisible(false);

        addJFrame.setBounds(screenSize.width / 2, screenSize.height / 2, 300, 150);
        addJFrame.setLayout(null);
        final JTextField addArea = new JTextField();
        addArea.setBounds(30, 20, 220, 30);
        addJFrame.add(addArea);
        JButton addconfirm = new JButton("确认添加");
        addconfirm.setBounds(30, 70, 100, 30);
        addconfirm.addActionListener(new ActionListener() {
            //			@Override
            public void actionPerformed(ActionEvent arg0) {
                String newTopic = addArea.getText().trim();
                try {
                    add_node_to_tree( newTopic );
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (NamingException e) {
                    e.printStackTrace();
                }
                addJFrame.setVisible(false);
//                ui.reflashJtreeRoot();
            }
        });
        JButton addcancel = new JButton("取消添加");
        addcancel.setBounds(150, 70, 100, 30);
        addcancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addJFrame.setVisible(false);
            }
        });
        addJFrame.add(addconfirm);
        addJFrame.add(addcancel);
        addJFrame.setVisible(false);

        newTreeFrame.setBounds(screenSize.width / 2, screenSize.height / 2,300, 150);
        newTreeFrame.setLayout(null);
        final JTextField newtreeArea = new JTextField();
        newtreeArea.setBounds(30, 20, 220, 30);
        newTreeFrame.add(newtreeArea);
        JButton newconfirm = new JButton("确认新建");
        newconfirm.setBounds(30, 70, 100, 30);
        newconfirm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (!newtreeArea.getText().trim().equals("")) {
                    try {
                        new_tree(newtreeArea.getText().trim());
                    } catch (NamingException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    newTreeFrame.setVisible(false);
                }
            }
        });
        JButton newcancel = new JButton("取消新建");
        newcancel.setBounds(150, 70, 100, 30);
        newcancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newTreeFrame.setVisible(false);
            }
        });
        newTreeFrame.add(newconfirm);
        newTreeFrame.add(newcancel);
        newTreeFrame.setVisible(false);
    }

    //右键菜单初始化
    private void PopMenu_init() {
        popMenu = new JPopupMenu();
        JMenuItem addItem = new JMenuItem("新建", addimage);
        addItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addJFrame.setVisible(true);
            }
        });
        JMenuItem delItem = new JMenuItem("删除", deleteimage);
        delItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    delete_node_from_tree();
                } catch (NamingException e1) {
                    e1.printStackTrace();
                } catch (DocumentException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        JMenuItem editItem = new JMenuItem("修改", modifyimage);
        editItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editingJFrame.setVisible( true );
            }
        });

        JMenuItem strategyItem = new JMenuItem("策略", strategyImage);
        strategyItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DefaultMutableTreeNode temp = (DefaultMutableTreeNode) TTTree.getLastSelectedPathComponent();
                JComboBox[] topicComboBox = {ui.comboBox, ui.comboBox_1, ui.comboBox_2, ui.comboBox_3, ui.comboBox_4, ui.comboBox_5};
                int rightLevel = 0;
                if (temp != null) rightLevel = temp.getLevel();
                int leftLevel = ((DefaultMutableTreeNode) LibTree.getLastSelectedPathComponent()).getLevel();
                int currentlevel = leftLevel + rightLevel;
                int[] indexs = new int[currentlevel];

                if (temp == null) {
                    temp = (DefaultMutableTreeNode) LibTree.getLastSelectedPathComponent();
                    for (int j = 0; j < leftLevel; j++) {

                        if (!temp.equals(lib_root)) {
                            int tempIndex = temp.getParent().getIndex(temp);
                            indexs[leftLevel - 1 - j] = tempIndex + 1;
                            temp = (DefaultMutableTreeNode) temp.getParent();
                        }
                    }
                } else {
                    for (int i = 0; i < rightLevel; i++) {
                        if (temp.getParent() != null) {
                            int tempIndex = temp.getParent().getIndex(temp);
                            indexs[currentlevel - 1 - i] = tempIndex + 1;
                            temp = (DefaultMutableTreeNode) temp.getParent();
                        }
                    }
                    temp = (DefaultMutableTreeNode) LibTree.getLastSelectedPathComponent();
                    for (int j = 0; j < leftLevel; j++) {

                        if (!temp.equals(lib_root)) {
                            int tempIndex = temp.getParent().getIndex(temp);
                            indexs[leftLevel - 1 - j] = tempIndex + 1;
                            temp = (DefaultMutableTreeNode) temp.getParent();
                        }
                    }
                }
                for (int k = 0; k < indexs.length; k++) {

                    topicComboBox[k].setSelectedIndex(indexs[k]);
                }
                Hashtable<Integer, DefaultMutableTreeNode> selectedTopicPath = new Hashtable<>();
                do {
                    selectedTopicPath.put(temp.getLevel(), temp);
                    temp = (DefaultMutableTreeNode) temp.getParent();
                } while (temp != null && !temp.equals(lib_root));
                if (temp != null)
                    System.out.println("temp.toString()" + temp.toString());
                else {
                    System.out.println("selectedTopicPath" + selectedTopicPath);
                    temp = (DefaultMutableTreeNode) LibTree.getLastSelectedPathComponent();
                    temp = (DefaultMutableTreeNode) temp.getParent();
                    do {
                        if (!selectedTopicPath.contains(temp) && !temp.toString().equals("all"))
                            selectedTopicPath.put(temp.getLevel(), temp);
                        temp = (DefaultMutableTreeNode) temp.getParent();
                    } while (temp != null && !temp.equals(lib_root));
                    if (temp != null)
                        System.out.println("temp.toString()" + temp.toString());
                    System.out.println("selectedTopicPath" + selectedTopicPath);
                }
                ui.reflashFbdnGroups();
                ui.visualManagement.setSelectedIndex(3);
            }

        });

        popMenu.add(addItem);
        popMenu.add(delItem);
        popMenu.add(editItem);
        popMenu.add(strategyItem);
    }

    //菜单栏初始化
    private void MenuBar_init() {
        TTMenuBar = new JMenuBar();
        JMenu file = new JMenu("菜单");

        JMenuItem open_menu = new JMenuItem("打开", newtreeimage);
        file.add(open_menu);

        JMenuItem save_menu = new JMenuItem("保存", saveimage);
        file.add(save_menu);

        JMenu tree = new JMenu("主题树");

        JMenuItem add_menu = new JMenuItem("新建", addimage);
        add_menu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addJFrame.setVisible(true);
            }
        });
        tree.add(add_menu);

        JMenuItem delete_menu = new JMenuItem("删除", deleteimage);
        delete_menu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    delete_node_from_tree();
                } catch (NamingException e1) {
                    e1.printStackTrace();
                } catch (DocumentException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        tree.add(delete_menu);

        JMenuItem modify_menu = new JMenuItem("修改", modifyimage);
        modify_menu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editingJFrame.setVisible( true );
            }
        });
        tree.add(modify_menu);

        TTMenuBar.add(file);
        TTMenuBar.add(tree);
    }

    //工具栏初始化
    private void ToolBar_init()  {
        TTToolBar = new JToolBar(SwingConstants.HORIZONTAL);
        TTToolBar.setSize(1200, 30);

        JButton open_button = new JButton(newtreeimage);
        open_button.setToolTipText("新建主题树");
        open_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newTreeFrame.setVisible(true);
            }
        });
        TTToolBar.add(open_button);

        JButton add_button = new JButton(addimage);
        add_button.setToolTipText("新建主题");
        add_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addJFrame.setVisible(true);
            }
        });
        TTToolBar.add(add_button);

        JButton delete_button = new JButton(deleteimage);
        delete_button.setToolTipText("删除主题");
        delete_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    delete_node_from_tree();
                } catch (NamingException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (DocumentException e1) {
                    e1.printStackTrace();
                }
            }
        });
        TTToolBar.add(delete_button);

        JButton modify_button = new JButton(modifyimage);
        modify_button.setToolTipText("修改主题");
        modify_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editingJFrame.setVisible(true);
            }
        });
        TTToolBar.add(modify_button);

        JButton save_button = new JButton(saveimage);
        save_button.setToolTipText("保存主题树");
        save_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //topic_counter = 0;
                Enumeration<DefaultMutableTreeNode> children = root.children();
                while (children.hasMoreElements()) {
                    DefaultMutableTreeNode child = children.nextElement();
                    save_tree(child, "");
                }
            }
        });
        TTToolBar.add(save_button);

        JButton close_button = new JButton(reflashimage);
        close_button.setToolTipText("刷新主题树");
        close_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                try {
////                    ui.topicTreeUI.lu = new LdapUtil();
////                    ui.topicTreeUI.lu.connectLdap();
////                    ui.topicTreeUI1.reload_LibTrees();
//                    PSManagerUI.topicTreeM.invalidate();
//                } catch (NamingException e1) {
//
//                    e1.printStackTrace();
//                } catch (DocumentException e1) {
//                    e1.printStackTrace();
//                }
            }
        });
        TTToolBar.add(close_button);
        TTFrame.add(BorderLayout.NORTH, TTToolBar);
    }

    private void Tree_init() {
        root = new DefaultMutableTreeNode("root");
        model = new DefaultTreeModel(root);
        TTTree.setModel(model);
        TTTree.setSize(440, 500);
        TTTree.setRootVisible(false);
        TTTree.getSelectionModel().setSelectionMode(TreeSelectionModel.CONTIGUOUS_TREE_SELECTION);
        TTTree.setEditable(false);
        TTTree.setDragEnabled(true);
        TTTree.setDropMode(DropMode.ON_OR_INSERT);
        TTTree.setShowsRootHandles(false);

        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        renderer.setClosedIcon(treecloseimage);
        renderer.setOpenIcon(treeopenimage);
        renderer.setLeafIcon(leafimage);
        TTTree.setCellRenderer(renderer);

//        try {
//            TTTree.setTransferHandler(new TreeTransferHandler(lu, LibTree, TTTree));
//        } catch (ClassNotFoundException e) {
//
//            e.printStackTrace();
//        }

        TTTree.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                if (evt.getButton() == 3) {
                    TreePath path = TTTree.getPathForLocation(evt.getX(), evt.getY());
                    TTTree.setSelectionPath(path);
                    popMenu.show(TTTree, evt.getX(), evt.getY());
                }
            }
        });

        JScrollPane TreePane = new JScrollPane(TTTree);
        TreePane.setBounds(250, 30, 440, 500);
        Border title1 = BorderFactory.createTitledBorder("主题");
        TreePane.setBorder(title1);
        TreePane.setOpaque(false);
        TTFrame.add(BorderLayout.CENTER, TreePane);
    }

    private void LibTree_init() {
        LibTree.setSize(250, 500);
        LibTree.setRootVisible(false);
        LibTree.setEditable(false);
        LibTree.setDragEnabled(true);
        LibTree.setDropMode(DropMode.ON_OR_INSERT);
        LibTree.getSelectionModel().setSelectionMode(TreeSelectionModel.CONTIGUOUS_TREE_SELECTION);
//        try {
//            LibTree.setTransferHandler(new TreeTransferHandler(lu, LibTree, TTTree));
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }

        LibTree.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                if (evt.getButton() == 1) {
                    TreePath path = LibTree.getPathForLocation( evt.getX(), evt.getY() );
                    if (path != null) {
                        LibTree.setSelectionPath( path );
                        DefaultMutableTreeNode chosen_node = (DefaultMutableTreeNode) LibTree.getLastSelectedPathComponent();
                        System.out.println( "chosen_node  " + chosen_node );
                        TopicTreeEntry tempentry = (TopicTreeEntry) chosen_node.getUserObject();
                        if (tempentry != null) {
                            root.removeAllChildren();
                            root.setUserObject( tempentry );
                            TTTree.setRootVisible( false );
                            try {
                                reload_TTTree( root );
                            } catch (NamingException e) {
                                e.printStackTrace();
                            }
                            LibTree.setSelectionPath(path);
                        } else {
                            JOptionPane.showMessageDialog(null, "神马玩意？");
                        }
                    }
                }
            }
        });

        LibTree.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                if (evt.getButton() == 3) {
                    TreePath path = LibTree.getPathForLocation(evt.getX(), evt.getY());
                    if (path != null) {
                        LibTree.setSelectionPath(path);
                        DefaultMutableTreeNode chosen_node = (DefaultMutableTreeNode) LibTree.getLastSelectedPathComponent();
                        System.out.println("chosen_node  " + chosen_node);
                        TopicTreeEntry tempentry = (TopicTreeEntry) chosen_node.getUserObject();
                        if (tempentry != null) {
                            root.removeAllChildren();
                            root.setUserObject(tempentry);
                            TTTree.setRootVisible(false);
                            try {
                                reload_TTTree(root);
                            } catch (NamingException e) {
                                e.printStackTrace();
                            }
                            LibTree.setSelectionPath(path);
                        } else {
                            JOptionPane.showMessageDialog(null, "");
                        }
                    }
                    TreePath path2 = LibTree.getPathForLocation(evt.getX(), evt.getY());
                    LibTree.setSelectionPath(path2);
                    popMenu.show(LibTree, evt.getX(), evt.getY());
                }
            }
        });
        JScrollPane TreePane = new JScrollPane(LibTree);
        TreePane.setBounds(30, 30, 300, 500);
        TreePane.setPreferredSize(new Dimension(150, 500));
        Border title1 = BorderFactory.createTitledBorder("主题树");
        TreePane.setBorder(title1);
        TreePane.setOpaque(false);
        TTFrame.add(BorderLayout.WEST, TreePane);

        LibTreeForSchema.setSize(250, 500);
        LibTreeForSchema.setRootVisible(false);
        LibTreeForSchema.setEditable(false);
        LibTreeForSchema.setDragEnabled(false);
        LibTreeForSchema.setDropMode(DropMode.ON_OR_INSERT);
        LibTreeForSchema.setToggleClickCount(2);
        LibTreeForSchema.getSelectionModel().setSelectionMode(TreeSelectionModel.CONTIGUOUS_TREE_SELECTION);
//        try {
//            LibTreeForSchema.setTransferHandler(new TreeTransferHandler(lu, LibTree, TTTree));
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
        LibTreeForSchema.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()==1){//点击几次，这里是双击事件

                    TreePath path = LibTreeForSchema.getPathForLocation(e.getX(), e.getY());
                    LibTreeForSchema.setSelectionPath(path);
                    DefaultMutableTreeNode chosenSchema = (DefaultMutableTreeNode)LibTreeForSchema.getLastSelectedPathComponent();
                    if(path!=null&&chosenSchema.isLeaf())
                    {
                        File clickSchemaFile = new File("schema/"+chosenSchema.toString()+".xsd");
                        if(clickSchemaFile.exists()){
                            try {
                                ui.schemaUI.xsd = "schema/"+chosenSchema.toString()+".xsd";
                                ui.schemaUI.reload_Schema( "schema/"+chosenSchema.toString()+".xsd");
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                        }else
                            JOptionPane.showMessageDialog(null, "没有该主题的schema： "+chosenSchema.toString());
                    }

                }
            }

            public void mousePressed(MouseEvent evt) {

            }
        });
    }

    public void reload_LibTrees() throws NamingException, DocumentException {

        TopicTreeEntry libentry = new TopicTreeEntry();
        libentry = topicUtil.readRoot();
        lib_root.setUserObject(libentry);

        lib_root.removeAllChildren();
        setTTTree(lib_root);
        libmodel.reload(lib_root);
        LibTree.setModel(libmodel);
        LibTreeForSchema.setModel(libmodel);
        LibTreeForSchema.updateUI();
        LibTree.updateUI();
    }

    private void reload_TTTree(DefaultMutableTreeNode rootnode) throws NamingException {
        TopicTreeEntry rootentry = (TopicTreeEntry) rootnode.getUserObject();
        root.setUserObject(rootentry);
        root.removeAllChildren();
        setTTTree(root);

        model.reload(root);
        TTTree.setModel(model);
        TTTree.updateUI();
    }

    protected void setTTTree(DefaultMutableTreeNode tempnode) throws NamingException {
        List<TopicTreeEntry> topics ;
        TopicTreeEntry tempentry = (TopicTreeEntry) tempnode.getUserObject();
        topics = tempentry.getChildList();
        if(topics != null){
            for (TopicTreeEntry topic : topics) {
                DefaultMutableTreeNode newnode = new DefaultMutableTreeNode();
                newnode.setUserObject(topic);
                setTTTree(newnode);
                tempnode.add(newnode);
            }
        }
    }

    protected void rename_node(String newName) throws IOException, DocumentException, NamingException {
        JTree tempTree = TTTree;
        DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) TTTree.getLastSelectedPathComponent();
        if (treeNode == null) {
            treeNode = (DefaultMutableTreeNode) LibTree.getLastSelectedPathComponent();
            tempTree = LibTree;
        }
        if (treeNode != null && !newName.equals("")) {
            TopicTreeEntry tempentry = (TopicTreeEntry) treeNode.getUserObject();
            topicUtil.renameTopic(tempentry, newName);
            reload_LibTrees();
            treeNode.setUserObject(tempentry);
            if (tempTree == TTTree) {
                reload_TTTree(treeNode);
            }
        }
        ui.reflashJtreeRoot();
    }

    protected void delete_node_from_tree() throws NamingException, IOException, DocumentException {
        JTree temptree = TTTree;
        DefaultMutableTreeNode temp_node = (DefaultMutableTreeNode) TTTree.getLastSelectedPathComponent();
        if (temp_node == null) {
            temp_node = (DefaultMutableTreeNode) LibTree.getLastSelectedPathComponent();
            temptree = LibTree;
        }
        if (temp_node == null)
            return;
        TopicTreeEntry tempEntry = (TopicTreeEntry) temp_node.getUserObject();
        topicUtil.deleteTopic(tempEntry);
        reload_LibTrees();
        temp_node.setUserObject(tempEntry);
        if (temptree == TTTree) {
            reload_TTTree(temp_node);
        }
        //下发新的主题树
        ui.reflashJtreeRoot();
    }

    protected void add_node_to_tree(String name) throws IOException, DocumentException, NamingException {
        JTree tempTree = TTTree;
        DefaultMutableTreeNode tempNode = (DefaultMutableTreeNode) TTTree.getLastSelectedPathComponent();
        if (tempNode == null) {
            tempTree = LibTree;
            tempNode = (DefaultMutableTreeNode) LibTree.getLastSelectedPathComponent();
        }
        TopicTreeEntry temptopic = (TopicTreeEntry) tempNode.getUserObject();
        topicUtil.addTopic( temptopic, name);
        reload_LibTrees();
        tempNode.setUserObject(temptopic);
        if (tempTree == TTTree) {
            reload_TTTree(tempNode);

        }
        ui.reflashJtreeRoot();
    }

    protected void new_tree(String newTreeName) throws NamingException, IOException, DocumentException {
        topicUtil.addNewTree(newTreeName);
        reload_LibTrees();
        ui.reflashJtreeRoot();
    }

    protected void save_tree(DefaultMutableTreeNode current_node, String current_code) {
        JOptionPane.showMessageDialog(null, "保存成功");
    }

    public JPanel getTreeInstance() throws NamingException, DocumentException {
        if (TTFrame == null) {
            TTFrame = new JPanel();
            frame_init();
        }
        return TTFrame;
    }
}

