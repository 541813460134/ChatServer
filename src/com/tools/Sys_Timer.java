package com.tools;



import java.text.SimpleDateFormat;
import java.util.Calendar;



public class Sys_Timer {

	private String date ="";
	public String getSysTime()
	{
		  
		Calendar calendar= Calendar.getInstance();
		SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    
       date = dateFormat.format(calendar.getTime());
		
		return date;
		
	}
	
	public static void main(String[] args) {
		Sys_Timer s = new Sys_Timer();
		System.out.println(s.getSysTime());
	}
	
	
}
