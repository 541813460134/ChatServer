package com.common;

import java.awt.Color;

import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class insertTextPane {
	private JTextPane textPane ; //�ı�����
	private Document doc ;
	private int fontSize = 12;
	
	public insertTextPane(JTextPane textPane) {
		this.textPane = textPane;
	}
	
	public void insert(String str,AttributeSet attrSet) {
		doc =textPane.getDocument();
		str = str+"\r\n";
		
		try {
			doc.insertString(doc.getLength(), str, attrSet);
		} catch (BadLocationException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		
		
	}
	
	public void setDocs(String str,Color col) {
		SimpleAttributeSet attrSet = new SimpleAttributeSet();
		StyleConstants.setForeground(attrSet, col);  //������ɫ
		StyleConstants.setBold(attrSet, true);
		
		StyleConstants.setFontSize(attrSet, fontSize); //�����С
		
		insert(str,attrSet);
		
		
	}
	
	
	

}
