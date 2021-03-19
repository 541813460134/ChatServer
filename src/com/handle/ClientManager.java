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
	
	public boolean waitForClient = true; //是否继续等待新的客户端接入
	public boolean leave = true; //是否报告客户端的离线行为
	public boolean lost = true; //是否报告客户端的掉线行为
	public boolean addRow = true; //是否动态刷新JTable数据
	public boolean follow = true; //是否跟踪客户端托管对象的执行过程
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
	
	//向上层图形管理界面显示系统群消息,userID是群消息发送者ID,message是群消息内容
	public void displayQUNMessage(String userID,String message) {
		
		qunDisplay.append(userID+"  "+timer.getSysTime()+"\r\n"+message+"\r\n");
		
		
	}
	
	//设置是否提供自由注册服务
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
				System.out.println("监听本地端口6666时出错！！！");
			}
			
		
		
		while(waitForClient) {
			
			try {
				clientList.add(new Clienter(serverSocket.accept(),userData,st,
						tableModel,canRegedit,canLogin,parent,leave,lost,
						addRow,follow));
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				//e.printStackTrace();
				break;
			}
			
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			
				
		}
				
		}
				
		};	
		server.start();
		
	}
	
	//关闭服务器
	public void stopServer() {
		
		try {
			serverSocket.close();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
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
	
	//服务器向所有客户端广播消息
	public void mulitiCast(String message) {
		for(int i=0; i<clientList.size();i++) {
			clientList.get(i).send_Thread(message);
		}
		
		
	}
	
	
	
	

	//向所有在线用户转发群消息
	public void qunMultiCast(String message, Clienter sender) {
		// TODO 自动生成的方法存根
		for(int i=0;i<clientList.size();i++) {
			if(clientList.get(i)!=sender) {
			clientList.get(i).send_Thread(message);
			}
			}
		
	}
	
	//服务器禁止提供登录服务
	
	public void clearClients(String message) {
		for(int i=0; i<clientList.size();i++) {
			clientList.get(i).send_Thread(message);
			clientList.get(i).clear();
		}
		
	}
	
	//在服务器端强制指定的客户端
	public void canelClient(String ID) {
		for(int i=0;i<clientList.size();i++) {
			client = clientList.get(i);
			
			if(client.ID.equals(ID)) {
				client.send_Thread("SYS_CAN");
				client.beCancel();
			}
			
		}
		
		
		
	}
	
	//管理员向指定用户发送私聊消息
	public void chatClient(String ID,String Message) {
		for(int i=0;i<clientList.size();i++) {
			client = clientList.get(i);
			
			if(client.ID.equals(ID)) {
				client.send_Thread(Message);
			}
			break;
		}
		
		
	}
	
	//向所有在线用户发送文件
	public void sendFile(String fileName,long length,String filePath,JTextArea jta,
			JScrollPane jScrollPane3) {
		if(clientList.size()>0) {
			mulitiCast("Fil_Rev"+fileName);
			for(int i=0;i<clientList.size();i++) {
				clientList.get(i).send_File(filePath, jta, jScrollPane3);
			}
			
			
		}else {
			jta.append(timer.getSysTime()+"当前无用户在线，文件发送被中止\r\n");
		}
		
		
		
	}
	
	//向单个指定用户发送指定文件
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
