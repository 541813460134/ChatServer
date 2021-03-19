package com.handle;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.common.getFilePath;
import com.tools.logSave;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JSlider;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class Manager extends JFrame implements ActionListener{

	private JPanel contentPane;
	private JTable table;
	private JTextField textField;
	ClientManager cm;
	JButton savelog;
	JTextArea systemfollow;
	private JMenuItem file_send;
	private JMenuItem mess_send;
	private fileSend fs;
	private JScrollPane scrollPane_5;
	private JTextArea filearea;
	private getFilePath gf = new getFilePath();
	private JTextArea textArea;
	private JMenuItem logout;
	private JLabel lblNewLabel_7;
	static Point origin = new Point();;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Manager frame = new Manager();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Manager() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1001, 597);
		contentPane = new JPanel();
		contentPane.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.CYAN, Color.CYAN, Color.CYAN, Color.CYAN));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e1) {
			// TODO �Զ����ɵ� catch ��
			e1.printStackTrace();
		} 
		
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 984, 78);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel_5 = new JLabel("��̨������");
		lblNewLabel_5.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5.setFont(new Font("����", Font.PLAIN, 35));
		lblNewLabel_5.setBounds(153, 13, 623, 52);
		panel.add(lblNewLabel_5);
		
		JButton exit_bu = new JButton("X");
		exit_bu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int result = JOptionPane.showConfirmDialog(null, "ȷ���˳�ϵͳ?", "ȷ��", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
                if(result == JOptionPane.OK_OPTION){
                    System.exit(0);
                }
			}
		});
		exit_bu.setFont(new Font("����", Font.PLAIN, 15));
		exit_bu.setBounds(943, 0, 41, 27);
		panel.add(exit_bu);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setFont(new Font("����", Font.PLAIN, 18));
		tabbedPane.setBounds(0, 78, 984, 472);
		contentPane.add(tabbedPane);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("�û��������", null, panel_1, null);
		panel_1.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 979, 418);
		panel_1.add(scrollPane);
		
		
		DefaultTableModel dtm = new DefaultTableModel();
		 String[] columnNames = {"ID","IP","name"};
		 dtm.setColumnIdentifiers(columnNames);
		
		JPopupMenu popupMenu = new JPopupMenu();
		addPopup(scrollPane, popupMenu);
		
		 file_send = new JMenuItem("�����ļ�");
		
		popupMenu.add(file_send);
		
		 mess_send = new JMenuItem("������Ϣ");
		popupMenu.add(mess_send);
		
		 logout = new JMenuItem("ע�����û�");
		popupMenu.add(logout);
		
		file_send.addActionListener(this);
		mess_send.addActionListener(this);
		logout.addActionListener(this);
		table = new JTable(dtm);
		table.setFont(new Font("����", Font.PLAIN, 15));
		scrollPane.setViewportView(table);
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("�������������", null, panel_2, null);
		panel_2.setLayout(null);
		
		JPanel panel_10 = new JPanel();
		panel_10.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.CYAN, Color.CYAN, Color.CYAN, Color.CYAN));
		panel_10.setBounds(0, 0, 146, 225);
		panel_2.add(panel_10);
		panel_10.setLayout(null);
		
		JButton closeServer = new JButton("�رշ���");
		closeServer.setFont(new Font("����", Font.PLAIN, 15));
		closeServer.setBounds(14, 185, 113, 27);
		panel_10.add(closeServer);
		closeServer.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				cm.stopServer();
			}
		});
		
		JLabel lblNewLabel = new JLabel("����");
		lblNewLabel.setFont(new Font("����", Font.PLAIN, 15));
		lblNewLabel.setBounds(0, 0, 72, 18);
		panel_10.add(lblNewLabel);
		
		JLabel lblNewLabel_10 = new JLabel(new ImageIcon("image/jindu.gif"));
		lblNewLabel_10.setBounds(0, 26, 140, 155);
		panel_10.add(lblNewLabel_10);
		
		JPanel panel_11 = new JPanel();
		panel_11.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.CYAN, Color.CYAN, Color.CYAN, Color.CYAN));
		panel_11.setBounds(145, 0, 146, 225);
		panel_2.add(panel_11);
		panel_11.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("ע��");
		lblNewLabel_1.setFont(new Font("����", Font.PLAIN, 15));
		lblNewLabel_1.setBounds(0, 0, 72, 18);
		panel_11.add(lblNewLabel_1);
		
		JRadioButton openRegister = new JRadioButton("����ע��");
		
		openRegister.setFont(new Font("����", Font.PLAIN, 15));
		openRegister.setBounds(10, 59, 108, 27);
		panel_11.add(openRegister);
		
		openRegister.setSelected(true);
		
		JRadioButton closeRegister = new JRadioButton("��ͣע��");
		
		closeRegister.setFont(new Font("����", Font.PLAIN, 15));
		closeRegister.setBounds(10, 128, 100, 27);
		panel_11.add(closeRegister);
		
		ButtonGroup group1=new ButtonGroup();
		
		group1.add(openRegister);
		group1.add(closeRegister);
		
		JPanel panel_12 = new JPanel();
		panel_12.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.CYAN, Color.CYAN, Color.CYAN, Color.CYAN));
		panel_12.setBounds(289, 0, 690, 225);
		panel_2.add(panel_12);
		panel_12.setLayout(null);
		
		JRadioButton freelogin = new JRadioButton("���ɵ�¼");
		
		freelogin.setFont(new Font("����", Font.PLAIN, 15));
		freelogin.setBounds(26, 57, 157, 27);
		panel_12.add(freelogin);
		
		freelogin.setSelected(true);
		
		JRadioButton closelogin = new JRadioButton("��ֹ��¼");
		
		closelogin.setFont(new Font("����", Font.PLAIN, 15));
		closelogin.setBounds(26, 131, 157, 27);
		panel_12.add(closelogin);
		
		ButtonGroup group2=new ButtonGroup();
		
		group2.add(freelogin);
		group2.add(closelogin);
		
		JLabel lblNewLabel_2 = new JLabel("��¼");
		lblNewLabel_2.setFont(new Font("����", Font.PLAIN, 15));
		lblNewLabel_2.setBounds(0, 0, 72, 18);
		panel_12.add(lblNewLabel_2);
		
		JPanel panel_13 = new JPanel();
		panel_13.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.RED, Color.RED, Color.RED, Color.RED));
		panel_13.setBounds(0, 226, 979, 192);
		panel_2.add(panel_13);
		panel_13.setLayout(null);
		
		JLabel lblNewLabel_3 = new JLabel("�㲥");
		lblNewLabel_3.setFont(new Font("����", Font.PLAIN, 15));
		lblNewLabel_3.setBounds(0, 0, 72, 18);
		panel_13.add(lblNewLabel_3);
		
		JButton sendbroadcast = new JButton("���͹㲥");
		sendbroadcast.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String s = textArea.getText();
				cm.mulitiCast(s);
			}
		});
		sendbroadcast.setFont(new Font("����", Font.PLAIN, 15));
		sendbroadcast.setBounds(852, 152, 113, 27);
		panel_13.add(sendbroadcast);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(50, 31, 881, 110);
		panel_13.add(scrollPane_1);
		
		 textArea = new JTextArea();
		scrollPane_1.setViewportView(textArea);
		
		JPanel panel_3 = new JPanel();
		tabbedPane.addTab("ϵͳ�������", null, panel_3, null);
		panel_3.setLayout(null);
		
		JPanel panel_17 = new JPanel();
		panel_17.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.ORANGE, Color.ORANGE, Color.ORANGE, Color.ORANGE));
		panel_17.setBounds(0, 0, 979, 129);
		panel_3.add(panel_17);
		panel_17.setLayout(null);
		
		this.setUndecorated(true);
		JSlider slider = new JSlider();
		slider.setMinimum(20);
		slider.setValue(100);
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				int val = slider.getValue();
				float a = (float) (val*0.01);
				setOpacity(a);
				lblNewLabel_7.setText("��ǰ:  "+val+"%");
			}
		});
		slider.setBounds(51, 68, 200, 26);
		panel_17.add(slider);
		
		JLabel lblNewLabel_6 = new JLabel("����͸����");
		lblNewLabel_6.setFont(new Font("����", Font.PLAIN, 15));
		lblNewLabel_6.setBounds(51, 37, 92, 18);
		panel_17.add(lblNewLabel_6);
		
		 lblNewLabel_7 = new JLabel("��ǰ: 100"+"%");
		lblNewLabel_7.setFont(new Font("����", Font.PLAIN, 15));
		lblNewLabel_7.setBounds(202, 37, 99, 18);
		panel_17.add(lblNewLabel_7);
		
		JLabel lblNewLabel_8 = new JLabel("��������");
		lblNewLabel_8.setFont(new Font("����", Font.PLAIN, 15));
		lblNewLabel_8.setBounds(0, 0, 72, 18);
		panel_17.add(lblNewLabel_8);
		
		JPanel panel_18 = new JPanel();
		panel_18.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.ORANGE, Color.ORANGE, Color.ORANGE, Color.ORANGE));
		panel_18.setBounds(0, 179, 979, 235);
		panel_3.add(panel_18);
		panel_18.setLayout(null);
		
		JCheckBox chckbxNewCheckBox_2 = new JCheckBox("�Զ�ˢ�������û��б�");
		chckbxNewCheckBox_2.setSelected(true);
		chckbxNewCheckBox_2.setFont(new Font("����", Font.PLAIN, 15));
		chckbxNewCheckBox_2.setBounds(34, 29, 229, 27);
		panel_18.add(chckbxNewCheckBox_2);
		
		JCheckBox chckbxNewCheckBox_3 = new JCheckBox("�Զ�����ϵͳ������־");
		chckbxNewCheckBox_3.setSelected(true);
		chckbxNewCheckBox_3.setFont(new Font("����", Font.PLAIN, 15));
		chckbxNewCheckBox_3.setBounds(34, 78, 196, 27);
		panel_18.add(chckbxNewCheckBox_3);
		
		JCheckBox chckbxNewCheckBox_4 = new JCheckBox("�Զ�ˢ�������û��б�");
		chckbxNewCheckBox_4.setSelected(true);
		chckbxNewCheckBox_4.setFont(new Font("����", Font.PLAIN, 15));
		chckbxNewCheckBox_4.setBounds(34, 126, 196, 27);
		panel_18.add(chckbxNewCheckBox_4);
		
		JCheckBox chckbxNewCheckBox_5 = new JCheckBox("�Զ�����ϵͳ������Ϣ");
		chckbxNewCheckBox_5.setSelected(true);
		chckbxNewCheckBox_5.setFont(new Font("����", Font.PLAIN, 15));
		chckbxNewCheckBox_5.setBounds(34, 178, 184, 27);
		panel_18.add(chckbxNewCheckBox_5);
		
		JLabel lblNewLabel_9 = new JLabel("�߼�����");
		lblNewLabel_9.setFont(new Font("����", Font.PLAIN, 15));
		lblNewLabel_9.setBounds(0, 0, 72, 18);
		panel_18.add(lblNewLabel_9);
		
		JPanel panel_4 = new JPanel();
		tabbedPane.addTab("���ݿ�������", null, panel_4, null);
		panel_4.setLayout(null);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(0, 0, 979, 418);
		panel_4.add(scrollPane_2);
		
		JTextArea textArea_1 = new JTextArea();
		textArea_1.setFont(new Font("����", Font.PLAIN, 15));
		scrollPane_2.setViewportView(textArea_1);
		
		JPanel panel_5 = new JPanel();
		tabbedPane.addTab("�ͻ��˼��", null, panel_5, null);
		panel_5.setLayout(null);
		
		JPanel panel_14 = new JPanel();
		panel_14.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.CYAN, Color.CYAN, Color.CYAN, Color.CYAN));
		panel_14.setBounds(0, 0, 979, 68);
		panel_5.add(panel_14);
		panel_14.setLayout(null);
		
		JLabel lblNewLabel_4 = new JLabel("���߹���");
		lblNewLabel_4.setFont(new Font("����", Font.PLAIN, 15));
		lblNewLabel_4.setBounds(0, 0, 72, 18);
		panel_14.add(lblNewLabel_4);
		
		JCheckBox chckbxNewCheckBox = new JCheckBox("�û�����ʱ�����Ի�����ʾ");
		chckbxNewCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				
				
			}
		});
		
		chckbxNewCheckBox.setFont(new Font("����", Font.PLAIN, 15));
		chckbxNewCheckBox.setBounds(133, 21, 233, 27);
		panel_14.add(chckbxNewCheckBox);
		
		chckbxNewCheckBox.setSelected(true);
		
		JCheckBox chckbxNewCheckBox_1 = new JCheckBox("���û�����ʱ�����Ի�����ʾ");
		
		chckbxNewCheckBox_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				
			}
		});
		chckbxNewCheckBox_1.setSelected(true);
		chckbxNewCheckBox_1.setFont(new Font("����", Font.PLAIN, 15));
		chckbxNewCheckBox_1.setBounds(419, 21, 233, 27);
		panel_14.add(chckbxNewCheckBox_1);
		
		JPanel panel_6 = new JPanel();
		tabbedPane.addTab("����������", null, panel_6, null);
		panel_6.setLayout(null);
		
		JScrollPane scrollPane_6 = new JScrollPane();
		scrollPane_6.setBounds(0, 0, 979, 414);
		panel_6.add(scrollPane_6);
		
		JTextArea userfollow = new JTextArea();
		userfollow.setFont(new Font("����", Font.PLAIN, 15));
		scrollPane_6.setViewportView(userfollow);
		
		JPanel panel_7 = new JPanel();
		tabbedPane.addTab("ϵͳ������־", null, panel_7, null);
		panel_7.setLayout(null);
		
		JPanel panel_15 = new JPanel();
		panel_15.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.LIGHT_GRAY, Color.LIGHT_GRAY, Color.LIGHT_GRAY, Color.LIGHT_GRAY));
		panel_15.setBounds(831, 0, 148, 418);
		panel_7.add(panel_15);
		panel_15.setLayout(null);
		
		JButton emptylog = new JButton("���");
		emptylog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				systemfollow.setText("");
				
			}
		});
		emptylog.setFont(new Font("����", Font.PLAIN, 15));
		emptylog.setBounds(14, 317, 113, 27);
		panel_15.add(emptylog);
		
		 savelog = new JButton("����");
		savelog.setFont(new Font("����", Font.PLAIN, 15));
		savelog.addActionListener(this);
		savelog.setBounds(14, 378, 113, 27);
		panel_15.add(savelog);
		
		JScrollPane scrollPane_3 = new JScrollPane();
		scrollPane_3.setBounds(0, 0, 832, 418);
		panel_7.add(scrollPane_3);
		
		 systemfollow = new JTextArea();
		 systemfollow.setFont(new Font("����", Font.PLAIN, 15));
		scrollPane_3.setViewportView(systemfollow);
		
		JPanel panel_8 = new JPanel();
		tabbedPane.addTab("Ⱥ�Ĺ������", null, panel_8, null);
		panel_8.setLayout(null);
		
		JScrollPane scrollPane_4 = new JScrollPane();
		scrollPane_4.setBounds(0, 0, 979, 368);
		panel_8.add(scrollPane_4);
		
		JTextArea qundisplay = new JTextArea();
		qundisplay.setFont(new Font("����", Font.PLAIN, 15));
		scrollPane_4.setViewportView(qundisplay);
		
		JRadioButton freequnchat = new JRadioButton("����Ⱥ��");
		freequnchat.setFont(new Font("����", Font.PLAIN, 15));
		freequnchat.setBounds(24, 377, 157, 27);
		panel_8.add(freequnchat);
		
		freequnchat.setSelected(true);
		
		JRadioButton rdbtnNewRadioButton_6 = new JRadioButton("���Ƽ���");
		rdbtnNewRadioButton_6.setFont(new Font("����", Font.PLAIN, 15));
		rdbtnNewRadioButton_6.setBounds(299, 377, 157, 27);
		panel_8.add(rdbtnNewRadioButton_6);
		
		JRadioButton closequnchat = new JRadioButton("�ر�Ⱥ��");
		closequnchat.setFont(new Font("����", Font.PLAIN, 15));
		closequnchat.setBounds(616, 377, 157, 27);
		panel_8.add(closequnchat);
		
		ButtonGroup group3=new ButtonGroup();
		
		group3.add(freequnchat);
		group3.add(rdbtnNewRadioButton_6);
		group3.add(closequnchat);
		
		JPanel panel_9 = new JPanel();
		tabbedPane.addTab("�ļ��·�����", null, panel_9, null);
		panel_9.setLayout(null);
		
		JPanel panel_16 = new JPanel();
		panel_16.setBounds(0, 0, 979, 374);
		panel_9.add(panel_16);
		panel_16.setLayout(null);
		
		 scrollPane_5 = new JScrollPane();
		scrollPane_5.setBounds(0, 0, 979, 374);
		panel_16.add(scrollPane_5);
		
		 filearea = new JTextArea();
		 filearea.setFont(new Font("����", Font.PLAIN, 15));
		scrollPane_5.setViewportView(filearea);
		
		textField = new JTextField();
		textField.setBounds(0, 381, 623, 24);
		panel_9.add(textField);
		textField.setColumns(10);
		
		JButton btnNewButton_4 = new JButton("ѡȡ�ļ�");
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String filepath = gf.getPath();
				textField.setText(filepath);
				
			}
		});
		btnNewButton_4.setFont(new Font("����", Font.PLAIN, 15));
		btnNewButton_4.setBounds(637, 380, 113, 27);
		panel_9.add(btnNewButton_4);
		
		
		
		JButton btnNewButton_5 = new JButton("��ʼ����");
		btnNewButton_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String path = textField.getText();
				String name = gf.filename;
				if(textField.getText()!="") {
				cm.sendFile(name, 1000,textField.getText(), filearea, scrollPane_5);
				}
			}
		});
		btnNewButton_5.setFont(new Font("����", Font.PLAIN, 15));
		btnNewButton_5.setBounds(809, 380, 113, 27);
		panel_9.add(btnNewButton_5);
		
		
		this.addMouseListener(new MouseAdapter() {
			// ���£�mousePressed ���ǵ����������걻����û��̧��
			public void mousePressed(MouseEvent e) {
			// ����갴�µ�ʱ���ô��ڵ�ǰ��λ��
			origin.x = e.getX();
			origin.y = e.getY();
			}
			});
		this.addMouseMotionListener(new MouseMotionAdapter() {
			// �϶���mouseDragged ָ�Ĳ�������ڴ������ƶ�������������϶���
			public void mouseDragged(MouseEvent e) {
			// ������϶�ʱ��ȡ���ڵ�ǰλ��
			Point p = getLocation();
			// ���ô��ڵ�λ��
			// ���ڵ�ǰ��λ�� + ��굱ǰ�ڴ��ڵ�λ�� - ��갴�µ�ʱ���ڴ��ڵ�λ��
			setLocation(p.x + e.getX() - origin.x, p.y + e.getY()- origin.y);
			}
			});
		
		
		
		
		 cm = new ClientManager(systemfollow, userfollow, qundisplay,textArea_1);
		cm.init(dtm);
		
		chckbxNewCheckBox_1.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				
				 JCheckBox checkBox = (JCheckBox) e.getSource();
				 if(checkBox.isSelected()) {
					 cm.setLeave(true);
				 }else {
					 cm.setLeave(false);
					 
				 }
			}
		});
		
		closelogin.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JRadioButton closelogin = (JRadioButton) e.getSource();
				if(closelogin.isSelected()) {
					cm.setLogin(false);
				}else {
					 cm.setLogin(true);
					 
				 }
				
				
			}
		});
		
