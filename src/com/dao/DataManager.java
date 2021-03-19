package com.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

import com.tools.Sys_Timer;
import com.tools.TableFill;

public class DataManager {
	
	private Connection con;
	private Statement st;
	private int numCols = 0;
	private TableFill tableFiller;
	private String[] colunm;
	private String friends;
	private JTextArea jta;
	private Sys_Timer timer = new Sys_Timer();
	private static int count = 0;
	public DataManager(JTextArea jta)
	{
		this.jta = jta;
		tableFiller = new TableFill();
		count++;
		if(jta!=null)
		{
			jta.append(timer.getSysTime()+"数据库跟踪者报告数据管理者类已被实例化"+count+"次\r\n");
		}
			
	}
	public void follow(JTextArea jta) {
		this.jta = jta;
		if(jta!=null) {
			jta.append(timer.getSysTime()+"数据库跟踪者开始跟踪数据库执行过程\r\n");
		}
		
		
	}
	
	public synchronized Statement getMySQLConnection(String URL,String password) {
		
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			if(jta!=null)
			{
				jta.append(timer.getSysTime()+"数据库管理者为MySQL数据库加载驱动完成\r\n");
			}
			
			con = DriverManager.getConnection(URL, "root", password);
			if(jta!=null)
			{
				jta.append(timer.getSysTime()+"数据库管理者为MySQL数据库建立连接完成\r\n");
			}
			
			st = con.createStatement();
			if(jta!=null)
			{
				jta.append(timer.getSysTime()+"数据库管理者为MySQL数据库创建连接对象完成\r\n");
			}
			
			
			
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		
		return st;
		
	}
	
	public synchronized boolean upDate(String sql) {
		
		boolean result = false;
		
		try {
			st.executeUpdate(sql);
			if(jta!=null)
			{
				jta.append(timer.getSysTime()+"数据库管理者执行SQL语句:"+sql+",成功完成\r\n");
			}
			
			result = true;
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			if(jta!=null)
			{
				jta.append("\r\n"+timer.getSysTime()+"异常！！执行SQL语句:"+sql+",发生如下错误:"+e+"\r\n");
			}
			result = false;
		}
		
		
		return result;
		
		
		
		
	}
	
