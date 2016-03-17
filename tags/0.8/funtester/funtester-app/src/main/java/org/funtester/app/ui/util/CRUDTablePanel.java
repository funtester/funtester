package org.funtester.app.ui.util;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Action;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * CRUD table panel
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class CRUDTablePanel extends TablePanel {
	
	private static final long serialVersionUID = 9053425751834219953L;

	private boolean editable = true;
	
	private Action newAction;
	private Action cloneAction;
	private Action editAction;
	private Action removeAction;
	private Action duplicateAction;
	private Action upAction;
	private Action downAction;
	
	public CRUDTablePanel() {
		
		// To select an item updates the actions
		
		ListSelectionListener lsl = new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if ( e.getValueIsAdjusting() ) {
					return;
				}
				updateActionsEnabledStatus( getTable().getSelectedRow() );
			}
		};
		getTable().getSelectionModel().addListSelectionListener( lsl );
		
		// Double click fires the edit action
		
		MouseAdapter ma = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if ( MouseEvent.BUTTON1 == e.getButton()
						&& 2 == e.getClickCount()
						&& getEditAction() != null
						&& getEditAction().isEnabled() ) {
					getEditAction().actionPerformed( null );
				}
			}
		};
		
		getTable().addMouseListener( ma );
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
		// Update the actions
		updateActionsEnabledStatus( getTable().getSelectedRow() );
		// Update the table background
		final Color color = 
				editable ? java.awt.SystemColor.window : java.awt.SystemColor.control;
		getTable().setBackground( color );		
	}
	
	public Action getNewAction() {
		return newAction;
	}
	
	public void setNewAction(Action action) {
		if ( null == action ) throw new IllegalArgumentException( "null action" );
		newAction = action;
	}
	
	public Action getCloneAction() {
		return cloneAction;
	}
	
	public void setCloneAction(Action action) {
		if ( null == action ) throw new IllegalArgumentException( "null action" );
		cloneAction = action;
		cloneAction.setEnabled( editable && getTable().getSelectedRow() >= 0 );
	}	
	
	public Action getEditAction() {
		return editAction;
	}
	
	public void setEditAction(Action action) {
		if ( null == action ) throw new IllegalArgumentException( "null action" );
		editAction = action;
		editAction.setEnabled( editable && getTable().getSelectedRow() >= 0 );
	}
	
	public Action getRemoveAction() {
		return removeAction;
	}
	
	public void setRemoveAction(Action action) {
		if ( null == action ) throw new IllegalArgumentException( "null action" );
		removeAction = action;
		removeAction.setEnabled( editable && getTable().getSelectedRow() >= 0 );
	}
	
	public Action getDuplicateAction() {
		return duplicateAction;
	}

	public void setDuplicateAction(Action action) {
		if ( null == action ) throw new IllegalArgumentException( "null action" );
		duplicateAction = action;
		duplicateAction.setEnabled( editable && getTable().getSelectedRow() >= 0 );
	}

	public Action getUpAction() {
		return upAction;
	}

	public void setUpAction(Action action) {
		if ( null == action ) throw new IllegalArgumentException( "null action" );
		upAction = action;
	}

	public Action getDownAction() {
		return downAction;
	}

	public void setDownAction(Action action) {
		if ( null == action ) throw new IllegalArgumentException( "null action" );
		downAction = action;
	}
	
	protected void updateActionsEnabledStatus(int index) {
		final int rowCount = getTable().getRowCount();
		
		if ( getNewAction() != null ) {
			getNewAction().setEnabled( editable );
		}
		
		if ( getCloneAction() != null ) {
			getCloneAction().setEnabled( editable && rowCount > 0 && index >= 0 );
		}
		
		if ( getEditAction() != null ) {
			getEditAction().setEnabled( editable && rowCount > 0 && index >= 0 );
		}
		
		if ( getRemoveAction() != null ) {
			getRemoveAction().setEnabled( editable && rowCount > 0 && index >= 0 );
		}
		
		if ( getDuplicateAction() != null ) {
			getDuplicateAction().setEnabled( editable && rowCount > 0 && index >= 0 );
		}
		
		if ( getUpAction() != null ) {
			getUpAction().setEnabled( editable && rowCount > 1 && index > 0 );
		}
		
		if ( getDownAction() != null ) {
			getDownAction().setEnabled( editable && rowCount > 1 && index >= 0 && index < rowCount - 1 );
		}
	}
	
}
