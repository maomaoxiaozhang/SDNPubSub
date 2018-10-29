//package edu.bupt.wangfu.module.managerMgr.design;
//
//import edu.bupt.wangfu.info.device.*;
//import edu.bupt.wangfu.module.managerMgr.policyMgr.PolicyUtil;
//import edu.bupt.wangfu.module.topicTreeMgr.TopicTreeMgr;
//import edu.bupt.wangfu.module.topologyMgr.TopoMgr;
//import edu.bupt.wangfu.role.manager.ManagerStart;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.stereotype.Component;
//
//import javax.swing.*;
//import javax.swing.border.EmptyBorder;
//import javax.swing.border.LineBorder;
//import javax.swing.table.DefaultTableCellRenderer;
//import javax.swing.table.DefaultTableModel;
//import javax.swing.table.JTableHeader;
//import javax.swing.table.TableColumn;
//import javax.swing.tree.DefaultMutableTreeNode;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.util.*;
//import java.util.List;
//
//@Component
//public class PSManagerUI {
//    @Autowired
//    TopicTreeMgr topicTreeMgr;
//
//    @Autowired
//    TopoMgr topomgr;
//
//    @Autowired
//    Manager manager;
//
////    @Autowired
////    ManagerStart managerStart;
//
//    public static TopicTreeUI topicTreeUI = null;
//    public static PolicyUtil util = null;
//    private String currentGroup;
//    private java.util.List<String> currentTargetGroups;// 受限集群框内
//    Vector<String> groupNames = new Vector<>();
//
//    public static Dimension screenSize;
//    public static JPanel topicTreeM;// 主题树管理
//
//    public JTabbedPane visualManagement;// "可视化管理"
//    public JTextArea text;// 控制台文本
//    public JComboBox comboBox;// 根主题
//    public JComboBox comboBox_1;
//    public JComboBox comboBox_2;
//    public JComboBox comboBox_3;
//    public JComboBox comboBox_4;
//    public JComboBox comboBox_5;
//    private JFrame frame;// 主窗口
//    private JTabbedPane topTabbedPane;// 顶层tab窗体，分为图形化管理，控制台，系统设置等
//    private JPanel groupsMgmt;// 集群信息管理
//    private JScrollPane allGroupsScrollPane;// 所有集群 可滚动窗口容器
//    private JPanel allGroupsPane;// 所有集群
//    private JTabbedPane groupsInfoTabbedPane;// 包括“控制器”、“交换机”、“订阅信息”、“配置信息”、“端口信息”、“队列信息”
//    private JPanel controller;//控制器信息
//
//    private JPanel groupSwitch;// 集群交换机信息
//    private JPanel switchMap;// 集群内交换机拓扑关系图面板
//    private JLabel switchMapLabel;// 交换机拓扑
//    private JPanel switchMapPanel;// 可滚动Panel
//
//    private JPanel switchHost;// 交换机下主机信息面板
//    private JLabel switchHostLabel;// 交换机拓扑
//    private JPanel switchHostPanel;//可滚动Panel
//    private JScrollPane switchHostScrollPane;//滚动组件
//
//    private JPanel subscbs;// 订阅信息面板
//    private JTable subsTable;// 订阅信息表
//    //private ConcurrentHashMap<String, ArrayList<String>> groupSubs;
//    private DefaultTableModel groupSubsModel;
//    private JTextField groupsNameOrIPInput;// “按集群或ip地址查询”输入框
//
//    private JPanel ports; // 交换机端口信息
//    private JTable portTable; // 端口信息表
//    private JPanel queues; // 交换机队列信息
//    private JTable queueTable; //队列信息表
//
//    private JPanel topoManage; // 拓扑管理
//    private JPanel policyM;// 策略管理
//
//    private JPanel sdnConfig;//SDN相关信息配置
//
//    private JPanel consol;// 控制台
//    private JPanel sys;// 系统
//
//    private JPanel newPolicy;
//    private JLabel newPolicylabel;
//    private JLabel currentPolicyLabel;
//    private JPanel editPolicy;
//    private JScrollPane subTopicScrollPane;
//
//    private JPanel editChoose;
//    private JScrollPane showPanel;
//    public JComboBox groupName,switchName,portName,type,state;
//    private JButton showInfo;
//
//    private JPanel fbdnGroupsPanel;
//    private JButton confirmFbdnGroups;
//
//    private JTable policyTable;
//    private JButton currentPolicyReflash;
//    private JPanel policyLabel;
//    private DefaultTableModel currentPolicyTableModel;
//    // 被选中的集群名称和
//    private JPanel fbdnGroups;
//    private JPanel topicsPanel;
//    private JPanel currentPolicyPanel;
//
//    private JTextArea sysInfo;
//    private JLabel INT100;
//    private JLabel buptImage;
//    private JLabel allGroupLabel;
//    private JPanel allGroupsPanel;
//    private JScrollPane fbdnGroupsScrollPane;
//    private JPanel chooseTopic;
//
//
//    public PSManagerUI() {
//
////        topicTreeUI = new TopicTreeUI();
////        util = new PolicyUtil();
////        //policyList = ShorenUtils.getAllPolicy();
////        JFrame.setDefaultLookAndFeelDecorated(true);
////        /**
////         * com.jtattoo.plaf.aluminium.AluminiumLookAndFeel 椭圆按钮+翠绿色按钮背景+金属质感
////         *
////         */
////        try {
////            UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////        open();
////        currentTargetGroups = new ArrayList<>();
//        //msgExceptions = new ArrayList<>();
//    }
//
//    /**
//     * Launch the application.
//     */
////    public static void main(String[] args) {
////        EventQueue.invokeLater(new Runnable() {
////            public void run() {
////                try {
////                    JFrame.setDefaultLookAndFeelDecorated(true);
////                    UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
////                    PSManagerUI window = new PSManagerUI();
////                    window.open();
////                } catch (Exception e) {
////                    e.printStackTrace();
////                }
////            }
////        });
////    }
//
//    /**
//     * Initialize the contents of the frame.
//     */
//    public void open() {
//        frame = new JFrame();// 主窗口
//        Toolkit kit = Toolkit.getDefaultToolkit();
//        screenSize = kit.getScreenSize();
//        frame.setIconImage(kit.getImage("./img/INT25.png"));
//        frame.setResizable(false);
//        frame.setTitle("发布订阅管理器");
//        frame.setBounds(screenSize.width / 2 - 400, screenSize.height / 2 - 300, 800, 600);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.getContentPane().setLayout(null);
//        frame.setVisible(true);
//
//        topTabbedPane = new JTabbedPane(JTabbedPane.TOP);// 顶层tab窗体，分为图形化管理，控制台，系统设置等
//        topTabbedPane.setBounds(0, 0, 792, 570);
//        topTabbedPane.setPreferredSize(frame.getSize());
//        frame.getContentPane().add(topTabbedPane);
//
//        visualManagement = new JTabbedPane(JTabbedPane.LEFT);// 图形化管理分页
//        visualManagement.setToolTipText("图形化管理");
//        topTabbedPane.addTab("图形化管理", null, visualManagement, null);
//
//        groupsMgmt = new JPanel();// 集群成员窗口
//        visualManagement.addTab("", new ImageIcon("./img/GroupM.png"), groupsMgmt, null);
//        groupsMgmt.setLayout(new GridLayout(0, 1, 0, 0));
//
//        allGroupsPanel = new JPanel();
//        allGroupsPanel.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
//        groupsMgmt.add(allGroupsPanel);
//
//        allGroupsPane = new JPanel();// 所有集群显示窗口
//        FlowLayout flowLayout = (FlowLayout) allGroupsPane.getLayout();
//        flowLayout.setAlignment(FlowLayout.LEADING);
//        allGroupsPane.setSize(new Dimension(350, 1000));
//        allGroupsPane.setPreferredSize(new Dimension(350, 450));// 显示所有集群信息的窗口的大小要根据集群数量动态调整
//        allGroupsPanel.setLayout(new BorderLayout(0, 0));
//
//        allGroupLabel = new JLabel("\u5F53\u524D\u6240\u6709\u96C6\u7FA4\u4FE1\u606F\uFF08\u70B9\u51FB\u67E5\u8BE2\uFF09");
//        allGroupsPanel.add(allGroupLabel, BorderLayout.NORTH);
//        allGroupLabel.setHorizontalAlignment(SwingConstants.CENTER);
//
//        allGroupsScrollPane = new JScrollPane(allGroupsPane);
//        allGroupsPanel.add(allGroupsScrollPane);
//
//        groupsInfoTabbedPane = new JTabbedPane(JTabbedPane.TOP);
//        groupsMgmt.add(groupsInfoTabbedPane);
//
//        //集群控制器信息
//        controller = new JPanel();
//        controller.setBorder(new EmptyBorder(20, 20, 20, 20));
//        controller.setLayout(null);
//        groupsInfoTabbedPane.addTab("控制器", null, controller, null);
//
//        // 集群交换机信息
//        groupSwitch = new JPanel();
//        groupsInfoTabbedPane.addTab("交换机", null, groupSwitch, null);
//        groupSwitch.setLayout(new GridLayout(1, 0, 2, 0));//1行多列，行间距为2
//
//        // 交换机拓扑
//        switchMap = new JPanel();
//        FlowLayout fl = (FlowLayout) switchMap.getLayout();
//        fl.setAlignment(FlowLayout.LEADING);
//
//        groupSwitch.add(switchMap);
//        switchMap.setLayout(new BorderLayout(0, 0));
//
//        switchMapLabel = new JLabel("交换机拓扑");
//        switchMapLabel.setHorizontalAlignment(SwingConstants.CENTER);
//        switchMapLabel.setHorizontalTextPosition(SwingConstants.CENTER);
//        switchMap.add(BorderLayout.NORTH, switchMapLabel);
//
//        switchMapPanel = new JPanel();
//        switchMapPanel.setBorder(new EmptyBorder(10, 10, 10, 10));//设置边距
//        switchMapPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
//        switchMapPanel.setPreferredSize(new Dimension(250, 500));
//        switchMap.add(BorderLayout.CENTER, switchMapPanel);
//
//        JScrollPane switchMapScrollPane = new JScrollPane(switchMapPanel);
//        switchMap.add(switchMapScrollPane);
//
//        // 交换机下主机
//        switchHost = new JPanel();
//        FlowLayout hostlayout = (FlowLayout) switchHost.getLayout();
//        hostlayout.setAlignment(FlowLayout.CENTER);
//
//        groupSwitch.add(switchHost);
//        switchHost.setLayout(new BorderLayout(0, 0));
//
//        switchHostLabel = new JLabel("交换机下主机");
//        switchHostLabel.setHorizontalAlignment(SwingConstants.CENTER);
//        switchHostLabel.setHorizontalTextPosition(SwingConstants.CENTER);
//        switchHost.add(BorderLayout.NORTH, switchHostLabel);
//
//        switchHostPanel = new JPanel();
//        switchHostPanel.setBorder(new EmptyBorder(7, 3, 7, 3));//设置边距
//        switchHostPanel.setPreferredSize(new Dimension(250, 500));
//        switchHost.add(BorderLayout.CENTER, switchHostPanel);
//
//        switchHostScrollPane = new JScrollPane(switchHostPanel);
//        switchHost.add(switchHostScrollPane);
//
//        subscbs = new JPanel();
//        groupsInfoTabbedPane.addTab("订阅信息", null, subscbs, null);
//        subscbs.setLayout(null);
//
//        subsTable = new JTable();
//        subsTable.setVerifyInputWhenFocusTarget(false);
//        subsTable.setEnabled(false);
//        subsTable.setOpaque(false);
//        subsTable.setBounds(0,0,420,245);
//        JScrollPane subsScrollPane = new JScrollPane(subsTable);
//        subsScrollPane.setSize(new Dimension(420, 245));
//        subsScrollPane.setViewportView(subsTable);
//        subsScrollPane.setOpaque(false);
//        subscbs.add(subsScrollPane);
//
//        JLabel lblNewLabel = new JLabel("集群成员订阅查询(按ip)");
//        lblNewLabel.setBounds(441, 17, 150, 15);
//        subscbs.add(lblNewLabel);
//
//        groupsNameOrIPInput = new JTextField();
//        groupsNameOrIPInput.setBounds(443, 42, 150, 21);
//        groupsNameOrIPInput.setHorizontalAlignment(SwingConstants.LEFT);
//        subscbs.add(groupsNameOrIPInput);
//        groupsNameOrIPInput.setColumns(10);
//
//        JButton checkButton = new JButton("查询");
//        checkButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//
//                String searchInput = groupsNameOrIPInput.getText();
//                if (searchInput.equals("")) {
//                    JOptionPane.showMessageDialog(null, "输入为空！请输入要查询的集群名称或代表ip");
//                } else {
//                    DefaultTableModel searchSubsModel;
//                    List<String> sercherSubs =topomgr.getLsdb().getLSDB().get(searchInput).getSubsTopics();
//                    String[] columnNames = {"集群" + currentGroup + "成员" + searchInput + "的订阅"};
//                    String[][] searchSubInfo;
//                    if (sercherSubs != null && sercherSubs.size() > 0) {
//                        searchSubInfo = new String[sercherSubs.size()][1];
//                        for (int i = 0; i < sercherSubs.size(); i ++) {
//                            searchSubInfo[i][0] = sercherSubs.get(i);
//                        }
//                        searchSubsModel = new DefaultTableModel(searchSubInfo,columnNames);
//                        subsTable.removeAll();
//                        subsTable.setModel(searchSubsModel);
//                    } else {
//                        JOptionPane.showMessageDialog(null, "此成员无订阅信息");
//                    }
//                }
//            }
//        });
//        checkButton.setBounds(498, 73, 93, 23);
//        subscbs.add(checkButton);
//
//        //端口信息
//        ports = new JPanel();
//        groupsInfoTabbedPane.addTab("端口信息", null, ports, null);
//
//        //队列信息
//        queues = new JPanel();
//        groupsInfoTabbedPane.addTab("队列信息", null, queues, null);
//
//        // 拓扑
//        topoManage = new JPanel(new BorderLayout());
//        topoManage.setBounds(new Rectangle(650,570));
//        visualManagement.addTab("", new ImageIcon("./img/topoManage.png"), topoManage, null);
//
//        //主题树
//        topicTreeM = new JPanel();
//        visualManagement.addTab("", new ImageIcon("./img/TopicTree.png"), topicTreeM, null);
//
//        try {
//            topicTreeM.add(topicTreeUI.getTreeInstance());
//        } catch (Exception e1) {
//            e1.printStackTrace();
//        }
//
//
//        //策略管理
//        policyM = new JPanel();
//        visualManagement.addTab("", new ImageIcon("./img/policy.png"), policyM, null);
//        policyM.setLayout(new GridLayout(0, 1, 0, 5));
//
//        newPolicy = new JPanel();
//        newPolicy.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
//        newPolicy.setName("\u65B0\u5EFA\u7B56\u7565");
//        newPolicy.setAutoscrolls(true);
//        policyM.add(newPolicy);
//        newPolicy.setLayout(new BorderLayout(0, 0));
//
//        newPolicylabel = new JLabel("\u65B0\u5EFA\u7B56\u7565");
//        newPolicylabel.setHorizontalAlignment(SwingConstants.CENTER);
//        newPolicy.add(newPolicylabel, BorderLayout.NORTH);
//
//        editPolicy = new JPanel();
//        newPolicy.add(editPolicy, BorderLayout.CENTER);
//        editPolicy.setLayout(null);
//
//        Enumeration trees = topicTreeUI.lib_root.children();
//        Vector<DefaultMutableTreeNode> treesNames = new Vector<>();
//        DefaultMutableTreeNode defaultChose = new DefaultMutableTreeNode("All");
//
//        treesNames.add(defaultChose);
//
//        while (trees.hasMoreElements()) {
//            treesNames.add((DefaultMutableTreeNode) trees.nextElement());
//        }
//
//        chooseTopic = new JPanel();
//        chooseTopic.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
//        chooseTopic.setBounds(10, 10, 602, 65);
//        editPolicy.add(chooseTopic);
//        chooseTopic.setLayout(null);
//        comboBox = new JComboBox(treesNames);
//        comboBox.setBounds(10, 5, 124, 45);
//        comboBox.setSelectedIndex(0);
//        comboBox.setOpaque(false);
//        chooseTopic.add(comboBox);
//
//        comboBox.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//
//                if (comboBox_1.getItemCount() > 0) {
//                    comboBox_1.removeAllItems();// 先清除
//                }
//                if (comboBox.getSelectedIndex() != 0) {
//
//                    DefaultMutableTreeNode tempNode = (DefaultMutableTreeNode) comboBox.getSelectedItem();
//                    DefaultMutableTreeNode defaultChose = new DefaultMutableTreeNode("All");
//
//                    comboBox_1.addItem(defaultChose);
//                    comboBox_1.setSelectedIndex(0);
//                    if (tempNode != null) {
//                        Enumeration tempNodeEnum = tempNode.children();
//                        while (tempNodeEnum.hasMoreElements()) {
//                            DefaultMutableTreeNode tempTree = (DefaultMutableTreeNode) tempNodeEnum.nextElement();
//                            comboBox_1.addItem(tempTree);
//                        }
//                    }
//                    reflashFbdnGroups();
//                }
//            }
//        });
//
//        comboBox.setName("选择主题树");
//        comboBox.setToolTipText("选择主题树");
//        comboBox.setBorder(BorderFactory.createTitledBorder("选择主题树"));
//        comboBox.setEditable(true);
//
//        subTopicScrollPane = new JScrollPane();
//        subTopicScrollPane.setBounds(144, 0, 458, 63);
//        chooseTopic.add(subTopicScrollPane);
//        subTopicScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
//
//        topicsPanel = new JPanel();
//        topicsPanel.setBorder(null);
//        subTopicScrollPane.setViewportView(topicsPanel);
//        FlowLayout flowLayout_2 = (FlowLayout) topicsPanel.getLayout();
//        flowLayout_2.setAlignment(FlowLayout.LEADING);
//
//        comboBox_1 = new JComboBox();
//        comboBox_1.setPreferredSize(new Dimension(95, 45));
//        comboBox_1.setEditable(true);
//        comboBox_1.setOpaque(false);
//        topicsPanel.add(comboBox_1);
//
//        comboBox_1.setBorder(BorderFactory.createTitledBorder("选择主题"));
//        comboBox_1.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                if (comboBox_2.getItemCount() > 0) {
//                    comboBox_2.removeAllItems();// 先清除
//                }
//                DefaultMutableTreeNode tempNode = (DefaultMutableTreeNode) comboBox_1.getSelectedItem();
//                DefaultMutableTreeNode defaultChose = new DefaultMutableTreeNode("All");
//
//                comboBox_2.addItem(defaultChose);
//                comboBox_2.setSelectedIndex(0);
//
//                if (comboBox_1.getSelectedIndex() != 0) {
//
//                    if (tempNode != null) {
//                        Enumeration tempNodeEnum = tempNode.children();
//                        while (tempNodeEnum.hasMoreElements()) {
//                            DefaultMutableTreeNode tempTree = (DefaultMutableTreeNode) tempNodeEnum.nextElement();
//                            comboBox_2.addItem(tempTree);
//                        }
//                    }
//                    // 显示受限集群
//                    reflashFbdnGroups();
//                }
//            }
//        });
//
//        comboBox_2 = new JComboBox();
//        comboBox_2.setEditable(true);
//        comboBox_2.setPreferredSize(new Dimension(95, 45));
//        comboBox_2.setBorder(BorderFactory.createTitledBorder("选择主题"));
//        comboBox_2.setOpaque(false);
//        topicsPanel.add(comboBox_2);
//
//        comboBox_2.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//
//                if (comboBox_3.getItemCount() > 0) {
//                    comboBox_3.removeAllItems();// 先清除
//                }
//                DefaultMutableTreeNode tempNode = (DefaultMutableTreeNode) comboBox_2.getSelectedItem();
//                DefaultMutableTreeNode defaultChose = new DefaultMutableTreeNode("All");
//
//                comboBox_3.addItem(defaultChose);
//                comboBox_3.setSelectedIndex(0);
//                if (comboBox_2.getSelectedIndex() != 0) {
//
//                    if (tempNode != null) {
//                        Enumeration tempNodeEnum = tempNode.children();
//                        while (tempNodeEnum.hasMoreElements()) {
//                            DefaultMutableTreeNode tempTree = (DefaultMutableTreeNode) tempNodeEnum.nextElement();
//
//                            comboBox_3.addItem(tempTree);
//                        }
//                    }
//                    reflashFbdnGroups();
//                }
//            }
//        });
//        comboBox_3 = new JComboBox();
//        comboBox_3.setPreferredSize(new Dimension(95, 45));
//        comboBox_3.setBorder(BorderFactory.createTitledBorder("选择主题"));
//        comboBox_3.setEditable(true);
//        comboBox_3.setOpaque(false);
//        topicsPanel.add(comboBox_3);
//
//        comboBox_3.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                if (comboBox_4 != null && comboBox_4.getItemCount() > 0) {
//                    comboBox_4.removeAllItems();// 先清除
//                }
//                DefaultMutableTreeNode tempNode = (DefaultMutableTreeNode) comboBox_3.getSelectedItem();
//                if (topicsPanel.getComponentCount() > 4) {
//
//                    topicsPanel.remove(4);
//                } else if (topicsPanel.getComponentCount() > 3) {
//                    topicsPanel.remove(3);
//                }
//                if (tempNode != null && tempNode.children() != null) {// 当选择某个主题，并且该主题有子主题时，创建第四个选框
//                    Enumeration tempNodeEnum = tempNode.children();
//                    comboBox_4 = new JComboBox();
//                    comboBox_4.setPreferredSize(new Dimension(95, 45));
//                    comboBox_4.setBorder(BorderFactory.createTitledBorder("选择主题"));
//                    comboBox_4.setEditable(true);
//                    comboBox_4.setOpaque(false);
//                    topicsPanel.add(comboBox_4);
//
//                    DefaultMutableTreeNode defaultChose = new DefaultMutableTreeNode("All");
//                    comboBox_4.addItem(defaultChose);
//                    comboBox_4.setSelectedIndex(0);
//
//                    if (comboBox_3.getSelectedIndex() != 0) {
//
//                        while (tempNodeEnum.hasMoreElements()) {
//                            DefaultMutableTreeNode tempTree = (DefaultMutableTreeNode) tempNodeEnum.nextElement();
//                            comboBox_4.addItem(tempTree);
//                        }
//                        comboBox_4.addActionListener(new ActionListener() {
//
//                            public void actionPerformed(ActionEvent e) {
//                                if (comboBox_5 != null && comboBox_5.getItemCount() > 0) {
//                                    comboBox_5.removeAllItems();// 先清除
//                                }
//                                DefaultMutableTreeNode tempNode = (DefaultMutableTreeNode) comboBox_4.getSelectedItem();
//                                if (topicsPanel.getComponentCount() > 4) {
//                                    topicsPanel.remove(4);
//                                }
//                                // 当选择某个主题，并且该主题有子主题时，创建第四个选框
//                                if (tempNode != null && tempNode.children() != null) {
//                                    Enumeration tempNodeEnum = tempNode.children();
//
//                                    if (comboBox_4.getSelectedIndex() != 0) {
//                                        comboBox_5 = new JComboBox();
//                                        comboBox_5.setPreferredSize(new Dimension(95, 45));
//                                        comboBox_5.setBorder(BorderFactory.createTitledBorder("选择主题"));
//                                        comboBox_5.setEditable(true);
//                                        comboBox_5.setOpaque(false);
//                                        topicsPanel.add(comboBox_5);
//                                        DefaultMutableTreeNode defaultChose = new DefaultMutableTreeNode("All");
//                                        comboBox_5.addItem(defaultChose);
//                                        comboBox_5.setSelectedIndex(0);
//
//                                        while (tempNodeEnum.hasMoreElements()) {
//                                            DefaultMutableTreeNode tempTree = (DefaultMutableTreeNode) tempNodeEnum.nextElement();
//                                            comboBox_5.addItem(tempTree);
//                                        }
//                                        comboBox_5.addActionListener(new ActionListener() {
//                                            public void actionPerformed(ActionEvent e) {
//                                                System.out.println("已达5级主题");
//                                            }
//                                        });
//                                    }
//                                }
//                            }
//                        });
//                    }
//                    reflashFbdnGroups();
//                }
//            }
//        });
//
//        // 受限集群总面板
//        fbdnGroups = new JPanel();
//        fbdnGroups.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
//        fbdnGroups.setBounds(10, 87, 600, 151);
//
//        editPolicy.add(fbdnGroups);
//        fbdnGroups.setLayout(new BorderLayout(0, 0));
//
//        JLabel label = new JLabel("受限集群（仅在线）");
//        label.setHorizontalAlignment(SwingConstants.CENTER);
//        fbdnGroups.add(label, BorderLayout.NORTH);
//
//        fbdnGroupsPanel = new JPanel();
//        fbdnGroupsScrollPane = new JScrollPane(fbdnGroupsPanel);
//        fbdnGroupsPanel.setPreferredSize(new Dimension(230, 80));
//        fbdnGroupsScrollPane.setPreferredSize(new Dimension(230, 80));
//        fbdnGroupsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
//        fbdnGroups.add(fbdnGroupsScrollPane, BorderLayout.CENTER);
//        // 默认加载所有集群 且处于未选中状态
//        if (manager.getGroups() != null) {
//            for (final String groupName : manager.getGroups().keySet()) {
//                final JCheckBox group = new JCheckBox(groupName);
//                group.setSelected(false);
//                group.addActionListener(new ActionListener() {
//                    public void actionPerformed(ActionEvent e) {
//                        if (group.isSelected()) {
//                            currentTargetGroups.add(groupName);
//                        } else {
//                            for (String temTG : currentTargetGroups) {
//                                if (temTG.equals(groupName)) {
//                                    currentTargetGroups.remove(temTG);
//                                    break;
//                                }
//                            }
//                        }
//                        System.out.print("currentGroups:" + currentTargetGroups);
//                    }
//                });
//                fbdnGroupsPanel.add(group);
//            }
//        }
//
//        confirmFbdnGroups = new JButton("应用更改");
//        confirmFbdnGroups.addActionListener(new ActionListener() {
//
//            public void actionPerformed(ActionEvent e) {
//                // 使策略变更生效
//                // 获得受限集群面板中选中的集群
//                Policy currentPolicy = new Policy();
//                List<String> currentChosenTargetGroups = new ArrayList<>();
//
//                for (int i = 0; i < fbdnGroupsPanel.getComponentCount(); i++) {
//                    JCheckBox topicsChoosed = (JCheckBox) fbdnGroupsPanel.getComponent(i);
//                    if (topicsChoosed.isSelected()) {
//                        currentChosenTargetGroups.add(topicsChoosed.getText());
//                    }
//                }
//
//                currentPolicy.setTargetGroups(currentChosenTargetGroups);
//
//                // 获得主题树中选中主题
//                String selectedTopic = comboBox.getSelectedItem().toString();
//                /*TopicEntry currentChosenTopicEntry = new TopicEntry();
//                currentChosenTopicEntry.setTopicName(selectedTopic);
//
//                String chosenTopicPath = "ou=" + selectedTopic + ",ou=all_test,dc=wsn,dc=com";
//                try {
//                    currentChosenTopicEntry.setTopicCode(ShorenUtils.ldap.getByDN(chosenTopicPath).getTopicCode());
//                } catch (NamingException e2) {
//                    e2.printStackTrace();
//                }*/
//                for (int i = 0; i < topicsPanel.getComponentCount(); i++) {
//                    JComboBox topicsChoosed = (JComboBox) topicsPanel.getComponent(i);
//                    if (topicsChoosed.getSelectedIndex() != 0 && topicsChoosed.getSelectedItem() != null) {
//                        selectedTopic = selectedTopic + ":" + topicsChoosed.getSelectedItem().toString();
//                    } else
//                        break;
//                }
//                currentPolicy.setTargetTopic(selectedTopic);
//                // 使策略生效
//                if (selectedTopic == null) {
//                    JOptionPane.showMessageDialog(null, "请选择主题!");
//                } else if (selectedTopic != null) {
//                    managerStart.getPolicyMap().put(selectedTopic,currentPolicy);
//                    try {
//                        util.updateXMLFile(managerStart.getPolicyMap());
//                    } catch (Exception e1) {
//                        e1.printStackTrace();
//                    }
//                    currentPolicyReflash.doClick();
//                    JOptionPane.showMessageDialog(null, "策略设置成功!");
//                } else
//                    JOptionPane.showMessageDialog(null, "请确认所选的主题是否正确!");
//            }
//        });
//        fbdnGroups.add(confirmFbdnGroups, BorderLayout.SOUTH);
//
//        currentPolicyPanel = new JPanel();
//        currentPolicyPanel.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
//        currentPolicyPanel.setOpaque(false);
//        policyM.add(currentPolicyPanel);
//        currentPolicyPanel.setLayout(new BorderLayout(0, 0));
//
//        policyLabel = new JPanel();
//        currentPolicyPanel.add(policyLabel, BorderLayout.NORTH);
//        policyLabel.setLayout(new FlowLayout(FlowLayout.TRAILING, 5, 5));
//
//        currentPolicyLabel = new JLabel("\u5F53\u524D\u7B56\u7565");
//        currentPolicyLabel.setPreferredSize(new Dimension(500, 15));
//        policyLabel.add(currentPolicyLabel);
//        currentPolicyLabel.setHorizontalTextPosition(SwingConstants.CENTER);
//        currentPolicyLabel.setHorizontalAlignment(SwingConstants.CENTER);
//
//        currentPolicyReflash = new JButton("刷新");
//        currentPolicyReflash.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                reflashCurrentPolicy();
//            }
//        });
//        policyLabel.add(currentPolicyReflash);
//
//        if(managerStart.getPolicyMap() != null){
//            // 初始化当前所有策略信息
//            String[][] tPolicy = new String[managerStart.getPolicyMap().size()][2];
//            String[] columnNames = {"主题", "对应策略"};
//            List<Policy> policyList = (List<Policy>) managerStart.getPolicyMap().values();
//            for (int i = 0; i < managerStart.getPolicyMap().size(); i++) {
//                tPolicy[i][0] = policyList.get(i).getTargetTopic();
//                tPolicy[i][1] = policyList.get(i).getTargetGroups().toString();
//
//            }
//            System.out.println(Arrays.deepToString(tPolicy));
//            currentPolicyTableModel = new DefaultTableModel(tPolicy, columnNames) {
//                private static final long serialVersionUID = 1L;
//                public boolean isCellEditable(int row, int column) {
//                    return false;
//                }
//            };
//            policyTable = new JTable();
//            policyTable.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
//            policyTable.setModel(currentPolicyTableModel);
//            policyTable.setOpaque(false);
//            JScrollPane currentPolicyScrollPane = new JScrollPane(policyTable);
//            currentPolicyScrollPane.setOpaque(false);
//
//            policyTable.setPreferredScrollableViewportSize(new Dimension(600, 500));
//            currentPolicyPanel.add(currentPolicyScrollPane, BorderLayout.CENTER);
//        }
//
//
//        //reflashCurrentPolicy();
//
//        //SDN配置信息  HanB
//        sdnConfig = new JPanel();
//        visualManagement.addTab("", new ImageIcon("./img/sdnConfig.png"), sdnConfig, null);
//        sdnConfig.setLayout(new GridLayout(0,1,0,0));
//
//        JLabel ctrl = new JLabel("Controller");
//        ctrl.setBounds(270,0,100,20);
//
//        JLabel odllabel = new JLabel("odl账号:");
//        odllabel.setBounds(150,50,50,23);
//        final JTextField odl = new JTextField("admin");
//        odl.setBounds(200,50,100,23);
//        JLabel odlpwdlabel = new JLabel("odl密码:");
//        odlpwdlabel.setBounds(330,50,50,23);
//        final JTextField odlpwd = new JTextField("admin");
//        odlpwd.setBounds(380,50,100,23);
//        JLabel groupmountlabel = new JLabel("集群规模:");
//        groupmountlabel.setBounds(250,100,100,23);
//        final JTextField groupmount = new JTextField("50");
//        groupmount.setBounds(310,100,60,23);
//
//        JPanel ctrlp = new JPanel();
//        ctrlp.setLayout(null);
//        ctrlp.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
//        ctrlp.add(ctrl);
//        ctrlp.add(odllabel);
//        ctrlp.add(odl);
//        ctrlp.add(odlpwdlabel);
//        ctrlp.add(odlpwd);
//        ctrlp.add(groupmountlabel);
//        ctrlp.add(groupmount);
//
//        JLabel swch = new JLabel("Switch");
//        swch.setBounds(280,0,100,20);
//        JLabel wqlabel = new JLabel("平均队列长度权值(wq):");
//        wqlabel.setBounds(200,60,130,23);
//        final JTextField wq = new JTextField("0.4");
//        wq.setBounds(340,60,60,23);
//
//        JPanel swchp = new JPanel();
//        swchp.setLayout(null);
//        swchp.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
//        swchp.add(swch);
//        swchp.add(wqlabel);
//        swchp.add(wq);
//
//        JLabel hst = new JLabel("Host");
//        hst.setBounds(290,0,100,23);
//        JButton defalt = new JButton("恢复默认");
//        defalt.setBounds(0,157,313,23);
//        defalt.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                odl.setText("admin");
//                odlpwd.setText("admin");
//                groupmount.setText("50");
//                wq.setText("0.4");
//            }
//        });
//        /*JButton modefy = new JButton("点击修改");
//        modefy.setBounds(314,157,313,23);
//        modefy.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                String odlstr = odl.getText();
//                String odlpwdstr = odlpwd.getText();
//                String groupmountstr = groupmount.getText();
//                String wqstr = wq.getText();
//                MsgSdnConfig msgSdnConfig = new MsgSdnConfig(odlstr,odlpwdstr,Integer.parseInt(groupmountstr),
//                        Double.parseDouble(wqstr));
//                if (new SdnConfig().bloodMsgSdnConfig(msgSdnConfig)) {
//                    JOptionPane.showMessageDialog(null, "设置成功!");
//                }else {
//                    JOptionPane.showMessageDialog(null, "未检测到配置变化，请重新设置!");
//                }
//                System.out.println(odlstr + " " + odlpwdstr + " " + groupmountstr + " " + wqstr);
//            }
//        });
//
//        JPanel hstp = new JPanel();
//        hstp.setLayout(null);
//        hstp.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
//        hstp.add(hst);
//        hstp.add(defalt);
//        hstp.add(modefy);
//
//        sdnConfig.add(ctrlp);
//        sdnConfig.add(swchp);
//        sdnConfig.add(hstp);*/
//
//        // 流量管理
//        final JPanel flowManage = new JPanel();
//        flowManage.setLayout(null);
//        //flowManage.setLayout(new GridLayout(0,1,0,0));
//        visualManagement.addTab("", new ImageIcon("./img/flowManage.png"), flowManage, null);
//
//
//        editChoose = new JPanel();
//        editChoose.setBounds(10, 10, 650, 70);
//        editChoose.setLayout(null);
//
//        JPanel panel = new JPanel();
//        showPanel = new JScrollPane(panel);
//        showPanel.setBounds(10, 80, 650,450 );
//        //showPanel.setLayout(null);
//
//        //flowManage.add(editChoose);
//        //flowManage.add(showPanel);
//
//        JLabel choose = new JLabel("选择端口");
//        choose.setBounds(270,0,100,20);
//        editChoose.add(choose);
//
//        final String defaultC = new String("null");
//        //ArrayList<String> day = new ArrayList<>();
//        //ArrayList<Double> element = new ArrayList<>();
//        groupNames.add(defaultC);
//        if (manager.getGroups() != null) {
//            for (final String group : manager.getGroups().keySet()) {
//                groupNames.add(group);
//            }
//        }
//
//        groupName = new JComboBox(groupNames);
//        groupName.setBounds(10,20,95,45);
//        groupName.setSelectedIndex(0);
//        groupName.setOpaque(false);
//        editChoose.add(groupName);
//
//        groupName.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//
//                if (switchName.getItemCount() > 0) {
//                    switchName.removeAllItems();// 先清除
//                }
//                if (groupName.getSelectedIndex() != 0) {
//
//                    String tempGroup = (String)groupName.getSelectedItem();
//                    switchName.addItem(defaultC);
//                    switchName.setSelectedIndex(0);
//                    if (tempGroup != null) {
//                        final Map<String, Switch> groupSwitchMap = manager.getGroups().get(tempGroup).getSwitches();
//                        for (final String switchId : groupSwitchMap.keySet()) {
//                            switchName.addItem(switchId);
//                        }
//                    }
//
//                }
//            }
//        });
//
//        groupName.setName("选择集群");
//        groupName.setToolTipText("选择集群");
//        groupName.setBorder(BorderFactory.createTitledBorder("选择集群"));
//        groupName.setEditable(true);
//
//
//        switchName = new JComboBox();
//        switchName.setBounds(120,20,110,45);
//        switchName.setEditable(true);
//        switchName.setOpaque(false);
//        editChoose.add(switchName);
//
//        switchName.setBorder(BorderFactory.createTitledBorder("选择交换机"));
//        switchName.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                if (portName.getItemCount() > 0) {
//                    portName.removeAllItems();// 先清除
//                }
//
//                portName.addItem(defaultC);
//                portName.setSelectedIndex(0);
//
//                if (switchName.getSelectedIndex() != 0) {
//
//                    String tempSwitch = (String) switchName.getSelectedItem();
//                    if (tempSwitch != null) {
//                        Set<String> ports =manager.getGroups().get(groupName.getSelectedItem()).getSwitches().get(tempSwitch).getPorts();
//                        for(String port:ports){
//                            portName.addItem(port);
//                        }
//                    }
//                }
//
//
//            }
//        });
//
//        portName = new JComboBox();
//        portName.setEditable(true);
//        portName.setBounds(235,20,95,45);
//        portName.setBorder(BorderFactory.createTitledBorder("选择端口"));
//        portName.setOpaque(false);
//        editChoose.add(portName);
//
//        String list1[] = {"流量总数","平均速率","丢包率"};
//        type = new JComboBox(list1);
//        type.setEditable(true);
//        type.setBounds(340,20,95,45);
//        type.setBorder(BorderFactory.createTitledBorder("选择表数据"));
//        type.setOpaque(false);
//        editChoose.add(type);
//
//        String list2[] = {"日表","月表","年表"};
//        state = new JComboBox(list2);
//        state.setEditable(true);
//        state.setBounds(445,20,95,45);
//        state.setBorder(BorderFactory.createTitledBorder("选择表类型"));
//        state.setOpaque(false);
//        editChoose.add(state);
//
//        showInfo = new JButton("查询");
//        showInfo.setBounds(550,30,50,30);
//        /*showInfo.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                String c =AdminMgr.groups.get(groupName.getSelectedItem()).controllerAddr;
//                String s = (String)switchName.getSelectedItem();
//                String p = (String)portName.getSelectedItem();
//                String t = (String)type.getSelectedItem();
//                String tp =(String)state.getSelectedItem() ;
//                System.out.println(c);
//                System.out.println(s);
//                System.out.println(p);
//                System.out.println(t);
//                System.out.println(tp);
//
//                DataCollect dc = new DataCollect();
//                dc.select(c,s,p,day,element,t,tp);
//
//                FlowInfoShow flowInfoShow = new FlowInfoShow();// 柱状图的panel
//                flowInfoShow.setTitle(t);
//                flowInfoShow.setHorizontalTitle("时间");
//                flowInfoShow.setVerticallyTitle(t);
//
//                flowInfoShow.setElem(day);
//                flowInfoShow.setValue(element);
//                try {
//                    showPanel.setViewportView(flowInfoShow.createHistogramPanel());
//                    showPanel.updateUI();
//                }catch ( Exception exp ) { exp.printStackTrace(); }
//
//
//            }
//        });*/
//        editChoose.add(showInfo);
//
//        flowManage.add(editChoose);
//        flowManage.add(showPanel);
//
//		/*JTextArea flowText = new JTextArea();
//		flowText.setOpaque(false);
//
//		flowText.setText("开发中，敬请期待。。。");
//		flowText.setBounds(250, 200, 200, 50);
//		showPanel.add(flowText);*/
//
//		//控制台
//        consol = new JPanel();
//        topTabbedPane.addTab("控制台", null, consol, null);
//        consol.setLayout(new GridLayout(0, 1, 0, 0));
//
//        text = new JTextArea();
//        text.setOpaque(false);
//        text.setLineWrap(true);
//        text.setEditable(false);
//        consol.add(text);
//
//        sys = new JPanel();
//        sys.setLayout(null);
//        sysInfo = new JTextArea();
//        sysInfo.setBounds(150, 85, 500, 230);
//        sys.add(sysInfo);
//        sysInfo.setOpaque(false);
//        sysInfo.setLineWrap(true);
//        sysInfo.setEditable(false);
//        sysInfo.setText("                                                                 \u7F51 \u670D \u4E2D \u5FC3 \u51FA \u54C1\uFF01\r\n\r\n" +
//                "\u8DEF\u7531\u62D3\u6251\uFF1A\u738B\u53CC\u9526\uff0c\u5218\u660c\u5a01       \u961f\u5217\u7ba1\u7406\uff1a\u725b\u7433\u7433       \u670D\u52A1\u63A5\u53E3\uFF1A\u90ED\u6210       \u6570\u636E\u8F6C\u53D1\uFF1A\u9648\u5929\u5B87\r\n\r\n" +
//                "                       \u7BA1\u7406\u5458\uFF1A\u5434\u601D\u9F50\uFF08\u7B2C\u4E00\u7248\uFF09\uFF0C\u81E7\u4E9A\u5F3A\uFF08\u7B2C\u4E8C\u7248\uFF09\uff0c\u97e9\u6ce2\uff08\u7b2c\u4e09\u7248\uff09\r\n\r\n" +
//                "                                                     \u7F51\u7EDC\u6280\u672F\u7814\u7A76\u9662       \u5317\u4EAC\u90AE\u7535\u5927\u5B66");
//        topTabbedPane.addTab("系统Info", null, sys, null);
//
//        INT100 = new JLabel("");
//        INT100.setIcon(new ImageIcon("./img/INT100.png"));
//        INT100.setBounds(145, 250, 106, 105);
//        sys.add(INT100);
//
//        buptImage = new JLabel("");
//        buptImage.setIcon(new ImageIcon("./img/bupt.png"));
//        buptImage.setBounds(270, 246, 532, 110);
//        sys.add(buptImage);
//
//        // 刷新显示当前集群（第一次）
//        reloadAllGroup();
//    }
//
//    // 根据当前所选的主题，显示受限集群
//    public void reflashFbdnGroups() {
//        if (comboBox.getSelectedIndex() != 0 && comboBox.getSelectedItem() != null && !comboBox.getSelectedItem().toString().equals("null")) {
//
//            String selectedTopic = comboBox.getSelectedItem().toString();
//            for (int i = 0; i < topicsPanel.getComponentCount(); i++) {
//                JComboBox topicsChoosed = (JComboBox) topicsPanel.getComponent(i);
//                if (topicsChoosed.getSelectedIndex() != 0 && topicsChoosed.getSelectedItem() != null) {
//                    selectedTopic = selectedTopic + ":" + topicsChoosed.getSelectedItem().toString();
//                } else
//                    break;
//            }
//
//            if (managerStart.getPolicyMap().get(selectedTopic) != null) {
//                java.util.List<String> targetGroups = managerStart.getPolicyMap().get(selectedTopic).getTargetGroups();
//                for (String temp : targetGroups) {
//                    for (int i = 0; i < fbdnGroupsPanel.getComponentCount(); i++) {
//                        JCheckBox group = (JCheckBox) fbdnGroupsPanel.getComponent(i);
//                        if (group.getText().equals(temp))
//                            group.setSelected(true);
//                        fbdnGroupsPanel.repaint();
//                        break;
//                    }
//                }
//            } else {
//                for (int i = 0; i < fbdnGroupsPanel.getComponentCount(); i++) {
//                    JCheckBox group = (JCheckBox) fbdnGroupsPanel.getComponent(i);
//                    group.setSelected(false);
//                }
//                fbdnGroupsPanel.repaint();
//            }
//        }
//    }
//
//    // 刷新显示当前策略信息
//    public void reflashCurrentPolicy() {
//        DefaultTableModel tableModel = (DefaultTableModel) policyTable.getModel();
//        while (tableModel.getRowCount() > 0) {
//            tableModel.removeRow(0);
//        }
//        for (Policy aPolicyList : managerStart.getPolicyMap().values() ) {
//            tableModel.addRow(new Object[]{
//                    aPolicyList.getTargetTopic(),
//                    aPolicyList.getTargetGroups().toString()});
//        }
//        policyTable.invalidate();
//    }
//
//    public void newGroup(final Controller controller) {
//
//        /*GroupInfo aGroupItem = new GroupInfo();
//        aGroupItem.GroupAddress = newGroup.addr;
//        aGroupItem.GroupName = newGroup.name;
//        aGroupItem.date = newGroup.date;
//        aGroupItem.port = newGroup.tPort;
//        int stat = data.addGroup(aGroupItem);
//
//        if (stat == -1) {
//            text.append(new Date() + "已经存在名为" + newGroup.name + "的集群，无法注册" + "\r\n");
//            text.paintImmediately(text.getBounds());
//
//        } else {
//            if (stat == 1) {
//                text.append(new Date() + "集群" + newGroup.name + "代表地址为" + newGroup.addr + "端口号" + newGroup.tPort + "注册成功" + "\r\n");
//                text.paintImmediately(text.getBounds());
//            } else {
//                text.append(new Date() + "注册失败，未知错误!" + "\r\n");
//                text.paintImmediately(text.getBounds());
//            }
//        }
//        groupNames.add( newGroup.name);
//        reloadAllGroup();*/
//
//    }
//
//    public void reflashJtreeRoot() {
//
//        Enumeration trees = topicTreeUI.lib_root.children();
//        Vector<DefaultMutableTreeNode> treesNames = new Vector<>();
//        DefaultMutableTreeNode defaultChose = new DefaultMutableTreeNode("All");
//
//        treesNames.add(defaultChose);
//
//        while (trees.hasMoreElements()) {
//            treesNames.add((DefaultMutableTreeNode) trees.nextElement());
//        }
//
//        chooseTopic.remove(comboBox);
//        comboBox = new JComboBox(treesNames);
//        comboBox.setBounds(10, 5, 124, 45);
//        comboBox.setSelectedIndex(0);
//        comboBox.setOpaque(false);
//        chooseTopic.add(comboBox);
//
//        comboBox.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//
//                if (comboBox_1.getItemCount() > 0) {
//                    comboBox_1.removeAllItems();// 先清除
//                }
//                if (comboBox.getSelectedItem() instanceof String) {
//                    JOptionPane.showMessageDialog(null, "请确认所选的主题是否正确!");
//                } else {
//                    DefaultMutableTreeNode tempNode = (DefaultMutableTreeNode) comboBox.getSelectedItem();
//                    DefaultMutableTreeNode defaultChose = new DefaultMutableTreeNode("All");
//
//                    comboBox_1.addItem(defaultChose);
//                    comboBox_1.setSelectedIndex(0);
//                    if (tempNode != null) {
//                        Enumeration tempNodeEnum = tempNode.children();
//                        while (tempNodeEnum.hasMoreElements()) {
//                            DefaultMutableTreeNode tempTree = (DefaultMutableTreeNode) tempNodeEnum.nextElement();
//                            comboBox_1.addItem(tempTree);
//
//                        }
//                    }
//                    reflashFbdnGroups();
//                }
//            }
//        });
//        comboBox.setName("选择主题树");
//        comboBox.setToolTipText("选择主题树");
//        comboBox.setBorder(BorderFactory.createTitledBorder("选择主题树"));
//        comboBox.setEditable(true);
//        chooseTopic.repaint();
//    }
//
//    public void printAllGroup() {
//        /*java.util.List<GroupInfo> curGroup;
//        curGroup = data.getAllGroup();
//        Iterator<GroupInfo> itr = curGroup.iterator();
//        if (itr.hasNext())
//            text.append(new Date() + "  当前所有集群的信息如下：" + "\r\n");
//        while (itr.hasNext()) {
//            GroupInfo tmpItem = itr.next();
//            text.append("组名称:" + tmpItem.GroupName + "组代表地址：" + tmpItem.GroupAddress + "\r\n");
//            text.paintImmediately(text.getBounds());
//        }*/
//    }
//
//    /**
//     * new added on 2016/8/6 by HanB
//     * */
//    /*public void showException(MsgException exp) {
//        System.out.println("收到设备异常消息!");
//        visualManagement.setSelectedIndex(0);
//        // 逐级查询匹配，凡是符合条件背景设为红色以示告警
//        int groupCount = allGroupsPane.getComponentCount();
//        for (int i = 0; i < groupCount; i ++) {
//            JButton g = (JButton) allGroupsPane.getComponent(i);
//            if (g.getText().equals(exp.groupName)) {
//                g.setBackground(Color.RED);
//                g.doClick();
//                groupsInfoTabbedPane.setSelectedIndex(1);
//
//                int switchCount = switchMapPanel.getComponentCount();
//                for (int j = 0; j < switchCount; j ++) {
//                    if (switchMapPanel.getComponent(j) instanceof JButton) {
//                        JButton s = (JButton) switchMapPanel.getComponent(j);
//                        if (s.getText().equals(exp.switchID)) {
//                            s.setBackground(Color.RED);
//                            s.doClick();
//
//                            int hostCount = switchHostPanel.getComponentCount();
//                            for (int k = 0; k < hostCount; k ++) {
//                                JButton h = (JButton)switchHostPanel.getComponent(k);
//                                if (h.getText().equals(exp.hostAddr)) {
//                                    h.setBackground(Color.RED);
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        msgExceptions.add(exp);
//    }*/
//
//    public void reloadAllGroup() {
//
//        //清空所有集群面板和受限集群面板
//        if (allGroupsPane != null) allGroupsPane.removeAll();
//        if (fbdnGroupsPanel != null) fbdnGroupsPanel.removeAll();
//
//        if (manager.getGroups() != null && manager.getGroups().size() > 0) {
//
//            for (final String groupName : manager.getGroups().keySet()) { // 遍历当前所有集群
//                final JButton groupButton = new JButton(groupName);
//                currentGroup = groupName;
//                groupButton.setToolTipText(groupName);
//
//                groupButton.setPreferredSize(new Dimension(80, 80));
//                groupButton.setHorizontalTextPosition(SwingConstants.CENTER);
//                groupButton.setVerticalTextPosition(SwingConstants.BOTTOM);
//                allGroupsPane.add(groupButton);
//                groupButton.setIcon(new ImageIcon("./img/group.png"));
//
//                final JFrame groupFlow = new JFrame();
//                final JPopupMenu groupPopupMenu = new JPopupMenu();//集群按钮右键菜单
//                final JMenuItem groupFlowInfoItem = new JMenuItem("群内流量");
//                groupPopupMenu.add(groupFlowInfoItem);
//                groupButton.addMouseListener(new MouseAdapter() {
//                    public void mouseClicked(MouseEvent e) {
//                        if (e.getButton() == MouseEvent.BUTTON1) {
//                            // 移除跨级信息
//                            subsTable.removeAll();
//                            ports.removeAll();
//                            ports.repaint();
//                            queues.removeAll();
//                            queues.repaint();
//                            //currentGroupName = currentGroup;
//
//                            //String groupControllerName = manager.getGroups().get(currentGroup).getLocalAddr();
//                            //Controller groupController = globalUtil.getController(groupControllerAddr);
//
//                            //添加非空判断
//                            if (currentGroup == null) {
//                                controller.removeAll();
//                                switchMapPanel.removeAll();
//                                switchHostPanel.removeAll();
//                                groupsInfoTabbedPane.setTitleAt(0, ("控制器信息"));
//                                groupsInfoTabbedPane.setTitleAt(1, ("设备信息"));
//                                groupsInfoTabbedPane.setTitleAt(2, ("订阅信息"));
//                                groupsInfoTabbedPane.setTitleAt(3, ("端口信息"));
//                                groupsInfoTabbedPane.setTitleAt(4, ("队列信息"));
//                            }
//                            else {
//                                groupsInfoTabbedPane.setTitleAt(0, ("集群" + currentGroup + "控制器"));
//                                groupsInfoTabbedPane.setTitleAt(1, ("集群" + currentGroup + "设备"));
//                                groupsInfoTabbedPane.setTitleAt(2, ("集群" + currentGroup + "订阅"));
//                                groupsInfoTabbedPane.setTitleAt(3, ("集群" + currentGroup + "端口"));
//                                groupsInfoTabbedPane.setTitleAt(4, ("集群" + currentGroup + "队列"));
//                                //添加交换机和控制器
//                                if (controller != null) {
//                                    controller.removeAll();
//                                }
//                                JTextArea ctrlInfo = new JTextArea();
//                                ctrlInfo.setBounds(170,40,300,30);
//                                ctrlInfo.setOpaque(false);
//                                ctrlInfo.setLineWrap(true);
//                                ctrlInfo.setEditable(false);
//                                ctrlInfo.setText("集群" + currentGroup + "当前控制器URL: " + currentGroup);
//                                controller.add(ctrlInfo);
//
//                                JLabel opendaylight = new JLabel("");
//                                opendaylight.setIcon(new ImageIcon("./img/odl.png"));
//                                opendaylight.setBounds(100,100,400,100);
//                                controller.add(opendaylight);
//
//                                //交换机
//                                if (switchMapPanel != null)
//                                    switchMapPanel.removeAll();
//                                switchMapLabel.setText("集群" + currentGroup + "交换机");
//
//                                //获取集群所有交换机和群内拓扑
//                                final Map<String, Switch> groupSwitchMap = manager.getGroups().get(currentGroup).getSwitches();
//                                //globalUtil.getTopology(groupController);
//                                //groupSubs = adminMgr.getGroupSubscriptions(currentGroup); // 获取集群订阅，必须放在获取拓扑之后
//
//                                for (final String switchName : groupSwitchMap.keySet()) {
//                                    final JButton groupSwitchButton = new JButton("*" + switchName.substring(switchName.length() - 3));
//                                    groupSwitchButton.setToolTipText("Group " + currentGroup + "'s Switch:" + switchName);
//                                    groupSwitchButton.setPreferredSize(new Dimension(50, 50));
//                                    groupSwitchButton.setHorizontalTextPosition(SwingConstants.CENTER);
//                                    groupSwitchButton.setVerticalTextPosition(SwingConstants.BOTTOM);
//                                    groupSwitchButton.setIcon(new ImageIcon("./img/switch.png"));
//                                    switchMapPanel.add(groupSwitchButton);
//                                    groupSwitchButton.setToolTipText("点击左键查询交换机下主机，右键查看设备信息");
//                                    //final Controller finalGroupController = manager.getGroups().get(currentGroup);
//
//                                    final JFrame flowInfo = new JFrame();
//                                    final JPopupMenu switchPopupMenu = new JPopupMenu(); //交换机按钮右键菜单
//                                    final JMenuItem portInfoMenu = new JMenuItem("端口信息"),
//                                            flowInfoMenu = new JMenuItem("流量信息");
//                                    switchPopupMenu.add(portInfoMenu);
//                                    switchPopupMenu.add(flowInfoMenu);
//                                    groupSwitchButton.addMouseListener(new MouseAdapter() {
//                                        @Override
//                                        public void mouseClicked(MouseEvent e) {
//                                            if (e.getButton() == MouseEvent.BUTTON1) {
//                                                if (switchHostPanel != null)
//                                                    switchHostPanel.removeAll();
//                                                if (queues != null)
//                                                    queues.removeAll();
//                                                switchHostScrollPane.repaint();
//                                                switchHostLabel.setText("交换机" + groupSwitchButton.getText() + "下主机");
//                                                Map<String, Host> wsnHostMap = manager.getGroups().get(currentGroup).getSwitches().get(switchName).getHosts();
//                                                for (final Host host : wsnHostMap.values()) {
//                                                    final String memAddr = host.getIp();
//                                                    final JButton hostButton = new JButton(memAddr);
//                                                    hostButton.setToolTipText(memAddr);
//                                                    switchHostPanel.add(hostButton);
//                                                    hostButton.setIcon(new ImageIcon("./img/rep.png"));
//                                                   /* hostButton.setToolTipText("点击左键查询该成员订阅，右键查看主机信息");
//
//                                                    // 显示主机详细信息
//                                                    hostButton.addMouseListener(new MouseAdapter() {
//                                                        @Override
//                                                        public void mouseClicked(MouseEvent e) {
//                                                            if (e.getButton() == MouseEvent.BUTTON1) {
//                                                                DefaultTableModel memSubsModel;
//                                                                String[] colHeader = {"集群" + currentGroup + "成员" + memAddr + "的订阅"};
//                                                                String[][] memSubInfo;
//                                                                //ArrayList<String> memSubs = groupSubs.get(memAddr);
//                                                                ArrayList<String> memSubs = host.getSubTopics();
//                                                                if (memSubs != null && memSubs.size() > 0) {
//                                                                    memSubInfo = new String[memSubs.size()][1];
//                                                                    for (int i = 0; i < memSubs.size(); i ++) {
//                                                                        memSubInfo[i][0] = memSubs.get(i);
//                                                                    }
//                                                                    memSubsModel = new DefaultTableModel(memSubInfo,colHeader);
//                                                                    subsTable.removeAll();
//                                                                    subsTable.setModel(memSubsModel);
//                                                                    groupsInfoTabbedPane.setSelectedIndex(2);
//                                                                }else {
//                                                                    JOptionPane.showMessageDialog(null, "该成员未订阅任何主题！");
//                                                                }
//                                                            }
//                                                            if (e.getButton() == MouseEvent.BUTTON3) {
//                                                                JFrame f = new JFrame();
//                                                                SnmpMgr snmpMgr = new SnmpMgr();
//                                                                HostInfo hostInfo = snmpMgr.getHostInfo(host);
//                                                                f.setTitle("设备信息");
//                                                                f.setBounds(e.getXOnScreen(), e.getYOnScreen(), 230, 280);
//                                                                f.setVisible(true);
//                                                                JTextArea t = new JTextArea();
//                                                                String excpDes = "暂无";
//                                                                for (MsgException excp : msgExceptions) {
//                                                                    if (excp.groupName.equals(currentGroup) &&
//                                                                            excp.switchID.equals(groupSwitchButton.getText()) &&
//                                                                            excp.hostAddr.equals(host.getIpAddr())) {
//                                                                        groupButton.setBackground(Color.RED);
//                                                                        groupSwitchButton.setBackground(Color.RED);
//                                                                        hostButton.setBackground(Color.RED);
//                                                                        excpDes = excp.excpDescrip;
//                                                                        break;
//                                                                    }
//                                                                }
//                                                                t.setText("设备描述：" + hostInfo.getSysInfo() + "\r\n"
//                                                                        + "设备名称：" + hostInfo.getSysName() + "\r\n"
//                                                                        + "IP地址：" + host.getIpAddr() + "\r\n"
//                                                                        + "MAC地址：" + host.getMac() + "\r\n"
//                                                                        + "系统内存(KB)：" + hostInfo.getSysMemory() + "\r\n"
//                                                                        + "CPU利用率(%)：" + hostInfo.getCpuRate() + "\r\n"
//                                                                        + "网络接口数：" + hostInfo.getIfNum() + "\r\n"
//                                                                        + "交换机连接端口：" + host.getPort() + "\r\n"
//                                                                        + "最后发现时间：" + host.getLastSeen() + "\r\n"
//                                                                        + "设备异常：" + excpDes);
//                                                                t.setEditable(false);
//                                                                t.setOpaque(false);
//                                                                t.setLineWrap(true);
//                                                                f.add(t);
//                                                            }
//                                                        }
//                                                    });*/
//                                                }
//                                            }
//                                            if (e.getButton() == MouseEvent.BUTTON3) {
//                                                switchPopupMenu.show(groupSwitchButton,e.getX(),e.getY());
//                                                portInfoMenu.addActionListener(new ActionListener() {
//                                                    @Override
//                                                    public void actionPerformed(ActionEvent e) {
//                                                        //加载交换机端口信息
//                                                        if (ports != null)
//                                                            ports.removeAll();
//                                                        String[] pHeader = {"端口名称", "端口ID", "所属交换机", "进端口数据(Byte)", "出端口数据(Byte)"};
//                                                        Set<String> pList = manager.getGroups().get(currentGroup).getSwitches().get(switchName).getPorts();
//                                                        Object[][] pInfo = new Object[pList.size()][5];
//                                                        int pindex = 0;
//                                                        /*final Port[] pArray = new Port[pList.size()];
//                                                        for (final Port p : pList) { // 遍历每个端口
//                                                            pInfo[pindex][0] = p.getPortName();
//                                                            pInfo[pindex][1] = p.getTpid();
//                                                            pInfo[pindex][2] = p.getSwitchId();
//                                                            pInfo[pindex][3] = p.getEnport();
//                                                            pInfo[pindex][4] = p.getDeport();
//                                                            pArray[pindex] = p;
//                                                            pindex++;
//                                                        }*/
//                                                        portTable = new JTable(pInfo, pHeader) {
//                                                            public boolean isCellEditable(int row, int column) {
//                                                                return false; //表格不允许被编辑
//                                                            }
//                                                        };
//                                                        portTable.setPreferredScrollableViewportSize(new Dimension(620, 220));
//                                                        FitTableColumns(portTable); // 列宽自适应
//                                                        portTable.setRowHeight(23); // 行高
//                                                        DefaultTableCellRenderer r = new DefaultTableCellRenderer();
//                                                        r.setHorizontalAlignment(JLabel.CENTER);
//                                                        portTable.setDefaultRenderer(Object.class, r); // 文字居中
//                                                        JScrollPane pScrollPane = new JScrollPane(portTable);
//                                                        ports.add(pScrollPane, BorderLayout.CENTER);
//
//                                                        //添加行响应事件获取端口上的队列
//                                                        portTable.addMouseListener(new MouseAdapter() {
//                                                            public void mouseClicked(MouseEvent e) {
//                                                                int row = portTable.getSelectedRow();
//                                                                // 加载队列信息
//                                                                if (queues != null)
//                                                                    queues.removeAll();
//                                                                String[] qHeader = {"队列名称", "所属端口", "进队列数据(Byte)", "出队列数据(Byte)", "当前队列长度", "队列带宽"};
//                                                                Map<Integer, java.util.List<Queue>> qList = manager.getGroups().get(currentGroup).getSwitches().get(switchName).getQueues();
//                                                                Object[][] qInfo = new Object[qList.size()][6];
//                                                                int qindex = 0;
//                                                                /*for (Queue q : qList) { // 遍历端口上每个队列
//                                                                    qInfo[qindex][0] = q.getQueueName();
//                                                                    qInfo[qindex][1] = q.getPortId();
//                                                                    qInfo[qindex][2] = q.getEnqueue();
//                                                                    qInfo[qindex][3] = q.getDequeue();
//                                                                    qInfo[qindex][4] = q.getQueueLength();
//                                                                    qInfo[qindex][5] = q.getBrandWidth();
//                                                                    qindex++;
//                                                                }*/
//                                                                queueTable = new JTable(qInfo, qHeader) {
//                                                                    public boolean isCellEditable(int row, int column) {
//                                                                        return false; //表格不允许被编辑
//                                                                    }
//                                                                };
//                                                                queueTable.setPreferredScrollableViewportSize(new Dimension(620, 220));
//                                                                FitTableColumns(queueTable); // 列宽自适应
//                                                                queueTable.setRowHeight(23); // 行高
//                                                                DefaultTableCellRenderer r = new DefaultTableCellRenderer();
//                                                                r.setHorizontalAlignment(JLabel.CENTER);
//                                                                queueTable.setDefaultRenderer(Object.class, r); // 文字居中
//                                                                JScrollPane qScrollPane = new JScrollPane(queueTable);
//                                                                queues.add(qScrollPane, BorderLayout.CENTER);
//
//                                                                groupsInfoTabbedPane.setSelectedIndex(4);
//                                                            }
//
//                                                        });
//                                                        groupsInfoTabbedPane.setSelectedIndex(3);
//                                                    }
//                                                });
//                                                flowInfoMenu.addMouseListener(new MouseAdapter() {
//                                                    @Override
//                                                    public void mouseClicked(MouseEvent e) {
//
//                                                       /* FlowInfoShow flowInfoShow = new FlowInfoShow();// 柱状图的panel
//                                                        flowInfoShow.setTitle("流量统计柱形图");
//                                                        flowInfoShow.setHorizontalTitle("时间");
//                                                        flowInfoShow.setVerticallyTitle("数据量(byte)");
//
//                                                        ArrayList<String> elem = new ArrayList<>();
//                                                        ArrayList<Double> value = new ArrayList<>();
//                                                        ResultSet rs = DataCollect.selectFlowInfoByDPID(switchName);
//
//                                                        //添加记录
//                                                        try {
//                                                            while ( rs.next() ) {
//                                                                elem.add(rs.getString("lastseen").substring(0,8));
//                                                                value.add(rs.getDouble("flowCount"));
//                                                            }
//                                                        }catch ( Exception exp ) { exp.printStackTrace(); }
//
//                                                        flowInfoShow.setElem(elem);
//                                                        flowInfoShow.setValue(value);
//
//                                                        flowInfo.setTitle("交换机"+ switchName +"流量信息");
//                                                        flowInfo.setBounds(screenSize.width / 2 - 362, screenSize.height / 2 - 240, 725, 480);
//                                                        JScrollPane scollpane = new JScrollPane(new JPanel());
//                                                        flowInfo.add(scollpane);
//                                                        flowInfo.setResizable(false);
//                                                        flowInfo.setVisible(true);
//                                                        flowInfo.setDefaultCloseOperation(1);
//
//                                                        try {
//                                                            scollpane.setViewportView(flowInfoShow.createHistogramPanel());
//                                                            scollpane.updateUI();
//                                                        }catch ( Exception exp ) { exp.printStackTrace(); }*/
//                                                    }
//                                                });
//                                            }
//                                        }
//                                    });
//                                }
//                            }
//
//                            //加载集群订阅信息
//                            //ArrayList<String> subers = new ArrayList<>();//所有订阅用户地址
//                            if (topomgr.getLsdb().getLSDB().get(currentGroup).getSubsTopics() != null) {
//                                int count = topomgr.getLsdb().getLSDB().get(currentGroup).getSubsTopics().size();
//                                String[][] groupSubData = new String[count][1];
//                                for (int i = 0; i < count; i ++) {
//                                    groupSubData[i][1] =  topomgr.getLsdb().getLSDB().get(currentGroup).getSubsTopics().get(i);
//                                }
//                                String[] columnNames = {"集群" + currentGroup + "的所有订阅"};
//                                groupSubsModel = new DefaultTableModel(groupSubData, columnNames);
//                                subsTable.removeAll();
//                                subsTable.setModel(groupSubsModel);
//                                subsTable.repaint();
//                            }
//                        }
//                        //鼠标右键
//                        if (e.getButton() == 3) {
//                            groupPopupMenu.show(groupButton,e.getX(),e.getY());
//                            groupFlowInfoItem.addMouseListener(new MouseAdapter() {
//                                @Override
//                                public void mouseReleased(MouseEvent e2) {
//                                    groupFlow.setTitle("集群内流量");
//                                    groupFlow.setBounds(screenSize.width / 2 - 362, screenSize.height / 2 - 240, 725, 480);
//                                    groupFlow.setResizable(false);
//                                    groupFlow.setVisible(true);
//                                    groupFlow.setDefaultCloseOperation(1);
//                                }
//                            });
//                        }
//                    }
//                });
//
//                //向受限集群面板中添加
//                final JCheckBox groups = new JCheckBox(groupName);
//                groups.setSelected(false);
//                groups.addActionListener(new ActionListener() {
//                    public void actionPerformed(ActionEvent e) {
//                        if (groups.isSelected()) {
//                            currentTargetGroups.add(currentGroup);
//                        } else {
//                            for (String temTG : currentTargetGroups) {
//                                if (temTG.equals(currentGroup)) {
//                                    currentTargetGroups.remove(temTG);
//                                    break;
//                                }
//                            }
//                        }
//                        System.out.print("currentGroups:" + currentTargetGroups);
//                    }
//                });
//                if (fbdnGroupsPanel != null) {
//                    fbdnGroupsPanel.add(groups);
//                }
//
//
//
//
//            }
//        }
//        // 加载群间拓扑
////		JPanel temp = null;
////		while (temp == null ) {
////			temp = new JBroswer().getBroswer("D:\\Pub-Sub-System\\flow-master\\src\\main\\java\\com\\bupt\\wangfu\\mgr\\design\\webtopo\\index.html");
////		}
////		topoManage.add(temp);
//        //printAllGroup();
//    }
//
//    //设置表格列宽自适应方法
//    public void FitTableColumns(JTable myTable) {
//        JTableHeader header = myTable.getTableHeader();
//        int rowCount = myTable.getRowCount();
//        Enumeration columns = myTable.getColumnModel().getColumns();
//        while (columns.hasMoreElements()) {
//            TableColumn column = (TableColumn) columns.nextElement();
//            int col = header.getColumnModel().getColumnIndex(column.getIdentifier());
//            int width = (int) myTable.getTableHeader().getDefaultRenderer()
//                    .getTableCellRendererComponent(myTable, column.getIdentifier()
//                            , false, false, -1, col).getPreferredSize().getWidth();
//            for (int row = 0; row < rowCount; row++) {
//                int preferedWidth = (int) myTable.getCellRenderer(row, col).getTableCellRendererComponent(myTable,
//                        myTable.getValueAt(row, col), false, false, row, col).getPreferredSize().getWidth();
//                width = Math.max(width, preferedWidth);
//            }
//            header.setResizingColumn(column); // 此行很重要
//            column.setWidth(width + myTable.getIntercellSpacing().width);
//        }
//    }
//}