	public String getDate(String sql,String column) {
		
		String result = null;
		ResultSet rs = null;
		
		try {
			rs = st.executeQuery(sql);
			while(rs.next()) {
				result = rs.getString(column);
			}
			if(jta!=null)
			{
				jta.append(timer.getSysTime()+"数据库管理者执行SQL语句:"+sql+",成功完成\r\n");
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			if(jta!=null)
			{
				jta.append("\r\n"+timer.getSysTime()+"异常！！执行SQL语句:"+sql+",发生如下错误:"+e+"\r\n");
			}
		}
		
		
		return result;
			
		
	}
	
	
	public String getFriends(String ID) {
		
		int i = 0;
		String sql = "select * from friends where ID='"+ID+"'";
		friends ="F_List";
		ResultSet rs = null;
		
		try {
			rs = st.executeQuery(sql);
			while(rs.next()) {
				i++;
			}
			rs.close();
			friends = friends+":"+i;
			System.out.println(i+"条记录");
			if(i>0) {
				rs = st.executeQuery(sql);
				while(rs.next()) {
					friends = friends+":"+rs.getString("friendName")+"("+rs.getString("friendID")+")";
					
					
				}
				
				rs.close();
				
				if(jta!=null)
				{
					jta.append(timer.getSysTime()+"数据库管理者执行SQL语句:"+sql+",成功完成\r\n");
				}
				
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			if(jta!=null)
			{
				jta.append("\r\n"+timer.getSysTime()+"异常！！执行SQL语句:"+sql+",发生如下错误:"+e+"\r\n");
			}
		}
		
	return friends;
		
	}
	
	
	public String getFri_ip(String ID) {
		ResultSet rs = null;
		String ip = null;
		String sql = "select * from online where ID ='"+ID+"'";
		
		
		try {
			rs = st.executeQuery(sql);
			while(rs.next()) {
				ip = rs.getString("IP");
			}
			rs.close();
			
			if(jta!=null)
			{
				jta.append(timer.getSysTime()+"数据库管理者执行SQL语句:"+sql+",成功完成\r\n");
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			if(jta!=null)
			{
				jta.append("\r\n"+timer.getSysTime()+"异常！！执行SQL语句:"+sql+",发生如下错误:"+e+"\r\n");
			}
		}
		
		
		return ip;
		
		
	}
	
	public int checkOnLine(String sql,String ID,String password) {
		int info_check = -1;
		ResultSet rs = null;
		
		try {
			rs = st.executeQuery(sql);
			
			for(int i=0; rs.next(); i++) {
				if(rs.getString("ID").equals(ID)) {
					
					 info_check = 0; //密码错误
					if(rs.getString("password").equals(password)) {
						info_check = 1; //登录成功
						break;
					}			
					
				}
				
				
				rs.close();
				
				
			}	
			
			
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		
		return info_check;
	}
	
	public String[][] getData(String sql){
		String[][] result = null;
		ResultSet rs ;
		
		
		try {
			rs = st.executeQuery(sql);

		} catch (SQLException e) {
			e.printStackTrace();
			Logger.getLogger(DataManager.class.getName()).log(Level.SEVERE,null,e);
		}
		
		int i = 0;
		
		try {
			rs = st.executeQuery(sql);
			while(rs.next()) {
				i++;
			}
			rs.close();
			result = new String[i][numCols];
			if(i>0) {
				rs = st.executeQuery(sql);
				
				i=0;
				
				while(rs.next()) {
					
					for(int j=0; j<numCols;j++) {
						result[i][j] = rs.getString(j+1);
					}
					i++;
				}
				
				
				rs.close();
				if(jta!=null)
				{
					jta.append(timer.getSysTime()+"数据库管理者执行SQL语句:"+sql+",成功完成\r\n");
				}
					
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			if(jta!=null)
			{
				jta.append("\r\n"+timer.getSysTime()+"异常！！执行SQL语句:"+sql+",发生如下错误:"+e+"\r\n");
			}
		}
		
		
		return result;
	}
	
	//执行数据库查询语句,将得到的数据集填充在指定的表中
	public void getDateToTable(String sql,JTable table) {
		
		boolean result = false;
		
		ResultSet rs;
		try {
			rs = st.executeQuery(sql);
			getColunms(rs);
			tableFiller.fillDate(numCols,getData(sql),colunm,table);
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
			
		
	}
	
	public DefaultTableModel getTableModel() {
		return tableFiller.myModel;
	}
	
	//获得指定数据集的列值
	public String[] getColunms(ResultSet rs) {
		ResultSetMetaData rsmd = null;
		
		try {
			rsmd = rs.getMetaData();
			
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			if(jta!=null)
			{
				jta.append("\r\n"+timer.getSysTime()+"异常！！,数据库管理者获得结果集时发生如下错误:"+e+"\r\n");
			}
		}
		
		try {
			numCols = rsmd.getColumnCount();
			if(!rs.isClosed()) {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			if(jta!=null)
			{
				jta.append("\r\n"+timer.getSysTime()+"异常！！,数据库管理者获得结果集时发生如下错误:"+e+"\r\n");
			}
		}
		
		
		colunm = new String[numCols];
		for(int i=0;i<numCols;i++) {
			
			try {
				colunm[i] = rsmd.getColumnLabel(i+1);
			} catch (SQLException e) {
				e.printStackTrace();
				if(jta!=null)
				{
					jta.append("\r\n"+timer.getSysTime()+"异常！！,数据库管理者获得结果集时发生如下错误:"+e+"\r\n");
				}
			}
			
			
			
			
		}
		return colunm;
		

	}
	public synchronized void closer() {
		
			try {
				if(st!=null) {
				st.close();
				}
				
				if(con!=null) {
					con.close();
				}
				
				
				if(jta!=null)
				{
					jta.append(timer.getSysTime()+"数据库管理者关闭数据库连接成功完成\r\n");
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
				if(jta!=null)
				{
					jta.append("\r\n"+timer.getSysTime()+"异常！！,数据库管理者在关闭数据库连接时错误:"+e+"\r\n");
				}
			}
		
		
		
	}
	public String getSearch(String sql) {
		
		String result = "";
		
		
		ResultSet rs;
		try {
			rs = st.executeQuery(sql);
			while(rs.next()) {
				result = result+rs.getString("ID");
				result = result+rs.getString("name");
			}
			
			if(jta!=null)
			{
				jta.append(timer.getSysTime()+"数据库管理者执行SQL语句:"+sql+",成功完成\r\n");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			if(jta!=null)
			{
				jta.append("\r\n"+timer.getSysTime()+"异常！！执行SQL语句:"+sql+",发生如下错误:"+e+"\r\n");
			}
		}
		
		if(result.length()<10) {
			
			result = "NONESE";
		}
		
		
		return result;
		
	}
	

}
