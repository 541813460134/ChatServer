package com.handle;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Statement;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

import com.dao.DataManager;
import com.tools.createID;

public class Clienter {

	public String ID;// 托管则的客户端用户ID
	public boolean isLost = true; // 是否报告客户端用户的掉线行为
	public boolean isLeave = true; // 是否报告客户端用户的离线行为
	public boolean follow = true; // 是否跟踪自身的执行过程数据
	private Socket client; // 托管则客户端连接
	private Thread rev; // 消息接收线程

	private boolean do_check = true; // 是否继续接收客户端消息
	private String name; // 托管则客户端用户的昵称
	private String password; // 托管者客户端用户的密码
	private String userIP; // 托管者客户端用户的IP;
	private DataInputStream in; // 输出流
	// 数据库操作者类对象,并未实例化，只是接收上层调用者传来的值
	private DataManager userData;
	private createID IDcreater;
	private Statement st;
	private DataOutputStream out; // 输出流
	private DefaultTableModel tableModel;
	private boolean canRegedit = true;
	private boolean canLogin = true;
	private ClientManager parent;
	private long timeStart = 0;
	private long timeLast = 0;

	private Thread clearClient;
	private int lostCount = 0;
	private boolean isOnLine = true; // 托管的客户端用户是否还在线

	public boolean addrow = true;

	public Clienter(Socket client, DataManager userData, Statement st, DefaultTableModel tableModel, boolean canRegedit,
			boolean canLogin, ClientManager parent, boolean isLeave, boolean isLost, boolean addRow, boolean follow) {
		// TODO 自动生成的构造函数存根
		this.client = client;
		this.userData = userData;
		this.st = st;
		this.tableModel = tableModel;
		this.canRegedit = canRegedit;
		this.canLogin = canLogin;
		this.parent = parent;
		this.isLeave = isLeave;
		this.isLost = isLost;
		this.addrow = addRow;
		this.follow = follow;
		this.userData = userData;
		this.st = st;

		if (follow) {
			parent.SystemFollow("系统运行正常:客户端托管者初始化完成");
		}

		if (follow) {
			parent.SystemFollow("系统运行正常:客户端托管者为客户创建数据库连接完成");
		}

		rev_Thread(this);

		if (follow) {
			parent.SystemFollow("系统运行正常:客户端托管者为客户启动接收线程完成");
		}

	}

