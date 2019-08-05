package org.sfs.server.view;

import org.sfs.server.protocol.netty.SFSServer;

import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class SFSServerView {

    private JFrame frame;
    private JTextField textField;
    private JTextArea textArea;
    private SFSServer server;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    SFSServerView window = new SFSServerView();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public SFSServerView() {
        initialize();       //初始化界面
        //initServer();       //初始化netty服务器
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        FrameActionListener actionListener = new FrameActionListener();
        frame = new JFrame();
        frame.setBounds(100, 100, 587, 450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        textArea = new JTextArea();
        textArea.setBounds(14, 13, 541, 334);
        frame.getContentPane().add(textArea);

        textField = new JTextField();
        textField.setBounds(14, 366, 541, 24);
        textField.addKeyListener(new FrameActionListener());
        textField.setActionCommand("command");
        frame.getContentPane().add(textField);
        textField.setColumns(10);
    }

    public void initServer(){
        //开启netty服务，对客户端传递过来的账户数据进行记录
        server = new SFSServer(9999);
    }

    private class FrameActionListener implements KeyListener {
        public void keyTyped(KeyEvent e) {

        }

        public void keyPressed(KeyEvent e) {
            if(e.getKeyCode() == KeyEvent.VK_ENTER) //判断按下的键是否是回车键
            {
                if(textField.getText().equals("start")){
                    initServer();
                    textArea.setText("服务器初始化成功，绑定端口：9999\n");
                }else if(textField.getText().equals("shutdown")){
                    server.shutdown();
                    textArea.setText("服务器等待关闭\n");
                }else if(textField.getText().equals("shutdownNow")){
                    server.shutdownNow();
                    textArea.setText("服务器正在关闭\n");
                }
                textField.setText("");
            }
        }

        public void keyReleased(KeyEvent e) {

        }
    }
}
