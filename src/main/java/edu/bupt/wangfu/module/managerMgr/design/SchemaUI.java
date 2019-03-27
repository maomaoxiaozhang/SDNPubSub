package edu.bupt.wangfu.module.managerMgr.design;

import edu.bupt.wangfu.module.managerMgr.schemaMgr.XSDNode;
import edu.bupt.wangfu.module.managerMgr.schemaMgr.XSDUtil;
import org.dom4j.DocumentException;

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
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;

import static edu.bupt.wangfu.module.managerMgr.design.PSManagerUI.screenSize;

public class SchemaUI {
    XSDUtil xsdUtil;
    public String xsd;
    private JPanel STFrame = null;
    private JTree SchemaTree = null;
    private DefaultMutableTreeNode root = null;
    private DefaultTreeModel model = null;
    public DefaultMutableTreeNode schema_root = new DefaultMutableTreeNode("root");
    DefaultTreeModel libmodel = new DefaultTreeModel(schema_root);
    JFrame modifyJFrame = new JFrame("修改属性");
    JFrame newTagFrame = new JFrame("新建标签");
    ImageIcon addimage = new ImageIcon("./img/icon/add.jpg");
    ImageIcon deleteimage = new ImageIcon("./img/icon/delete.jpg");
    ImageIcon modifyimage = new ImageIcon("./img/icon/modify.jpg");
    ImageIcon saveimage = new ImageIcon("./img/icon/save.jpg");
    ImageIcon reflashimage = new ImageIcon("./img/icon/reflash.png");
    ImageIcon treeopenimage = new ImageIcon("./img/icon/open.jpg");
    ImageIcon treecloseimage = new ImageIcon("./img/icon/close.jpg");
    ImageIcon leafimage = new ImageIcon("./img/icon/composite.jpg");

    private JMenuBar STMenuBar = null;
    private JToolBar STToolBar = null;
    private JPopupMenu popMenu = null;
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

        JScrollPane TreePane = new JScrollPane(SchemaTree);
        TreePane.setBounds(250, 30, 440, 500);
        Border title1 = BorderFactory.createTitledBorder("schema");
        TreePane.setBorder(title1);
        TreePane.setOpaque(false);
        STFrame.add(BorderLayout.CENTER, TreePane);

        JScrollPane TreeForSchemaPane = new JScrollPane(ui.topicTreeUI1.LibTreeForSchema);
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
        libNode = xsdUtil.readXSDRoot( xsd );
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


        JMenuItem save_menu = new JMenuItem("保存", saveimage);
        file.add(save_menu);

        JMenu tree = new JMenu("schema");

