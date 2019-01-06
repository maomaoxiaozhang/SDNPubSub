package edu.bupt.wangfu.role.user.subscribe;

import edu.bupt.wangfu.test.PropertiesTest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayDeque;

public class MySubscriber {
    JFrame f = new JFrame("Server");

    JButton setupButton = new JButton("Setup");

    JPanel buttonPanel = new JPanel();
    JPanel mainPanel = new JPanel();
    JLabel statLabel1 = new JLabel();
    JLabel statLabel2 = new JLabel();
    JLabel statLabel3 = new JLabel();
    public JLabel iconLabel = new JLabel();
    public ImageIcon icon = new ImageIcon();
    Timer timer;
    public static FrameSynchronizer fsynch;

    public static Trans trans;

    public MySubscriber() {
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        //Buttons
        buttonPanel.setLayout(new GridLayout(1,0));
        buttonPanel.add(setupButton);
        setupButton.addActionListener(new setupButtonListener());

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

        //init timer
        //--------------------------
        timer = new Timer(100, new timerListener());
        timer.setInitialDelay(0);
        timer.setCoalesce(true);
        timer.start();

        fsynch = new FrameSynchronizer(100);

        f.getContentPane().add(mainPanel, BorderLayout.CENTER);
        f.setSize(new Dimension(380,420));
        f.setVisible(true);
    }

    class setupButtonListener implements ActionListener {

        public synchronized void actionPerformed(ActionEvent e){
            System.out.println("Setup Button pressed !");
            if (trans == null) {
                trans = new Trans();
            }
        }
    }

    class timerListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            Image image = fsynch.nextFrame();
            if (image != null) {
                icon = new ImageIcon(image);
                iconLabel.setIcon(icon);
            }
        }
    }

    public class FrameSynchronizer {

        private ArrayDeque<Image> queue;
        private int bufSize;
        private int curSeqNb;
        private Image lastImage;

        public FrameSynchronizer(int bsize) {
            curSeqNb = 1;
            bufSize = bsize;
            queue = new ArrayDeque<>(bufSize);
        }

        //synchronize frames based on their sequence number
        public void addFrame(Image image, int seqNum) {
            if (lastImage == null) {
                curSeqNb = seqNum;
                lastImage = image;
            }else {
                if (seqNum < curSeqNb) {
                    queue.add(lastImage);
                }
                else if (seqNum > curSeqNb) {
                    for (int i = curSeqNb; i < seqNum; i++) {
                        queue.add(lastImage);
                    }
                    queue.add(image);
                }
                else {
                    queue.add(image);
                }
            }
        }

        //get the next synchronized frame
        public Image nextFrame() {
            if (queue.size() == 0)
                return null;
            else {
                curSeqNb++;
                lastImage = queue.peekLast();
                return queue.remove();
            }
        }
    }

    public static void main(String[] args) {
        //更新constant 类中的属性值
        PropertiesTest.refreshPro();
        new MySubscriber();
    }
}