//		freelogin.addChangeListener(new ChangeListener() {
//			public void stateChanged(ChangeEvent e) {
//				JRadioButton freelogin = (JRadioButton) e.getSource();
//				if(freelogin.isSelected()) {
//					cm.setLogin(true);
//				}else {
//					 cm.setLogin(false);
//					 
//				 }
//			}
//		});
		
		closeRegister.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JRadioButton closeRegister = (JRadioButton) e.getSource();
				if(closeRegister.isSelected()) {
					cm.setRegedit(false);
				}else {
					cm.setRegedit(true);
					 
				 }
			}
			
		});
		
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		// TODO �Զ����ɵķ������
		if(e.getSource()==savelog) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fileChooser.setApproveButtonText("ȷ��");
			fileChooser.setDialogTitle("���Ϊ");
			
			int result = fileChooser.showSaveDialog(this);
			if(result==JFileChooser.CANCEL_OPTION)
			{
				return;
			}
			File saveFilename = fileChooser.getSelectedFile();
			
			if(saveFilename == null || saveFilename.getName().equals(""))
			{
				JOptionPane.showConfirmDialog(this, "���Ϸ����ļ���", "���Ϸ����ļ���", JOptionPane.ERROR_MESSAGE);
			}
			else
			{	
				logSave ls = new logSave();
				ls.Sava(systemfollow.getText(), saveFilename);
			}
			
		} else if(e.getSource()==file_send) {
			int index = table.getSelectedRow();
			String id = (String) table.getValueAt(index, 0);
			String ip = (String) table.getValueAt(index, 1);
			//System.out.println(id+"     "+ip);
			
			String file = gf.getPath();
			
			cm.sendSingleFile(id, gf.filename, 1000, file, filearea, scrollPane_5);
			
		
		} else if(e.getSource()==mess_send) {
			int index = table.getSelectedRow();
			String id = (String) table.getValueAt(index, 0);
		
			
			new Sendmes(cm,id).setVisible(true);
			
		} else if(e.getSource()==logout) {
			
			int index = table.getSelectedRow();
			String id = (String) table.getValueAt(index, 0);
			
			cm.canelClient(id);
			
		}
		
	}
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}
