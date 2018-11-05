package edu.bupt.wangfu.role.user.subscribe;/* ------------------
   Client
   usage: java Client [Server hostname] [Server RTSP listening port] [Video file requested]
   ---------------------- */

import edu.bupt.wangfu.role.user.util.RTCPpacket;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.ArrayDeque;
import java.util.Random;

public class Subscriber {
    //GUI
    //----
    JFrame f = new JFrame("Client");
    JButton setupButton = new JButton("Setup");
    JButton playButton = new JButton("Play");
    JButton pauseButton = new JButton("Pause");
    JButton tearButton = new JButton("Close");
    JButton describeButton = new JButton("Session");
    JPanel mainPanel = new JPanel();
    JPanel buttonPanel = new JPanel();
    JLabel statLabel1 = new JLabel();
    JLabel statLabel2 = new JLabel();
    JLabel statLabel3 = new JLabel();
    public JLabel iconLabel = new JLabel();
    public ImageIcon icon = new ImageIcon();

    //RTP variables:
    //----------------
//    DatagramPacket rcvdp;            //UDP packet received from the server
//    DatagramSocket RTPsocket;        //socket to be used to send and receive UDP packets
//    int RTP_RCV_PORT = 25000; //port where the client will receive the RTP packets
    
    public Timer timer; //timer used to receive data from the UDP socket
    byte[] buf;  //buffer used to store data received from the server 
   
    //RTSP variables
    //----------------
    //rtsp states 
    public final static int INIT = 0;
    public final static int READY = 1;
    public final static int PLAYING = 2;
    public int state;            //RTSP state == INIT or READY or PLAYING
    Socket RTSPsocket;           //socket used to send/receive RTSP messages
    InetAddress ServerIPAddr;

    //input and output stream filters
//    static BufferedReader RTSPBufferedReader;
//    static BufferedWriter RTSPBufferedWriter;
  //video file to request to the server
    static String VideoFileName = "movie.Mjpeg"; 
    int RTSPSeqNb = 0;           //Sequence number of RTSP messages within the session
    String RTSPid;              // ID of the RTSP session (given by the RTSP Server)

    final static String CRLF = "\r\n";
    final static String DES_FNAME = "session_info.txt";

    //RTCP variables
    //----------------
    DatagramSocket RTCPsocket;          //UDP socket for sending RTCP packets
//    static int RTCP_RCV_PORT = 19001;   //port where the client will receive the RTP packets
//    static int RTCP_PERIOD = 400;       //How often to send RTCP packets
    RtcpSender rtcpSender;

    //Video constants:
    //------------------
    static int MJPEG_TYPE = 26; //RTP payload type for MJPEG video

    //Statistics variables:
    //------------------
    double statDataRate;        //Rate of video data received in bytes/s
    int statTotalBytes;         //Total number of bytes received in a session
    double statStartTime;       //Time in milliseconds when start is pressed
    double statTotalPlayTime;   //Time in milliseconds of video playing since beginning
    float statFractionLost;     //Fraction of RTP data packets from sender lost since the prev packet was sent
    int statCumLost;            //Number of packets lost
    int statExpRtpNb;           //Expected Sequence number of RTP messages within the session
    int statHighSeqNb;          //Highest sequence number received in session

    public FrameSynchronizer fsynch;
//
//    /*
//     * 客户端不能为单例模式
//     * 此处不能使用懒加载模式
//     */
//    public static Subscriber getInstance() {
//    	return new Subscriber();
//    }
    
   
    //--------------------------
    //Constructor
    //--------------------------
    public Subscriber() {

        //build GUI
        //--------------------------
     
        //Frame
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        //Buttons
        buttonPanel.setLayout(new GridLayout(1,0));
        buttonPanel.add(setupButton);
        buttonPanel.add(playButton);
        buttonPanel.add(pauseButton);
        buttonPanel.add(tearButton);
        buttonPanel.add(describeButton);
        setupButton.addActionListener(new setupButtonListener());
        playButton.addActionListener(new playButtonListener());
        pauseButton.addActionListener(new pauseButtonListener());
        tearButton.addActionListener(new tearButtonListener());
        describeButton.addActionListener(new describeButtonListener());

        //Statistics
        statLabel1.setText("Total Bytes Received: 0");
        statLabel2.setText("Packets Lost: 0");
        statLabel3.setText("Data Rate (bytes/sec): 0");

        //Image display label
        iconLabel.setIcon(null);
        
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

        //init timer
        //--------------------------
        timer = new Timer(100, new timerListener());
        timer.setInitialDelay(0);
        timer.setCoalesce(true);

        //allocate enough memory for the buffer used to receive data from the server
        buf = new byte[15000];    

        //create the frame synchronizer
        fsynch = new FrameSynchronizer(100);
        state = INIT;
    }

