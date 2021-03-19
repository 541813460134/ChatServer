package com.tools;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class TableFill {
	
	private Class[] types;
	private boolean[] canEdit;
	private int colunms = 0;
	public DefaultTableModel myModel;
	private TableColumnModel tcm;
	private TableColumn tc;
	
	public TableFill(){
		
	}
	
	public Class[] types() {
		types = new Class[colunms];
		
		for(int i=0;i<colunms;i++) {
			types[i] = java.lang.String.class;
		}
		return types;
	}
	
	public boolean[] canEdit() {
		
		canEdit = new boolean[colunms];
		for(int i=0;i<colunms;i++) {
			canEdit[i] = false;
		}
		return canEdit;
	}
	
	//实际的数据填充过程
	public boolean fillDate(int colunms,String[][] data,String[] colunm,
			JTable jTable)
	{
		boolean result = false;
		this.colunms = colunms;
		types();
		canEdit();
		jTable.setVisible(true);
		jTable.setBackground(new java.awt.Color(198,62,4));
		jTable.getTableHeader().setVisible(true);
		
		myModel = new javax.swing.table.DefaultTableModel(data,colunm) {
			public Class getColumnClass(int columnIndex) {
				return types[columnIndex];
			}
			
			public boolean isCellEdittable(int rowIndex,int columnIndex) {
				return canEdit[columnIndex];
			}	
			
		};
		jTable.setModel(myModel);
		jTable.getTableHeader().setReorderingAllowed(false);
		tcm = jTable.getColumnModel();
		
		if(colunms<5) {
			for(int i=0;i<jTable.getColumnCount();i++) {
				tc = tcm.getColumn(i);
				tc.setPreferredWidth(750/colunms+10);
			}	
		}else {
			for(int i=0;i<jTable.getColumnCount();i++) {
				tc = tcm.getColumn(i);
				tc.setPreferredWidth(150);
			}
			
			
		}
		
		
		return result;
		
		
		
	}
	
	
	
	
	

}
