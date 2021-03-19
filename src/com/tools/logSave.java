package com.tools;
import java.io.*;

public class logSave {
	private FileWriter fw;
	public logSave(){
		
	}
	
	
	public boolean Sava(String log,File saveFilename) {
		boolean result = false;
		
		
		try {
			fw = new FileWriter(saveFilename);
			fw.write(log);
			fw.close();
			
			result = true;
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		
		return result;
		
	}
	
	
	

}
