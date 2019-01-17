package edu.bupt.wangfu.role.user.publish;

import edu.bupt.wangfu.role.user.util.RTPpacket;
import edu.bupt.wangfu.test.PropertiesTest;
import sun.misc.BASE64Encoder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Scanner;

import static edu.bupt.wangfu.module.util.Constant.FILE;

public class MyPublisher {
    int num = 0;
    JFrame f = new JFrame("Server");
    JButton setupButton = new JButton("Setup");
    JButton textButton = new JButton("Text");
    JButton videoButton = new JButton("Video");
    JPanel buttonPanel = new JPanel();
    JPanel mainPanel = new JPanel();
    JLabel statLabel1 = new JLabel();
    JLabel statLabel2 = new JLabel();
    JLabel statLabel3 = new JLabel();
    public JLabel iconLabel = new JLabel();
    public ImageIcon icon = new ImageIcon();

    //存储视频帧数据
    byte[] buf;

    //保存当前播放帧数
    int imagenb;

    //视频总大小
    int VIDEO_LENGTH = 500;

    //RTP payload type for MJPEG video
    int MJPEG_TYPE = 26;

    //Frame period of the video to stream, in ms
    int FRAME_PERIOD = 100;

    //文本传输计数器
    public static int count = 1;

    public static Trans trans;

    public MyPublisher() {
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        buf = new byte[20000];

        //Buttons
        buttonPanel.setLayout(new GridLayout(1,0));
        buttonPanel.add(setupButton);
        buttonPanel.add(textButton);
        buttonPanel.add(videoButton);
        setupButton.addActionListener(new setupButtonListener());
        textButton.addActionListener(new textButtonListener());
        videoButton.addActionListener(new videoButtonListener());

        //frame layout
        mainPanel.setLayout(null);
        mainPanel.add(iconLabel);
        mainPanel.add(buttonPanel);
        mainPanel.add(statLabel1);
        mainPanel.add(statLabel2);
        mainPanel.add(statLabel3);
        iconLabel.setBounds(0,0,380,280);
        buttonPanel.setBounds(0,280,380,50);
        statLabel1.setBounds(0,330,380,20);
        statLabel2.setBounds(0,350,380,20);
        statLabel3.setBounds(0,370,380,20);

        f.getContentPane().add(mainPanel, BorderLayout.CENTER);
        f.setSize(new Dimension(380,420));
        f.setVisible(true);

        Scanner in = new Scanner(System.in);
        while (in.hasNext()) {
            num = in.nextInt();
            System.out.println("num: " + num);
        }
    }

    class setupButtonListener implements ActionListener {

        public synchronized void actionPerformed(ActionEvent e){
            System.out.println("Setup Button pressed !");
            if (trans == null) {
                trans = new Trans();
            }
        }
    }

    class textButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            System.out.println("Text Button pressed!");
            if (trans == null) {
                System.out.println("请先注册用户！");
            }else {
                trans.sendTest(num);
            }
        }
    }

    class videoButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            System.out.println("Video Button pressed!");
            if (trans == null) {
                System.out.println("请先注册用户！");
            }else {
                play(FILE);
            }
        }
    }

    public void play(String fileName) {
        VideoStream video = null;
        try {
            video = new VideoStream(fileName);
        } catch (Exception e) {
            System.out.println("文件不存在");
        }
        while (imagenb < VIDEO_LENGTH) {
            imagenb++;
            try {
                int image_length = video.getnextframe(buf);
                RTPpacket rtp_packet = new RTPpacket(MJPEG_TYPE, imagenb, imagenb * FRAME_PERIOD,
                        buf, image_length);
                int packet_length = rtp_packet.getlength();
                byte[] packet_bits = new byte[packet_length];
                rtp_packet.getpacket(packet_bits);
                BASE64Encoder encoder = new BASE64Encoder();
                trans.sendMethod("rtp--" + encoder.encode(packet_bits));
            }catch(Exception ex) {
                System.out.println("视频播放出错！");
                ex.printStackTrace();
                System.exit(0);
            }
        }
    }

    public static void main(String[] args) {
        //更新constant 类中的属性值
        PropertiesTest.refreshPro();
        new MyPublisher();
    }
}
