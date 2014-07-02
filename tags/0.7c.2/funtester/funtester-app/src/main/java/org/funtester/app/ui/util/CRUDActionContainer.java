package org.funtester.app.ui.util;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.funtester.common.util.Announcer;
import org.funtester.core.util.UpdateListener;

/**
 * A simple action container that listens to a list (or table) and keeps a
 * reference to the selected object. It can be useful to create CRUD operations
 * where we have to get the selected object for updating or deleting purposes.
 * 
 * @author Thiago Delgado Pinto
 *
 * @param <T>	the type for the selected object.
 */
public abstract class CRUDActionContainer< T > implements ListSelectionListener {
	
	private final BaseTableModel< T > tableModel;
	private T selectedObject = null;
	private int selectedIndex = -1;
	
	// Not used directly for this class but it can be useful for child classes
	private final Announcer< UpdateListener > announcer =
			Announcer.to( UpdateListener.class );
	
	/**
	 * Create the action container using a table model.
	 * 
	 * @param tableModel the table model to be used.
	 */
	public CRUDActionContainer(BaseTableModel< T > tableModel) {
		this.tableModel = tableModel;
	}

	/**
	 * Add an {@code UpdateListener}.
	 * 
	 * @param listener	the update listener to add.
	 * @return			true if added, false otherwise.
	 */
	public boolean addListener(UpdateListener listener) {
		return announcer.addListener( listener );
	}

	/**
	 * Remove an {@code UpdateListener}.
	 * 
	 * @param listener	the update listener to removed.
	 * @return			true if removed, false otherwise.
	 */	
	public boolean removeListener(UpdateListener listener) {
		return announcer.removeListener( listener );
	}	
	
	/**
	 * Return the selected object.
	 * @return
	 */
	public T getSelectedObject() {
		return selectedObject;
	}
	
	/**
	 * Return true whether an object is selected, false otherwise.
	 * @return
	 */
	public boolean isSelectionEmpty() {
		return ( null == getSelectedObject() );
	}	
	
	/**
	 * Return the selected index (single selection).
	 * @return
	 */
	public int getSelectedIndex() {
		return selectedIndex;
	}
	
	@Override
	public void valueChanged(ListSelectionEvent e) {
		if ( e.getValueIsAdjusting() ) { return; }
		
		//
		// Update the selected index and the selected object
		//		
		ListSelectionModel lsm = (ListSelectionModel) e.getSource();
		if ( lsm.isSelectionEmpty() ) { // Nothing selected
			setSelectedIndex( -1 );
			setSelectedObject( null );
			return;
		}
		// Selects the first object
		final int FIRST_INDEX = lsm.getMinSelectionIndex();
		setSelectedIndex( FIRST_INDEX );
		setSelectedObject( tableModel.itemAt( FIRST_INDEX ) );
	}
	
	//
	// PROTECTED
	//
	
	protected void setSelectedObject(T obj) {
		this.selectedObject = obj;
	}
	
	protected void setSelectedIndex(int index) {
		this.selectedIndex = index;
	}	
	
	protected Announcer< UpdateListener > getAnnouncer() {
		return announcer;
	}	
	
	protected BaseTableModel< T > getTableModel() {
		return tableModel;
	}

}
