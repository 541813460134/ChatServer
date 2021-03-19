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

	public String ID;// �й���Ŀͻ����û�ID
	public boolean isLost = true; // �Ƿ񱨸�ͻ����û��ĵ�����Ϊ
	public boolean isLeave = true; // �Ƿ񱨸�ͻ����û���������Ϊ
	public boolean follow = true; // �Ƿ���������ִ�й�������
	private Socket client; // �й���ͻ�������
	private Thread rev; // ��Ϣ�����߳�

	private boolean do_check = true; // �Ƿ�������տͻ�����Ϣ
	private String name; // �й���ͻ����û����ǳ�
	private String password; // �й��߿ͻ����û�������
	private String userIP; // �й��߿ͻ����û���IP;
	private DataInputStream in; // �����
	// ���ݿ�����������,��δʵ������ֻ�ǽ����ϲ�����ߴ�����ֵ
	private DataManager userData;
	private createID IDcreater;
	private Statement st;
	private DataOutputStream out; // �����
	private DefaultTableModel tableModel;
	private boolean canRegedit = true;
	private boolean canLogin = true;
	private ClientManager parent;
	private long timeStart = 0;
	private long timeLast = 0;

	private Thread clearClient;
	private int lostCount = 0;
	private boolean isOnLine = true; // �йܵĿͻ����û��Ƿ�����

	public boolean addrow = true;

	public Clienter(Socket client, DataManager userData, Statement st, DefaultTableModel tableModel, boolean canRegedit,
			boolean canLogin, ClientManager parent, boolean isLeave, boolean isLost, boolean addRow, boolean follow) {
		// TODO �Զ����ɵĹ��캯�����
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
			parent.SystemFollow("ϵͳ��������:�ͻ����й��߳�ʼ�����");
		}

		if (follow) {
			parent.SystemFollow("ϵͳ��������:�ͻ����й���Ϊ�ͻ��������ݿ��������");
		}

		rev_Thread(this);

		if (follow) {
			parent.SystemFollow("ϵͳ��������:�ͻ����й���Ϊ�ͻ����������߳����");
		}

	}

	// �й�����ĺ��Ĵ�������ʵ����Ϣ�Ľ��պͽ���
	private void rev_Thread(final Clienter myself) {
		// TODO �Զ����ɵķ������
		rev = new Thread() {

			public void run() {

				try {
					in = new DataInputStream(client.getInputStream());
					if (follow) {
						parent.SystemFollow("ϵͳ��������:�ͻ����й���Ϊ�ͻ���װ���������");
					}
				} catch (IOException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
					do_check = false;
				}

				while (do_check) {

					try {
						String str = null;
					
						
						str = in.readUTF();
					
						
						
						
						
						if (follow) {
							parent.SystemFollow("ϵͳ��������:�ͻ����й����յ����Կͻ�����Ϣ");
						}

						// �жϿͻ����Ƿ�����,"BYE_BYE"�������ź�
						if (!str.equals("BYE_BYE")) {
							String tag = null;
							if (follow) {
								parent.SystemFollow("ϵͳ��������:�ͻ����й������ڽ�����Ϣ");
							}

							StringTokenizer token = new StringTokenizer(str, ":");

							tag = token.nextToken();

							if (follow) {
								parent.SystemFollow("ϵͳ��������:�ͻ����й��߽����ͻ���Ϣ���,���� ��Ӧ�ͻ�������");
							}

							if (tag.equals("R")) // ע�����
							{
								if (canRegedit) {
									// ������ע���û����ǳ�
									name = token.nextToken();

									password = token.nextToken();

									IDcreater = new createID(st);

									ID = IDcreater.checkID();

									parent.UsermFollow("ϵͳ��⵽�����û�ע��,ע���ǳ�:ID:" + ID + "�ǳ�:" + name + "����:" + password);
									if (userData.upDate("INSERT INTO chat (ID,password,name)" + "VALUES('" + ID + "','"
											+ password + "','" + name + "')")) {
										send_Thread(ID);
										parent.UsermFollow("���û�ע��ɹ�,ע���ǳ�:ID:" + ID + "�ǳ�:" + name + "����:" + password);

									} else {
										send_Thread("" + 0);
										parent.UsermFollow("������ԭ���¸��û�ע��ʧ��");

									}
									// ע��ɹ����й�����Ӧ��ɣ�������Դ
									del();
									if (follow) {
										parent.SystemFollow("ϵͳ��������:�ͻ����й�����Ӧ" + "�ͻ���ע��ʱ����ɣ����ڹرտͻ���" + ID + "ռ�õ���Դ");
									}

									break;
								} else {
									send_Thread("" + -1); // ��ͣע��
									parent.UsermFollow("��������ͣע�����,�ÿͻ�ע��ʧ��");
									del();

									break;
								}

							} else if (tag.equals("L")) // ��¼����
							{
								if (canLogin) {
									ID = token.nextToken(); // ��¼��ID
									password = token.nextToken();
									if (token.hasMoreElements()) {
										userIP = token.nextToken();
									}

									parent.UsermFollow("ϵͳ��⵽���û���¼,��¼ID" + ID + "����:" + password + "IP:" + userIP);

									int result = userData.checkOnLine("select * from chat where ID='" + ID + "'", ID,
											password);

									// �жϸ��û��Ƿ��ѵ�¼
									if (follow) {
										parent.SystemFollow("ϵͳ��������:�ͻ����й�������Ϊ��Ӧ�ͻ�" + ID + "�ĵ�¼�¼�����ѯ���ݿ�");

									}

									if (result == 1) { // ���û�δ��¼
										// ��ѯ���û����ǳ�
										name = userData.getDate("select * from chat where ID ='" + ID + "'", "name");
										parent.UsermFollow("�û�" + ID + "��¼�ɹ��������û��ǳ�:" + name);

										if (userData.upDate("INSERT into online(ID,IP,name)VALUES('" + ID + "','"
												+ userIP + "','" + name + "')")) {

											if (follow) {
												parent.SystemFollow("ϵͳ��������:�ͻ����йܽ��ͻ�" + ID + "�ĵ�¼��Ϣд�����ݿ�");
											}

											String[] userInfo = new String[] { ID, userIP, name };
											if (addrow) {
												tableModel.addRow(userInfo);
											}
											if (follow) {
												parent.SystemFollow("ϵͳ��������:�ͻ����йܽ��ͻ�" + ID + "����Ϣ��������û��б�");

											}

											// �ط���¼�û����ǳ�
											send_Thread(name);
											timeStart = System.currentTimeMillis();

											// �������õĵ��߼�������
											clearClient();
											if (follow) {
												parent.SystemFollow("ϵͳ��������:�ͻ����йܽ��ͻ�" + ID + "�������߼�������");

											}

										} else {
											// ���û��ѵ�¼
											send_Thread(-2 + "");
											parent.UsermFollow("�û�:" + ID + " ��¼ʧ�ܣ����û��ѵ�¼");
											del();
											break;
										}

									} else {
										send_Thread(result + "");
										if (result == 0) { // 0,�������
											parent.UsermFollow("�û�:" + ID + " ��¼ʧ�ܣ��������");
										} else if (result == -1) {
											parent.UsermFollow("�û�:" + ID + " ��¼ʧ�ܣ����˺�δע��");

										}
									}

								} else {
									send_Thread(-3 + ""); // ��ͣ��¼����
									parent.UsermFollow("�û�:" + ID + " ��¼ʧ�ܣ�ϵͳ��ͣ��¼����");
									del();
									if (follow) {
										parent.SystemFollow("ϵͳ��������:�ͻ�" + ID + "��¼ʧ�ܣ��ͻ��˹��������ڹر���ռ�õ���Դ");
									}

									break;

								}
							} else if (tag.endsWith("F_List")) { // �ͻ�����������б�
								send_Thread(userData.getFriends(ID));
								parent.UsermFollow("���������û�:" + ID + " ���ͺ����б����");

							} else if (str.startsWith("ADD_Fr:")) { // �ͻ�����Ӻ���
								send_Thread("" + userData.upDate("insert into friends(ID,friendID,friendName)"
										+ "values('" + str.substring(7, 16) + "','" + str.substring(16, 25) + "','"
										+ str.substring(25, str.length()) + "')"));
								parent.UsermFollow("��������Ӧ�û�:" + ID + " ����Ӻ����������");
							} else if (tag.endsWith("Se_Fri")) {
								// �ͻ��˲��Һ���
								String result = userData.getSearch(
										"select * from chat where ID='" + str.substring(7,str.length()) + "'");
								//System.out.println(str.substring(7,str.length()));

								parent.UsermFollow("��������Ӧ�û�:" + ID + " �Ĳ��Һ����������");

								if (result.equals("NONESE")) {

									send_Thread("NONESE");
								//	System.out.println(result);

								} else {
									send_Thread("Se_Fri" + result);
								//	System.out.println(result);
								}
								parent.UsermFollow("���������û�:" + ID + " ���ͺ��Ѳ��ҽ�����");

							} else if (tag.endsWith("Fri_ip")) {
								// �ͻ���������ѵ�IP��ַ
								send_Thread("Fri_ip" + userData.getFri_ip(str.substring(7, str.length())));

								parent.UsermFollow("��������Ӧ�û�:" + ID + " ���������IP���");

							} else if (str.startsWith("SYS_IN")) {
								// �ͻ��˵�������֤��Ϣ
								timeStart = System.currentTimeMillis();

								if (follow) {
									parent.SystemFollow("ϵͳ��������:���߼���������յ��ͻ�:    " + ID + "   " + "��������Ϣ���ÿͻ���Ȼ����");
								}

							} else if (str.startsWith("DEL_FR")) // �ͻ���ɾ������
							{
								parent.UsermFollow("�������յ��ͻ�" + ID + "��ɾ�������������ڲ������ݿ��������Ӧ");

								String friendID = str.substring(7, str.length());

								if (userData.upDate(
										"delete from friends where ID ='" + ID + "'and friendID='" + friendID + "'")) {

									send_Thread("DEL_FR_ACCESS");
									send_Thread(userData.getFriends(ID));

									parent.UsermFollow("��������Ӧ �û� " + ID + "��ɾ������������ɣ�ɾ���ɹ�");

								} else {
									send_Thread("DEL_FR_FAILE");
									parent.UsermFollow("��������Ӧ �û� " + ID + "��ɾ������������ɣ�ɾ��ʧ��");

								}

							} else if (str.startsWith("SYSQUN")) { // �ͻ��˷���Ⱥ��Ϣ

								parent.qunMultiCast("QUN_ME:" + str.substring(7, str.length()) + ID, myself);
								parent.displayQUNMessage(ID, str.substring(7, str.length()));

								parent.UsermFollow("�������յ� �û� " + ID + "���͵�Ⱥ��Ϣ,��Ϣ������ɣ�����������Ⱥ�û�ת��");

							} else { // ������,���˷Ƿ�����Ϣ

								if (follow) {
									parent.SystemFollow("ϵͳ�����쳣:�ͻ��˹����������յ����Կͻ�  " + ID + "  ����Ϣ");

								}

								JOptionPane.showMessageDialog(null, "δ���˵���Ϣ�������");

							}
						} else { // �û���������,������Դ


							userData.upDate("delete from online where ID='"+ID+"'");
							parent.UsermFollow("���������� �û� " + ID + " ռ�õ�ϵͳ��Դ���");
							isOnLine = false;

							if (isLeave) {
								JOptionPane.showMessageDialog(null,
										"��⵽���û����ߣ�\r\n�û�ID��" + ID + "\r\n�û��ǳ�:"+name+"\r\n" );
							}
							
							
							if(addrow) {
								tableModel.removeRow(search(ID));
							}
							
							if(follow) {
								parent.SystemFollow("ϵͳ�����������ͻ��˹������������߿ͻ�"+ID+" ռ�õ�ϵͳ��Դ�������ÿͻ��������б���ɾ��");
							}

						}

					} catch (IOException e) {
						// TODO �Զ����ɵ� catch ��
						
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

	// ���õĵ��߼�������
	public void clearClient() {
		// TODO �Զ����ɵķ������
		clearClient = new Thread() {

			public void run() {
				while (isOnLine) {
					timeLast = System.currentTimeMillis();

					if ((timeLast - timeStart) > 25000) {
						if (lostCount > 2) {
							userData.upDate("delete from online where ID='" + ID + "'");

							if (follow) {
								parent.SystemFollow("ϵͳ��������:�ͻ��˹����߷��ֿͻ�  " + ID + "  �޹ʵ��ߣ�\r\n");
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
							// TODO �Զ����ɵ� catch ��
							e.printStackTrace();
						}

					}

				}

			}

		};

		clearClient.start();

	}

	private void del() {
		// TODO �Զ����ɵķ������
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
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		parent.clientList.remove(this);

	}

	// ǿ��ע���ͻ������ӣ����������ݺ���Դ
	public void beCancel() {
		userData.upDate("delete from online where ID='" + ID + "'");
		parent.UsermFollow("�û�   " + ID + "    ��ϵͳ����Աǿ��ע��");

		if (addrow) {
			tableModel.removeRow(search(ID));
		}

		del();
		if (follow) {
			parent.SystemFollow("ϵͳ�����������ͻ�" + ID + " ��ϵͳ����Աǿ��ע�����ͻ��˹�����������ռ�õ���Դ���");
		}

	}

	public void send_Thread(String Message) {
		// TODO �Զ����ɵķ������

		try {
			out = new DataOutputStream(client.getOutputStream());
			out.writeUTF(Message);
			if (follow) {

				parent.SystemFollow("ϵͳ�����������ͻ��˹����ߴ���ͻ�  " + ID + "  ���ⷢ����Ϣ���");
			}

		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}

	}

	public void send_File(final String fileName, final JTextArea jta, final JScrollPane jScrollPane3) {
		parent.UsermFollow("ϵͳ���û� " + ID + "�����ļ��� �ļ�·����" + fileName);

		new Thread() {
			private fileSend fileSender; // �ļ������߶���

			public void run() {

				fileSender = new fileSend(userIP, fileName, jta, ID, jScrollPane3);
				if (follow) {
					parent.SystemFollow("ϵͳ��������:�ͻ��˹����������ļ����͹���������ͻ� " + ID + "   �����ļ�");
				}

			}

		}.start();

	}

}
