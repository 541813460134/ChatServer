package com.common;

import java.io.File;

import javax.swing.JFileChooser;

public class getFilePath {
	public String filename;
	
	public getFilePath() {
		
	}
	
	public String getPath() {
		String path = null;
		File file = null;
		JFileChooser jf = new JFileChooser();
		
		int val = jf.showOpenDialog(jf);
		if(val==JFileChooser.APPROVE_OPTION) {
			file = jf.getSelectedFile();
		    filename = file.getName();
			path = file.getPath();
		}
		
		return path;
		
		
		
	}

}
