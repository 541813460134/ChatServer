package com.handle;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.tools.Sys_Timer;

public class fileSend {
	private Socket server=null;
	private DataInputStream fileIn;
	private DataOutputStream fileOut;
	public String fileName;
	private int n=0;
	//
	private byte[] data =new byte[512];
	private Sys_Timer timer=new Sys_Timer();
	private long timeStart;
	private long timeEnd;
	private long timeUse;
	private long length;
	private float speed;  //速度
	private String ID;
	private JScrollPane jScrollPane3;
	//
	public fileSend(String ip,String fileName,JTextArea jta,String ID,JScrollPane jScrollPane3) {
		try {
			this.fileName=fileName;
			this.ID=ID;
			this.jScrollPane3=jScrollPane3;
			server=new Socket(ip,6667);
			fileIn=new DataInputStream(new FileInputStream(fileName));
			fileOut=new DataOutputStream(server.getOutputStream());
			n=fileIn.read(data);
			jta.append("\r\n"+timer.getSysTime()+"文件发送管理器开始向用户"+ID+"发送文件：");
			jScrollPane3.getVerticalScrollBar().setValue(jScrollPane3.getVerticalScrollBar().
					getMaximum());
			timeStart=System.currentTimeMillis();
			while(n!=-1) {
				fileOut.write(data);
				n=fileIn.read(data);
			}
			fileIn.close();
			fileOut.close();
			server.close();
		}catch(Exception e) {
			Logger.getLogger(fileSend.class.getName()).log(Level.SEVERE,null,e);
			
		}
		jta.append("\r\n"+timer.getSysTime()+"文件发送管理器开始向用户"+ID+"发送文件完成：");
		jScrollPane3.getVerticalScrollBar().setValue(jScrollPane3.getVerticalScrollBar().
				getMaximum());
		timeTest(jta);
	}
	//
	private void timeTest(JTextArea jta) {
		String unit="字节";
		timeEnd=System.currentTimeMillis();
		timeUse=timeEnd-timeStart;
		File file=new File(fileName);
		length=file.length();
		if(length<1024) {
			unit="字节";
			speed=length;
		}else if(length<1024*1024) {
			unit="K";
			speed=(float)(length/1024);
		}else {
			unit="M";
			speed=(((float)length)/((float)(1024*1024)));
			
		}
		if(timeUse>1000) {
			jta.append("\r\n\r\n技术统计：文件发送管理器共向"+ID+"发送文件长度："+speed+unit+","
					+ "共用时："+(float)(timeUse/1000)+"秒，平均速度："+((speed*1000)/timeUse)+""+
					unit+"/秒\r\n\r\n");
			
		}else if(timeUse>0) {
			jta.append("\r\n\r\n技术统计：文件发送管理器共向"+ID+"发送文件长度："+speed+unit+","
					+ "共用时："+(timeEnd-timeStart)+"毫秒，平均速度："+speed*1000+""+
					unit+"/秒\r\n\r\n");
		}else {
			jta.append("\r\n\r\n技术统计：文件发送管理器共向"+ID+"发送文件长度："+speed+unit+","
					+ "共用时："+(timeEnd-timeStart)+"毫秒，平均速度："+speed*1000+""+
					unit+"/秒\r\n\r\n");
		}
		jScrollPane3.getVerticalScrollBar().setValue(jScrollPane3.getVerticalScrollBar().
				getMaximum());
		// TODO Auto-generated method stub
		
	}
	
}