        JMenuItem add_menu = new JMenuItem("新建标签", addimage);
        add_menu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newTagFrame.setVisible( true );
            }
        });
        tree.add(add_menu);

        JMenuItem delete_menu = new JMenuItem("删除标签", deleteimage);
        delete_menu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    delete_node_from_tree();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        tree.add(delete_menu);

        JMenuItem modify_menu = new JMenuItem("修改属性", modifyimage);
        modify_menu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                modifyJFrame.setVisible( true );
            }
        });
        tree.add(modify_menu);

        STMenuBar.add(file);
        STMenuBar.add(tree);
    }

    private void ToolBar_init() {
        STToolBar = new JToolBar(SwingConstants.HORIZONTAL);
        STToolBar.setSize(1200, 30);

        JButton add_button = new JButton(addimage);
        add_button.setToolTipText("新建标签");
        add_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newTagFrame.setVisible( true );
            }
        });
        STToolBar.add(add_button);

        JButton delete_button = new JButton(deleteimage);
        delete_button.setToolTipText("删除标签");
        delete_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    delete_node_from_tree();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        STToolBar.add(delete_button);

        JButton modify_button = new JButton(modifyimage);
        modify_button.setToolTipText("修改属性");
        modify_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                modifyJFrame.setVisible( true );
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

        modifyJFrame.setBounds( screenSize.width / 2, screenSize.height / 2, 400, 200 );
        modifyJFrame.setLayout(null );

        JLabel attribute = new JLabel("修改的属性",SwingConstants.CENTER);
        attribute.setBounds(10,10,150,30);
        modifyJFrame.add(attribute);

        final JTextField attrArea = new JTextField();
        attrArea.setBounds( 170, 10, 150, 30 );
        modifyJFrame.add( attrArea );


        JLabel value = new JLabel("属性值",SwingConstants.CENTER);
        value.setBounds(10,50,150,30);
        modifyJFrame.add(value);

        final JTextField valueArea = new JTextField();
        valueArea.setBounds( 170, 50, 150, 30 );
        modifyJFrame.add( valueArea );

        JButton editconfirm = new JButton( "确认修改" );
        editconfirm.setBounds( 70, 110, 100, 30 );
        editconfirm.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                String attr = attrArea.getText();
                String val = valueArea.getText();
                try {
                    modify_node( attr,val );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                modifyJFrame.setVisible( false );
            }
        } );
        JButton editcancel = new JButton( "取消修改" );
        editcancel.setBounds( 190, 110, 100, 30 );
        editcancel.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                modifyJFrame.setVisible( false );
            }
        } );
        modifyJFrame.add( editconfirm );
        modifyJFrame.add( editcancel );
        modifyJFrame.setVisible( false );

        newTagFrame.setBounds( screenSize.width / 2, screenSize.height / 2, 400, 250 );
        newTagFrame.setLayout( null );
        String[] type1 = {"xs:element","xs:complexType","xs:sequence"};
        JComboBox TagType = new JComboBox(type1);
        TagType.setBounds(100,10,150,50);
        TagType.setBorder(BorderFactory.createTitledBorder("选择类型"));
        TagType.setEditable(true);
        TagType.setOpaque(false);
        newTagFrame.add(TagType);

        JLabel name = new JLabel("名字",SwingConstants.CENTER);
        name.setBounds(10,70,150,30);
        newTagFrame.add(name);

        final JTextField nameArea = new JTextField();
        nameArea.setBounds( 170, 70, 150, 30 );
        newTagFrame.add( nameArea );


        JLabel type = new JLabel("数值类型",SwingConstants.CENTER);
        type.setBounds(10,110,150,30);
        newTagFrame.add(type);

        final JTextField typeArea = new JTextField();
        typeArea.setBounds( 170, 110, 150, 30 );
        newTagFrame.add( typeArea );

        JButton addconfirm = new JButton( "确认添加" );
        addconfirm.setBounds( 60, 160, 100, 30 );
        addconfirm.addActionListener( new ActionListener() {
            //			@Override
            public void actionPerformed(ActionEvent arg0) {
                XSDNode newNode = new XSDNode();
                String tagT = (String) TagType.getSelectedItem();
                String typeS = typeArea.getText();
                String nameS = nameArea.getText();
                newNode.setTagType( tagT );
                newNode.setName( nameS );
                newNode.setType( typeS );
//                System.out.println(newNode);
                try {
                    add_node_to_tree( newNode );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                newTagFrame.setVisible( false );
            }
        } );
        JButton addcancel = new JButton( "取消添加" );
        addcancel.setBounds( 190, 160, 100, 30 );
        addcancel.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newTagFrame.setVisible( false );
            }
        } );
        newTagFrame.add( addconfirm );
        newTagFrame.add( addcancel );
        newTagFrame.setVisible( false );

    }

    private void PopMenu_init() {
        popMenu = new JPopupMenu();

        JMenuItem addItem = new JMenuItem("新建", addimage);
        addItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newTagFrame.setVisible(true);
            }
        });

        JMenuItem delItem = new JMenuItem("删除标签", deleteimage);
        delItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    delete_node_from_tree();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        JMenuItem editAttrItem = new JMenuItem("修改属性", modifyimage);
        editAttrItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                modifyJFrame.setVisible( true );
            }
        });
        popMenu.add(addItem);
        popMenu.add(delItem);
        popMenu.add(editAttrItem);
    }

    protected void modify_node(String attr,String value) throws Exception {
        DefaultMutableTreeNode tempTreeNode = (DefaultMutableTreeNode) SchemaTree.getLastSelectedPathComponent();
        if (tempTreeNode != null && !attr.equals("")&& !value.equals("")) {
            XSDNode tempNode = (XSDNode) tempTreeNode.getUserObject();
            xsdUtil.modify(xsd,tempNode,attr,value);
            reload_Schema(xsd);
        }
    }

    protected void delete_node_from_tree() throws Exception {
        DefaultMutableTreeNode tempTreeNode = (DefaultMutableTreeNode) SchemaTree.getLastSelectedPathComponent();
        if (tempTreeNode == null)
            return;
        XSDNode tempNode = (XSDNode) tempTreeNode.getUserObject();
        xsdUtil.deleteTag(xsd,tempNode);
        reload_Schema(xsd);
        //下发新的主题树
    }

    protected void add_node_to_tree(XSDNode newNode) throws Exception {
        DefaultMutableTreeNode tempTreeNode = (DefaultMutableTreeNode) SchemaTree.getLastSelectedPathComponent();
        XSDNode tempNode = (XSDNode) tempTreeNode.getUserObject();
        System.out.println(newNode);
        xsdUtil.addTag(xsd, tempNode, newNode);
        reload_Schema( xsd );
//        tempNode.setUserObject(temptopic);
    }

    protected void save_tree(DefaultMutableTreeNode current_node, String current_code) {
        JOptionPane.showMessageDialog(null, "保存成功");
    }

    public JPanel getTreeInstance() {
        if (STFrame == null) {
            STFrame = new JPanel();
            frame_init();
        }
        return STFrame;
    }

}