	// 托管者类的核心代码区，实现消息的接收和解析
	private void rev_Thread(final Clienter myself) {
		// TODO 自动生成的方法存根
		rev = new Thread() {

			public void run() {

				try {
					in = new DataInputStream(client.getInputStream());
					if (follow) {
						parent.SystemFollow("系统运行正常:客户端托管者为客户封装传输流完成");
					}
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
					do_check = false;
				}

				while (do_check) {

					try {
						String str = null;
					
						
						str = in.readUTF();
					
						
						
						
						
						if (follow) {
							parent.SystemFollow("系统运行正常:客户端托管者收到来自客户的消息");
						}

						// 判断客户端是否离线,"BYE_BYE"是离线信号
						if (!str.equals("BYE_BYE")) {
							String tag = null;
							if (follow) {
								parent.SystemFollow("系统运行正常:客户端托管者正在解析消息");
							}

							StringTokenizer token = new StringTokenizer(str, ":");

							tag = token.nextToken();

							if (follow) {
								parent.SystemFollow("系统运行正常:客户端托管者解析客户消息完成,正在 响应客户端请求");
							}

							if (tag.equals("R")) // 注册服务
							{
								if (canRegedit) {
									// 解析出注册用户的昵称
									name = token.nextToken();

									password = token.nextToken();

									IDcreater = new createID(st);

									ID = IDcreater.checkID();

									parent.UsermFollow("系统检测到有新用户注册,注册昵称:ID:" + ID + "昵称:" + name + "密码:" + password);
									if (userData.upDate("INSERT INTO chat (ID,password,name)" + "VALUES('" + ID + "','"
											+ password + "','" + name + "')")) {
										send_Thread(ID);
										parent.UsermFollow("该用户注册成功,注册昵称:ID:" + ID + "昵称:" + name + "密码:" + password);

									} else {
										send_Thread("" + 0);
										parent.UsermFollow("服务器原因导致该用户注册失败");

									}
									// 注册成功，托管者响应完成，清理资源
									del();
									if (follow) {
										parent.SystemFollow("系统运行正常:客户端托管者响应" + "客户端注册时间完成，正在关闭客户端" + ID + "占用的资源");
									}

									break;
								} else {
									send_Thread("" + -1); // 暂停注册
									parent.UsermFollow("服务器暂停注册服务,该客户注册失败");
									del();

									break;
								}

							} else if (tag.equals("L")) // 登录服务
							{
								if (canLogin) {
									ID = token.nextToken(); // 登录者ID
									password = token.nextToken();
									if (token.hasMoreElements()) {
										userIP = token.nextToken();
									}

									parent.UsermFollow("系统检测到有用户登录,登录ID" + ID + "密码:" + password + "IP:" + userIP);

									int result = userData.checkOnLine("select * from chat where ID='" + ID + "'", ID,
											password);

									// 判断该用户是否已登录
									if (follow) {
										parent.SystemFollow("系统运行正常:客户端托管则正在为响应客户" + ID + "的登录事件而查询数据库");

									}

									if (result == 1) { // 该用户未登录
										// 查询该用户的昵称
										name = userData.getDate("select * from chat where ID ='" + ID + "'", "name");
										parent.UsermFollow("用户" + ID + "登录成功，返回用户昵称:" + name);

										if (userData.upDate("INSERT into online(ID,IP,name)VALUES('" + ID + "','"
												+ userIP + "','" + name + "')")) {

											if (follow) {
												parent.SystemFollow("系统运行正常:客户端托管将客户" + ID + "的登录信息写入数据库");
											}

											String[] userInfo = new String[] { ID, userIP, name };
											if (addrow) {
												tableModel.addRow(userInfo);
											}
											if (follow) {
												parent.SystemFollow("系统运行正常:客户端托管将客户" + ID + "的信息填充在线用户列表");

											}

											// 回发登录用户的昵称
											send_Thread(name);
											timeStart = System.currentTimeMillis();

											// 启动内置的掉线检查管理器
											clearClient();
											if (follow) {
												parent.SystemFollow("系统运行正常:客户端托管将客户" + ID + "启动掉线检查管理器");

											}

										} else {
											// 此用户已登录
											send_Thread(-2 + "");
											parent.UsermFollow("用户:" + ID + " 登录失败：此用户已登录");
											del();
											break;
										}

									} else {
										send_Thread(result + "");
										if (result == 0) { // 0,密码错误
											parent.UsermFollow("用户:" + ID + " 登录失败：密码错误");
										} else if (result == -1) {
											parent.UsermFollow("用户:" + ID + " 登录失败：此账号未注册");

										}
									}

								} else {
									send_Thread(-3 + ""); // 暂停登录服务
									parent.UsermFollow("用户:" + ID + " 登录失败：系统暂停登录服务");
									del();
									if (follow) {
										parent.SystemFollow("系统运行正常:客户" + ID + "登录失败，客户端管理者正在关闭其占用的资源");
									}

									break;

								}
							} else if (tag.endsWith("F_List")) { // 客户端请求好友列表
								send_Thread(userData.getFriends(ID));
								parent.UsermFollow("服务器向用户:" + ID + " 发送好友列表完成");

							} else if (str.startsWith("ADD_Fr:")) { // 客户端添加好友
								send_Thread("" + userData.upDate("insert into friends(ID,friendID,friendName)"
										+ "values('" + str.substring(7, 16) + "','" + str.substring(16, 25) + "','"
										+ str.substring(25, str.length()) + "')"));
								parent.UsermFollow("服务器响应用户:" + ID + " 的添加好友请求完成");
							} else if (tag.endsWith("Se_Fri")) {
								// 客户端查找好友
								String result = userData.getSearch(
										"select * from chat where ID='" + str.substring(7,str.length()) + "'");
								//System.out.println(str.substring(7,str.length()));

								parent.UsermFollow("服务器响应用户:" + ID + " 的查找好友请求完成");

								if (result.equals("NONESE")) {

									send_Thread("NONESE");
								//	System.out.println(result);

								} else {
									send_Thread("Se_Fri" + result);
								//	System.out.println(result);
								}
								parent.UsermFollow("服务器向用户:" + ID + " 发送好友查找结果完成");

							} else if (tag.endsWith("Fri_ip")) {
								// 客户端请求好友的IP地址
								send_Thread("Fri_ip" + userData.getFri_ip(str.substring(7, str.length())));

								parent.UsermFollow("服务器响应用户:" + ID + " 的请求好友IP完成");

							} else if (str.startsWith("SYS_IN")) {
								// 客户端的在线认证消息
								timeStart = System.currentTimeMillis();

								if (follow) {
									parent.SystemFollow("系统运行正常:掉线检查管理继续收到客户:    " + ID + "   " + "的在线消息，该客户仍然在线");
								}

							} else if (str.startsWith("DEL_FR")) // 客户端删除好友
							{
								parent.UsermFollow("服务器收到客户" + ID + "的删除好友请求，正在查找数据库以完成响应");

								String friendID = str.substring(7, str.length());

								if (userData.upDate(
										"delete from friends where ID ='" + ID + "'and friendID='" + friendID + "'")) {

									send_Thread("DEL_FR_ACCESS");
									send_Thread(userData.getFriends(ID));

									parent.UsermFollow("服务器响应 用户 " + ID + "的删除好友请求完成，删除成功");

								} else {
									send_Thread("DEL_FR_FAILE");
									parent.UsermFollow("服务器响应 用户 " + ID + "的删除好友请求完成，删除失败");

								}

							} else if (str.startsWith("SYSQUN")) { // 客户端发送群消息

								parent.qunMultiCast("QUN_ME:" + str.substring(7, str.length()) + ID, myself);
								parent.displayQUNMessage(ID, str.substring(7, str.length()));

								parent.UsermFollow("服务器收到 用户 " + ID + "发送的群消息,消息解析完成，正在向其它群用户转发");

							} else { // 过滤器,过滤非法的消息

								if (follow) {
									parent.SystemFollow("系统运行异常:客户端管理者意外收到来自客户  " + ID + "  的消息");

								}

								JOptionPane.showMessageDialog(null, "未过滤的消息，请过滤");

							}
						} else { // 用户正常离线,清理资源


							userData.upDate("delete from online where ID='"+ID+"'");
							parent.UsermFollow("服务器清理 用户 " + ID + " 占用的系统资源完成");
							isOnLine = false;

							if (isLeave) {
								JOptionPane.showMessageDialog(null,
										"检测到有用户离线！\r\n用户ID：" + ID + "\r\n用户昵称:"+name+"\r\n" );
							}
							
							
							if(addrow) {
								tableModel.removeRow(search(ID));
							}
							
							if(follow) {
								parent.SystemFollow("系统运行正常：客户端管理者清理下线客户"+ID+" 占用的系统资源，并将该客户从在线列表中删除");
							}

						}

					} catch (IOException e) {
						// TODO 自动生成的 catch 块
						
						break;
					}

					
				}

			}

		};
		rev.start();

	}

