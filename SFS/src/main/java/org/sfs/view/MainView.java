package org.sfs.view;

import org.sfs.common.Message;
import org.sfs.common.MessageType;
import org.sfs.netty.SFSClient;
import org.sfs.netty.URL;
import org.sfs.upload.UploadReciver;
import org.sfs.upload.UploadSender;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Random;


public class MainView {

	private JFrame frame;
	private JTextField textField;
	private JLabel lblNewLabel_1;
	private JTextField textField_1;
	private String currentHostId;
	private String targitHostId;
	private SFSClient client;
	private JLabel label;
	private JFileChooser fileChooser;
	private JComboBox comboBox;
	private File file;
	private UploadSender sender = new UploadSender();
	private UploadReciver reciver;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainView window = new MainView();
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
	public MainView() {
		this.client = new SFSClient();
		this.currentHostId = getRandomNumCode(6);		//生成随机ID
		initialize();											//初始化界面
		login();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 706, 238);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("Host ID:");
		lblNewLabel.setBounds(35, 27, 56, 18);
		frame.getContentPane().add(lblNewLabel);

		textField = new JTextField();
		textField.setBounds(94, 25, 205, 21);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		textField.setText(currentHostId);
		textField.setEnabled(false);

		lblNewLabel_1 = new JLabel("targetID:");
		lblNewLabel_1.setBounds(313, 27, 72, 18);
		frame.getContentPane().add(lblNewLabel_1);

		textField_1 = new JTextField();
		textField_1.setBounds(399, 24, 220, 24);
		frame.getContentPane().add(textField_1);
		textField_1.setColumns(10);

		JButton btnNewButton = new JButton("选择文件");
		btnNewButton.setBounds(298, 97, 113, 27);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int i = fileChooser.showOpenDialog(frame.getContentPane());// 显示文件选择对话框
				if (i == JFileChooser.APPROVE_OPTION) {		//判断用户选择的是打开按钮
					file = fileChooser.getSelectedFile();// 获得选中的文件对象
					label.setText(file.getAbsolutePath());
				}
			}
		});
		frame.getContentPane().add(btnNewButton);

		JButton btnNewButton_1 = new JButton("发送文件");
		btnNewButton_1.setBounds(508, 97, 113, 27);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(textField_1.getText() == null || textField_1.getText().trim().equals("")){
					JOptionPane.showMessageDialog(null,"请输入接收方ID");
					return;
				}
				targitHostId = textField_1.getText();
				Message message = new Message(MessageType.ENCRYPTION_REQUEST_1,currentHostId+","+targitHostId);
				Message result = (Message)client.send(new URL("127.0.0.1",9999),message);
				if(result.getMessageType().equals(MessageType.ENCRYPTION_RESPONSE)){
					//接收服务器传递过来的公钥和对方的IP
					String key = result.getData().toString().split(":::")[0];
					String ip = result.getData().toString().split(":::")[1];
					//与另外一端建立TCP连接，单独启动一个线程
					sender.send(file,key.getBytes(),comboBox.getSelectedItem().toString(),ip);
				}
			}
		});
		frame.getContentPane().add(btnNewButton_1);

		label = new JLabel("");
		label.setBounds(78, 149, 530, 18);
		frame.getContentPane().add(label);

		comboBox = new JComboBox();
		comboBox.setBounds(78, 98, 114, 24);
		comboBox.addItem("DES");
		comboBox.addItem("AES");
		comboBox.addItem("3DES");
		frame.getContentPane().add(comboBox);

		fileChooser = new JFileChooser();
		reciver = new UploadReciver(this.client,currentHostId);
		Thread t2 = new Thread(reciver);
		t2.start();
	}

	/**
	 * 连接服务器
	 */
	public void login(){
		Message message = new Message(MessageType.LOGIN_REQUEST,currentHostId);
		Message responseMsg = (Message) this.client.send(new URL("127.0.0.1",9999),message);
		if(responseMsg.getMessageType().equals(MessageType.LOGIN_RESPONSE) && responseMsg.getData().equals("success")){
			label.setText("客户端初始化完毕，连接服务器成功！！！");
		}
	}

	/**
	 * 返回一个6位的随机数
	 * @param number
	 * @return
	 */
	public static String getRandomNumCode(int number){
		String codeNum = "";
		int [] numbers = {0,1,2,3,4,5,6,7,8,9};
		Random random = new Random();
		for (int i = 0; i < number; i++) {
			int next = random.nextInt(10000);//目的是产生足够随机的数，避免产生的数字重复率高的问题
			codeNum+=numbers[next%10];
		}
		return codeNum;
	}
}
