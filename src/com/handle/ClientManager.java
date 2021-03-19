package com.handle;

import java.io.IOException;
import java.net.ServerSocket;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

import com.dao.DataManager;
import com.tools.Sys_Timer;

public class ClientManager {
	
	public boolean waitForClient = true; //�Ƿ�����ȴ��µĿͻ��˽���
	public boolean leave = true; //�Ƿ񱨸�ͻ��˵�������Ϊ
	public boolean lost = true; //�Ƿ񱨸�ͻ��˵ĵ�����Ϊ
	public boolean addRow = true; //�Ƿ�̬ˢ��JTable����
	public boolean follow = true; //�Ƿ���ٿͻ����йܶ����ִ�й���
	public ClientManager parent;
	
	public List<Clienter>clientList = new ArrayList<Clienter>();
	private Thread server;
	public ServerSocket serverSocket;
	
	public boolean canRegedit = true;
	public boolean canLogin = true;
	
	private Clienter client ;
	private Sys_Timer timer;
	
	private JTextArea systemFollow;
	
	private JTextArea userFollow;
	
	private JTextArea qunDisplay;
	
	private JTextArea jta;
	
	private DataManager userData ;
	private Statement st;
	
	public ClientManager(JTextArea systemFollow,JTextArea userFollow,JTextArea qunDisplay,JTextArea jta) {
		
		this.systemFollow = systemFollow;
		this.userFollow = userFollow;
		this.qunDisplay = qunDisplay;
		this.jta = jta;
		timer = new Sys_Timer();
		userData = new DataManager(jta);
		st = userData.getMySQLConnection("jdbc:mysql://localhost:3306/chatdemo?characterEncoding=utf8", "011126");
		
		
		
	}
	
	//���ϲ�ͼ�ι��������ʾϵͳȺ��Ϣ,userID��Ⱥ��Ϣ������ID,message��Ⱥ��Ϣ����
	public void displayQUNMessage(String userID,String message) {
		
		qunDisplay.append(userID+"  "+timer.getSysTime()+"\r\n"+message+"\r\n");
		
		
	}
	
	//�����Ƿ��ṩ����ע�����
	public void setRegedit(boolean regedit) {
		this.canRegedit = regedit;
	}
	
	public void setLogin(boolean login) {
		this.canLogin = login;
	}
	
	public void setFollow(boolean follow) {
		this.follow = follow;
		for(int i=0; i<clientList.size();i++) {
			clientList.get(i).follow = follow;
		}
	}
	
	
	public void SystemFollow(String runInfo) {
		systemFollow.append(timer.getSysTime()+"   "+runInfo+"\r\n");
	}
	
	public void UsermFollow(String userInfo) {
		userFollow.append(timer.getSysTime()+"  "+userInfo+"\r\n");
	}
	
	
	
	public void init(final DefaultTableModel tableModel) {
		parent = this;
		
		server = new Thread() {
			
		public void run() {
			
			try {
				serverSocket = new ServerSocket(6666);
			} catch (IOException e) {
				System.out.println("�������ض˿�6666ʱ��������");
			}
			
		
		
		while(waitForClient) {
			
			try {
				clientList.add(new Clienter(serverSocket.accept(),userData,st,
						tableModel,canRegedit,canLogin,parent,leave,lost,
						addRow,follow));
			} catch (IOException e) {
				// TODO �Զ����ɵ� catch ��
				//e.printStackTrace();
				break;
			}
			
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
			
				
		}
				
		}
				
		};	
		server.start();
		
	}
	
	//�رշ�����
	public void stopServer() {
		
		try {
			serverSocket.close();
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		if(server.isAlive()) {
			server.interrupt();
		}
		
		
	}
	
	public void setLeave(boolean leave) {
		for(int i=0;i<clientList.size();i++) {
			clientList.get(i).isLeave = leave;
			
		}
	}
	
	public void setLost(boolean lost) {
		for(int i=0;i<clientList.size();i++) {
			clientList.get(i).isLost = leave;
			
		}
		
		
	}
	
	public void setAddRow(boolean addRow) {
		for(int i=0; i<clientList.size();i++) {
			clientList.get(i).addrow = addRow;
		}
		
		
		
	}
	
	//�����������пͻ��˹㲥��Ϣ
	public void mulitiCast(String message) {
		for(int i=0; i<clientList.size();i++) {
			clientList.get(i).send_Thread(message);
		}
		
		
	}
	
	
	
	

	//�����������û�ת��Ⱥ��Ϣ
	public void qunMultiCast(String message, Clienter sender) {
		// TODO �Զ����ɵķ������
		for(int i=0;i<clientList.size();i++) {
			if(clientList.get(i)!=sender) {
			clientList.get(i).send_Thread(message);
			}
			}
		
	}
	
	//��������ֹ�ṩ��¼����
	
	public void clearClients(String message) {
		for(int i=0; i<clientList.size();i++) {
			clientList.get(i).send_Thread(message);
			clientList.get(i).clear();
		}
		
	}
	
	//�ڷ�������ǿ��ָ���Ŀͻ���
	public void canelClient(String ID) {
		for(int i=0;i<clientList.size();i++) {
			client = clientList.get(i);
			
			if(client.ID.equals(ID)) {
				client.send_Thread("SYS_CAN");
				client.beCancel();
			}
			
		}
		
		
		
	}
	
	//����Ա��ָ���û�����˽����Ϣ
	public void chatClient(String ID,String Message) {
		for(int i=0;i<clientList.size();i++) {
			client = clientList.get(i);
			
			if(client.ID.equals(ID)) {
				client.send_Thread(Message);
			}
			break;
		}
		
		
	}
	
	//�����������û������ļ�
	public void sendFile(String fileName,long length,String filePath,JTextArea jta,
			JScrollPane jScrollPane3) {
		if(clientList.size()>0) {
			mulitiCast("Fil_Rev"+fileName);
			for(int i=0;i<clientList.size();i++) {
				clientList.get(i).send_File(filePath, jta, jScrollPane3);
			}
			
			
		}else {
			jta.append(timer.getSysTime()+"��ǰ���û����ߣ��ļ����ͱ���ֹ\r\n");
		}
		
		
		
	}
	
	//�򵥸�ָ���û�����ָ���ļ�
	public void sendSingleFile(String ID,String fileName,long length,String filePath,
			JTextArea jta, JScrollPane jScrollPane3) {
		
		mulitiCast("Fil_Rev"+fileName);
		for(int i=0;i<clientList.size();i++) {
			if(clientList.get(i).ID.equals(ID)) {
				
				clientList.get(i).send_File(filePath, jta, jScrollPane3);
				break;
			}
		}
			
		
	}
	
	

}
