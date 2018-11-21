package edu.bupt.wangfu.module.managerMgr.design;

import edu.bupt.wangfu.module.managerMgr.schemaMgr.XSDNode;
import edu.bupt.wangfu.module.managerMgr.schemaMgr.XSDUtil;

import javax.naming.NamingException;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.tree.*;
import javax.xml.transform.TransformerFactory;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Hashtable;
import java.util.List;

import static edu.bupt.wangfu.module.managerMgr.design.PSManagerUI.screenSize;

public class SchemaUI {
    XSDUtil xsdUtil;
    private JPanel STFrame = null;
    private JTree SchemaTree = null;
    private DefaultMutableTreeNode root = null;
    private DefaultTreeModel model = null;
    public DefaultMutableTreeNode schema_root = new DefaultMutableTreeNode("root");
    DefaultTreeModel libmodel = new DefaultTreeModel(schema_root);
    JFrame editingJFrame = new JFrame("重命名");
    JFrame addJFrame = new JFrame("修改属性");
    JFrame newTagFrame = new JFrame("新建标签");
    ImageIcon addimage = new ImageIcon("./img/icon/add.jpg");
    ImageIcon deleteimage = new ImageIcon("./img/icon/delete.jpg");
    ImageIcon modifyimage = new ImageIcon("./img/icon/modify.jpg");
    ImageIcon newTagimage = new ImageIcon("./img/icon/newtree.png");
    ImageIcon saveimage = new ImageIcon("./img/icon/save.jpg");
    ImageIcon reflashimage = new ImageIcon("./img/icon/reflash.png");
    ImageIcon treeopenimage = new ImageIcon("./img/icon/open.jpg");
    ImageIcon treecloseimage = new ImageIcon("./img/icon/close.jpg");
    ImageIcon leafimage = new ImageIcon("./img/icon/composite.jpg");
    //ImageIcon schemaimage = new ImageIcon("./img/icon/schema.png");

    private JMenuBar STMenuBar = null;
    private JToolBar STToolBar = null;
    private JPopupMenu popMenu = null;
    //private TransformerFactory tFactory;
    PSManagerUI ui;

    public SchemaUI(PSManagerUI ui) {
        this.ui = ui;
        SchemaTree = new JTree(libmodel);
        SchemaTree.setAutoscrolls(true);
    }

    private void frame_init(){
        STFrame.setLayout(new BorderLayout());
        STFrame.setSize(new Dimension(600, 520));
        STFrame.setPreferredSize(new Dimension(600, 520));
        STFrame.setOpaque(false);
        STFrame.setBounds(screenSize.width / 4, screenSize.height / 8, 1100, 630);

        OPFrame_init();//增加、修改主题的对话框初始化
        MenuBar_init();//菜单栏初始化
        ToolBar_init();//工具栏初始化
        PopMenu_init();//右键菜单初始化
        Schema_init();//右侧树结构初始化
    }

    private void Schema_init(){
        SchemaTree.setModel(model);
        SchemaTree.setSize(440, 500);
        SchemaTree.setRootVisible(true);
        SchemaTree.getSelectionModel().setSelectionMode(TreeSelectionModel.CONTIGUOUS_TREE_SELECTION);
        SchemaTree.setEditable(false);
        SchemaTree.setDragEnabled(false);
        SchemaTree.setDropMode(DropMode.ON_OR_INSERT);
        SchemaTree.setShowsRootHandles(false);

        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        renderer.setClosedIcon(treecloseimage);
        renderer.setOpenIcon(treeopenimage);
        renderer.setLeafIcon(leafimage);
        SchemaTree.setCellRenderer(renderer);

//        try {
//            SchemaTree.setTransferHandler(new TreeTransferHandler(lu, LibTree, TTTree));
//        } catch (ClassNotFoundException e) {
//
//            e.printStackTrace();
//        }

        SchemaTree.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
//                if (evt.getButton() == 3) {
//                    TreePath path = TTTree.getPathForLocation(evt.getX(), evt.getY());
//                    TTTree.setSelectionPath(path);
//                    popMenu.show(TTTree, evt.getX(), evt.getY());
//                }
            }
        });

        JScrollPane TreePane = new JScrollPane(SchemaTree);
        TreePane.setBounds(250, 30, 440, 500);
        Border title1 = BorderFactory.createTitledBorder("schema");
        TreePane.setBorder(title1);
        TreePane.setOpaque(false);
        STFrame.add(BorderLayout.CENTER, TreePane);

        JScrollPane TreeForSchemaPane = new JScrollPane(ui.topicTreeUI.LibTreeForSchema);
        TreeForSchemaPane.setBounds(30, 30, 300, 500);
        TreeForSchemaPane.setPreferredSize(new Dimension(150, 500));
        Border title = BorderFactory.createTitledBorder("主题树");
        TreeForSchemaPane.setBorder(title);
        TreeForSchemaPane.setOpaque(false);
        STFrame.add(BorderLayout.WEST, TreeForSchemaPane);
    }

    public void reload_Schema(String xsd) throws Exception {

        XSDNode libNode = new XSDNode();
        xsdUtil = new XSDUtil();
        libNode = xsdUtil.connectXSD(xsd);
        schema_root.setUserObject(libNode);

        schema_root.removeAllChildren();
        setSTree(schema_root);
        libmodel.reload(schema_root);
        SchemaTree.setModel(libmodel);
        SchemaTree.updateUI();
    }

    protected void setSTree(DefaultMutableTreeNode tempTreeNode) throws NamingException {
        XSDNode node = (XSDNode)tempTreeNode.getUserObject();
        List<XSDNode> nodes =node.getSubXSDNode();
        if(nodes != null){
            for (XSDNode tempNode : nodes) {
                DefaultMutableTreeNode newnode = new DefaultMutableTreeNode();
                newnode.setUserObject(tempNode);
                setSTree(newnode);
                tempTreeNode.add(newnode);
            }
        }

    }

    private void MenuBar_init() {
        STMenuBar = new JMenuBar();
        JMenu file = new JMenu("菜单");

        JMenuItem open_menu = new JMenuItem("打开", newTagimage);
        file.add(open_menu);

        JMenuItem save_menu = new JMenuItem("保存", saveimage);
        file.add(save_menu);

        JMenu tree = new JMenu("schema");

        JMenuItem add_menu = new JMenuItem("新建标签", addimage);
        add_menu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        tree.add(add_menu);

        JMenuItem delete_menu = new JMenuItem("删除标签", deleteimage);
        delete_menu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        tree.add(delete_menu);

        JMenuItem modify_menu = new JMenuItem("修改属性", modifyimage);
        modify_menu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        tree.add(modify_menu);

        STMenuBar.add(file);
        STMenuBar.add(tree);
    }

    private void ToolBar_init() {
        STToolBar = new JToolBar(SwingConstants.HORIZONTAL);
        STToolBar.setSize(1200, 30);

        JButton open_button = new JButton(newTagimage);
        open_button.setToolTipText("新建标签");
        open_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newTagFrame.setVisible(true);
            }
        });
        STToolBar.add(open_button);