    //------------------------------------
    //main
    //------------------------------------
    public static Trans trans = new Trans();
    public static String fileName = "C:\\Users\\lenovo\\Desktop\\RTSP-Client-Server-master\\movie.Mjpeg";
    public static void main(String argv[]) throws Exception {
    	if (argv.length != 0)
    		fileName = argv[0];
    	new Subscriber();
    }


    //------------------------------------
    //Handler for buttons
    //------------------------------------

    //Handler for Setup button
    //-----------------------
    class setupButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent e){
            System.out.println("Setup Button pressed !");
            trans.sendMethod("rtcp--setup");
        }
    }
    
    //Handler for Play button
    //-----------------------
    class playButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            System.out.println("Play Button pressed!");
            trans.sendMethod("rtcp--play--" + fileName + "--" + trans.getUser());
        }
    }

    //Handler for Pause button
    //-----------------------
    class pauseButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent e){

            System.out.println("Pause Button pressed!");   
            trans.sendMethod("rtcp--pause");
        }
    }

    //Handler for Teardown button
    //-----------------------
    class tearButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent e){

            System.out.println("Teardown Button pressed !");  
            trans.sendMethod("rtcp--teardown");
        }
    }

    // Get information about the data stream
    class describeButtonListener implements ActionListener {

    	public void actionPerformed(ActionEvent e){

            System.out.println("Describe Button pressed !");  
            trans.sendMethod("rtcp--describe");
        }
    }

    //------------------------------------
    //Handler for timer
    //------------------------------------
    class timerListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
        	
        	Image image = fsynch.nextFrame();
        	if (image != null) {
        		icon = new ImageIcon(image);
        		iconLabel.setIcon(icon);
        	}        		
        }
    }

    //------------------------------------
    // Send RTCP control packets for QoS feedback
    //------------------------------------
    class RtcpSender implements ActionListener {

        private Timer rtcpTimer;
        int interval;

        // Stats variables
        private int numPktsExpected;    // Number of RTP packets expected since the last RTCP packet
        private int numPktsLost;        // Number of RTP packets lost since the last RTCP packet
        private int lastHighSeqNb;      // The last highest Seq number received
        private int lastCumLost;        // The last cumulative packets lost
        private float lastFractionLost; // The last fraction lost

        Random randomGenerator;         // For testing only

        public RtcpSender(int interval) {
            this.interval = interval;
            rtcpTimer = new Timer(interval, this);
            rtcpTimer.setInitialDelay(0);
            rtcpTimer.setCoalesce(true);
            randomGenerator = new Random();
        }

        public void run() {
            System.out.println("RtcpSender Thread Running");
        }

        public void actionPerformed(ActionEvent e) {

            // Calculate the stats for this period
            numPktsExpected = statHighSeqNb - lastHighSeqNb;
            numPktsLost = statCumLost - lastCumLost;
            lastFractionLost = numPktsExpected == 0 ? 0f : (float)numPktsLost / numPktsExpected;
            lastHighSeqNb = statHighSeqNb;
            lastCumLost = statCumLost;

            //To test lost feedback on lost packets
            // lastFractionLost = randomGenerator.nextInt(10)/10.0f;

            RTCPpacket rtcp_packet = new RTCPpacket(lastFractionLost, statCumLost, statHighSeqNb);
            int packet_length = rtcp_packet.getlength();
            byte[] packet_bits = new byte[packet_length];
            rtcp_packet.getpacket(packet_bits);

//            try {
//                DatagramPacket dp = new DatagramPacket(packet_bits, packet_length, ServerIPAddr, RTCP_RCV_PORT);
//                RTCPsocket.send(dp);
//            } catch (InterruptedIOException iioe) {
//                System.out.println("Nothing to read");
//            } catch (IOException ioe) {
//                System.out.println("Exception caught: "+ioe);
//            }
        }

        // Start sending RTCP packets
        public void startSend() {
            rtcpTimer.start();
        }

        // Stop sending RTCP packets
        public void stopSend() {
            rtcpTimer.stop();
        }
    }

    //------------------------------------
    //Synchronize frames
    //------------------------------------
    public class FrameSynchronizer {

        private ArrayDeque<Image> queue;
        private int bufSize;
        private int curSeqNb;
        private Image lastImage;

        public FrameSynchronizer(int bsize) {
            curSeqNb = 1;
            bufSize = bsize;
            queue = new ArrayDeque<Image>(bufSize);
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

    //------------------------------------
    //Parse Server Response
    //------------------------------------
//    private int parseServerResponse() {
//        int reply_code = 0;
//
//        try {
//            //parse status line and extract the reply_code:
//            String StatusLine = RTSPBufferedReader.readLine();
//            System.out.println("RTSP Client - Received from Server:");
//            System.out.println(StatusLine);
//          
//            StringTokenizer tokens = new StringTokenizer(StatusLine);
//            tokens.nextToken(); //skip over the RTSP version
//            reply_code = Integer.parseInt(tokens.nextToken());
//            
//            //if reply code is OK get and print the 2 other lines
//            if (reply_code == 200) {
//                String SeqNumLine = RTSPBufferedReader.readLine();
//                System.out.println(SeqNumLine);
//                
//                String SessionLine = RTSPBufferedReader.readLine();
//                System.out.println(SessionLine);
//
//                tokens = new StringTokenizer(SessionLine);
//                String temp = tokens.nextToken();
//                //if state == INIT gets the Session Id from the SessionLine
//                if (state == INIT && temp.compareTo("Session:") == 0) {
//                    RTSPid = tokens.nextToken();
//                }
//                else if (temp.compareTo("Content-Base:") == 0) {
//                    // Get the DESCRIBE lines
//                    String newLine;
//                    for (int i = 0; i < 6; i++) {
//                        newLine = RTSPBufferedReader.readLine();
//                        System.out.println(newLine);
//                    }
//                }
//            }
//        } catch(Exception ex) {
//            System.out.println("Exception caught: "+ex);
//            System.exit(0);
//        }
//      
//        return(reply_code);
//    }

    private void updateStatsLabel() {
        DecimalFormat formatter = new DecimalFormat("###,###.##");
        statLabel1.setText("Total Bytes Received: " + statTotalBytes);
        statLabel2.setText("Packet Lost Rate: " + formatter.format(statFractionLost));
        statLabel3.setText("Data Rate: " + formatter.format(statDataRate) + " bytes/s");
    }

    //------------------------------------
    //Send RTSP Request
    //------------------------------------

    private void sendRequest(String request_type) {
//        try {
//            //Use the RTSPBufferedWriter to write to the RTSP socket
//
//            //write the request line:
//            RTSPBufferedWriter.write(request_type + " " + VideoFileName + " RTSP/1.0" + CRLF);
//
//            //write the CSeq line: 
//            RTSPBufferedWriter.write("CSeq: " + RTSPSeqNb + CRLF);
//
//            //check if request_type is equal to "SETUP" and in this case write the 
//            //Transport: line advertising to the server the port used to receive 
//            //the RTP packets RTP_RCV_PORT
//            if (request_type == "SETUP") {
//                RTSPBufferedWriter.write("Transport: RTP/UDP; client_port= " + RTP_RCV_PORT  + " " + RTCP_RCV_PORT + CRLF);
//            }
//            else if (request_type == "DESCRIBE") {
//                RTSPBufferedWriter.write("Accept: application/sdp" + CRLF);
//            }
//            else {
//                //otherwise, write the Session line from the RTSPid field
//                RTSPBufferedWriter.write("Session: " + RTSPid + CRLF);
//            }
//
//            RTSPBufferedWriter.flush();
//        } catch(Exception ex) {
//            System.out.println("Exception caught: "+ex);
//            System.exit(0);
//        }
    }    
}
