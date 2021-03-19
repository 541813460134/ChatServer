package com.tools;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

public class createID {
	private String ID = "";
	private Random create;
	private Statement st;
	
	public createID(Statement st) {
		this.st = st;
	}
	
	
	public boolean check(String sql) {
		boolean check_id = false;
		
		try {
			st.executeQuery(sql);
		} catch (SQLException e) {
			check_id = true;
			e.printStackTrace();
		}
		
		return check_id;
	}
	
	public String checkID() {
		while(check("Select ALL * FROM chat where ID='"+getID()+"'"))
			break;
		return ID;
	}


	public String getID() {
		ID ="123";
		create = new Random();
		for(int i=0;i<6;i++) {
			ID=ID+create.nextInt(8);
		}
		
		return ID;
	}

}
