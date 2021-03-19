package com.handle;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.util.Iterator;
import java.util.List;

import javax.print.DocFlavor.URL;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class MyFileTransferHandler {
	private JTextArea jta;
	private JTextField jtf;
	public MyFileTransferHandler(JTextArea jTextArea,JTextField jTextField) {
		this.jta=jTextArea;
		this.jtf=jTextField;
	}
	public boolean canImport(JComponent arg0,DataFlavor[] arg1) {
		for(int i=0;i<arg1.length;i++) {
			DataFlavor flavor=arg1[i];
			if(flavor.equals(DataFlavor.javaFileListFlavor)) {
				System.out.println("canImport:JavaFileList FLAVOR:"+flavor);
				return true;
			}
			if(flavor.equals(DataFlavor.javaFileListFlavor)) {
				System.out.println("canImport:String FLAVOR:"+flavor);
				return true;
			}
			System.out.println("canImport:Rejected FLAVOR:"+flavor);
		}
		return false;
		
	}
	public boolean importData(JComponent comp,Transferable t) {
		DataFlavor[] flavors=t.getTransferDataFlavors();
		System.out.println("Trying to import:"+t);
		System.out.println("  which has"+flavors.length+"flavor.");
		for(int i=0;i<flavors.length;i++) {
			DataFlavor flavor=flavors[i];
			try {
				if(flavor.equals(DataFlavor.javaFileListFlavor)) {
					System.out.println("importData:FileListFlavor");
					List l=(List)t.getTransferData(DataFlavor.javaFileListFlavor);
					Iterator iter=l.iterator();
					while(iter.hasNext()) {
						File file=(File)iter.next();
						if(file.isFile()) {
							getFileInfo(file);
							jtf.setText(file.getCanonicalPath());
						}else {
							JOptionPane.showMessageDialog(comp, "你拖入的东西不是文件！");
							
						}
					}
					return true;
				}else if(flavor.equals(DataFlavor.stringFlavor)) {
					System.out.println("importData:String Flavor");
					String fileOrURL=(String)t.getTransferData(flavor);
					System.out.println("GOT STRING:"+fileOrURL);
					try {
						URL url=new URL(fileOrURL);
						System.out.println("Valid URL:"+url.toString());
						return true;
					}catch(Exception e) {
						System.out.println("Not a valid URL");
						return false;
					}
				}else {
					System.out.println("importData rejected:"+flavor);
				}
			}catch(Exception ex) {
				System.out.println(ex); 
			}
		}
		Toolkit.getDefaultToolkit().beep();
		return false;
		
	}
	private void getFileInfo(File file) {
		jta.append("文件名："+file.getName()+"\r\n");
		jta.append("文件路径："+file.getPath()+"\r\n");
		jta.append("文件长度："+file.length()+"\r\n");
		// TODO Auto-generated method stub
		
	}

}
