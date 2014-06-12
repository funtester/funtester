package org.funtester.app.ui.util;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;

/**
 * Table Panel
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class TablePanel extends JPanel {

	private static final long serialVersionUID = 357162193766691417L;
	
	protected final JTable table;
	protected final JScrollPane scrollPane;
	protected TableModel tableModel;

	public TablePanel() {
		setLayout(new BorderLayout(0, 0));
		
		table = new JTable();
		table.setName( "table" );
		table.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
		
		scrollPane = new JScrollPane();
		scrollPane.setViewportView( table );
		scrollPane.setVerifyInputWhenFocusTarget( false );
		scrollPane.setMinimumSize( new Dimension( 300, 50 ) );
		
		add(scrollPane, BorderLayout.CENTER);
	}
	
	public JScrollPane getScrollPane() {
		return scrollPane;
	}
	
	public TableModel getTableModel() {
		return this.tableModel;
	}
	
	public void setTableModel(TableModel model) {
		this.tableModel = model;
		table.setModel( this.tableModel );
	}
	
	public JTable getTable() {
		return table;
	}
	
	public int getSelectedRow() {
		return getTable().getSelectedRow();
	}
	
	public int getRowCount() {
		return getTable().getRowCount();
	}
	
	public void addListSelectionListener(ListSelectionListener l) {
		if ( null == table.getSelectionModel() ) {
			return;
		}
		table.getSelectionModel().addListSelectionListener( l );
	}

	public void addMouseListener(MouseListener l) {
		table.addMouseListener( l );
	}
}