	private int search(String ID) {

		int i;
		System.out.println(tableModel.getRowCount());
		for (i = 0; i < tableModel.getRowCount(); i++) {
			if (tableModel.getValueAt(i, 1).equals(ID)) {
				break;
			}

		}
		if ((i - 1) > tableModel.getRowCount()) {

			i = -1;
		}

		return (i - 1);

	}

	public void clear() {

		userData.upDate("delete from online where ID='" + ID + "'");

		if (addrow) {
			tableModel.removeRow(search(ID));
		}

		del();

	}

	// 内置的掉线检查管理器
	public void clearClient() {
		// TODO 自动生成的方法存根
		clearClient = new Thread() {

			public void run() {
				while (isOnLine) {
					timeLast = System.currentTimeMillis();

					if ((timeLast - timeStart) > 25000) {
						if (lostCount > 2) {
							userData.upDate("delete from online where ID='" + ID + "'");

							if (follow) {
								parent.SystemFollow("系统运行正常:客户端管理者发现客户  " + ID + "  无故掉线！\r\n");
							}

							if (addrow) {
								tableModel.removeRow(search(ID));
							}

							lostCount++;
						} else {
							lostCount = 0;
						}

						try {
							Thread.sleep(10000);
						} catch (InterruptedException e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						}

					}

				}

			}

		};

		clearClient.start();

	}

	private void del() {
		// TODO 自动生成的方法存根
		isOnLine = false;

		if (rev.isAlive()) {
			rev.interrupt();
		}

		try {
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.close();
			}
			if (client != null) {

				client.close();
			}
			if (clearClient != null) {
				if (clearClient.isAlive()) {
					clearClient.interrupt();
				}
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		parent.clientList.remove(this);

	}

	// 强制注销客户端连接，并清理数据和资源
	public void beCancel() {
		userData.upDate("delete from online where ID='" + ID + "'");
		parent.UsermFollow("用户   " + ID + "    被系统管理员强制注销");

		if (addrow) {
			tableModel.removeRow(search(ID));
		}

		del();
		if (follow) {
			parent.SystemFollow("系统运行正常：客户" + ID + " 被系统管理员强制注销，客户端管理者清理其占用的资源完成");
		}

	}

	public void send_Thread(String Message) {
		// TODO 自动生成的方法存根

		try {
			out = new DataOutputStream(client.getOutputStream());
			out.writeUTF(Message);
			if (follow) {

				parent.SystemFollow("系统运行正常：客户端管理者代理客户  " + ID + "  向外发送消息完成");
			}

		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

	}

	public void send_File(final String fileName, final JTextArea jta, final JScrollPane jScrollPane3) {
		parent.UsermFollow("系统向用户 " + ID + "发送文件， 文件路径：" + fileName);

		new Thread() {
			private fileSend fileSender; // 文件发送者对象

			public void run() {

				fileSender = new fileSend(userIP, fileName, jta, ID, jScrollPane3);
				if (follow) {
					parent.SystemFollow("系统运行正常:客户端管理者启动文件发送管理器，向客户 " + ID + "   发送文件");
				}

			}

		}.start();

	}

}
