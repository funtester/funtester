package org.funtester.app.ui.util;

import java.awt.Component;
import java.awt.Cursor;

/**
 * Simple class to manage the cursor state of a component. 
 * 
 * @author Thiago Delgado Pinto
 *
 */
public final class CursorController {

	private final Component component;
	private Cursor savedCursor = null;
	
	public CursorController(Component component) {
		if ( null == component ) throw new IllegalArgumentException( "component can't be null" );
		this.component = component;
	}
	
	/**
	 * Save the current cursor. 
	 */
	public void saveCursor() {
		savedCursor = component.getCursor();
	}
	
	/**
	 * Return {@code true} whether there is a saved cursor.
	 * @return
	 */
	public boolean hasSavedCursor() {
		return savedCursor != null;
	}
	
	/**
	 * Change the current cursor to a busy/wait cursor. If the cursor was
	 * not saved, saves it before changing.
	 */
	public void changeToBusyCursor() {
		if ( ! hasSavedCursor() ) {
			saveCursor();
		}
		component.setCursor( Cursor.getPredefinedCursor( Cursor.WAIT_CURSOR ) );
	}
	
	/**
	 * Restore a saved cursor.
	 */
	public void restoreCursor() {
		if ( hasSavedCursor() ) {
			component.setCursor( savedCursor );
		}
	}

}