//        JButton add_button = new JButton(addimage);
//        add_button.setToolTipText("新建主题");
//        add_button.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//            }
//        });
//        STToolBar.add(add_button);

        JButton delete_button = new JButton(deleteimage);
        delete_button.setToolTipText("删除标签");
        delete_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        STToolBar.add(delete_button);

        JButton modify_button = new JButton(modifyimage);
        modify_button.setToolTipText("修改属性");
        modify_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        STToolBar.add(modify_button);

        JButton save_button = new JButton(saveimage);
        save_button.setToolTipText("保存schema");
        save_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        STToolBar.add(save_button);

        JButton close_button = new JButton(reflashimage);
        close_button.setToolTipText("刷新schema");
        close_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                try {
//                    //ui.topicTreeUI.reload_LibTrees();
//                    //PSManagerUI.topicTreeM.invalidate();
//                } catch (NamingException e1) {
//
//                    e1.printStackTrace();
//                }
            }
        });
        STToolBar.add(close_button);
        STFrame.add(BorderLayout.NORTH, STToolBar);
    }

    private void OPFrame_init() { //增加、修改主题的对话框初始化

        editingJFrame.setBounds( screenSize.width / 2, screenSize.height / 2, 300, 150 );
        editingJFrame.setLayout( null );
        final JTextField editArea = new JTextField();
        editArea.setBounds( 30, 20, 220, 30 );
        editingJFrame.add( editArea );
        JButton editconfirm = new JButton( "确认修改" );
        editconfirm.setBounds( 30, 70, 100, 30 );
        editconfirm.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            }
        } );
        JButton editcancel = new JButton( "取消修改" );
        editcancel.setBounds( 150, 70, 100, 30 );
        editcancel.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editingJFrame.setVisible( false );
            }
        } );
        editingJFrame.add( editconfirm );
        editingJFrame.add( editcancel );
        editingJFrame.setVisible( false );

        addJFrame.setBounds( screenSize.width / 2, screenSize.height / 2, 300, 150 );
        addJFrame.setLayout( null );
        final JTextField addArea = new JTextField();
        addArea.setBounds( 30, 20, 220, 30 );
        addJFrame.add( addArea );
        JButton addconfirm = new JButton( "确认添加" );
        addconfirm.setBounds( 30, 70, 100, 30 );
        addconfirm.addActionListener( new ActionListener() {
            //			@Override
            public void actionPerformed(ActionEvent arg0) {

            }
        } );
        JButton addcancel = new JButton( "取消添加" );
        addcancel.setBounds( 150, 70, 100, 30 );
        addcancel.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addJFrame.setVisible( false );
            }
        } );
        addJFrame.add( addconfirm );
        addJFrame.add( addcancel );
        addJFrame.setVisible( false );

        newTagFrame.setBounds( screenSize.width / 2, screenSize.height / 2, 300, 150 );
        newTagFrame.setLayout( null );
        final JTextField newtreeArea = new JTextField();
        newtreeArea.setBounds( 30, 20, 220, 30 );
        newTagFrame.add( newtreeArea );
        JButton newconfirm = new JButton( "确认新建" );
        newconfirm.setBounds( 30, 70, 100, 30 );
        newconfirm.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            }
        } );
        JButton newcancel = new JButton( "取消新建" );
        newcancel.setBounds( 150, 70, 100, 30 );
        newcancel.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newTagFrame.setVisible( false );
            }
        } );
        newTagFrame.add( newconfirm );
        newTagFrame.add( newcancel );
        newTagFrame.setVisible( false );
    }

    private void PopMenu_init() {
        popMenu = new JPopupMenu();
        JMenuItem addItem = new JMenuItem("新建标签", newTagimage);
        addItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        JMenuItem delItem = new JMenuItem("删除标签", deleteimage);
        delItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        JMenuItem editItem = new JMenuItem("重命名", modifyimage);
        editItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        JMenuItem editAttrItem = new JMenuItem("修改属性", addimage);
        editItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        popMenu.add(addItem);
        popMenu.add(delItem);
        popMenu.add(editItem);
        popMenu.add(editAttrItem);
    }

    public JPanel getTreeInstance() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException, NamingException {
        if (STFrame == null) {
            STFrame = new JPanel();
            frame_init();
        }
        return STFrame;
    }

}
