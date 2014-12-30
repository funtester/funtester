package org.funtester.app.ui.util;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;
import javax.swing.JTree;

import org.funtester.common.util.Announcer;

/**
 * Generic tree mouse adapter makes the LEFT click to show a popup menu
 * and a DOUBLE click to fire {@link CommandEventListener#execute(Object)}
 * method.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class GenericTreeMouseAdapter extends MouseAdapter {
	
	private final JTree tree;
	private final JPopupMenu popupMenu;
	private final Announcer< CommandEventListener > commandAnnouncer;
	
	/**
	 * Creates the mouse adapter
	 * 
	 * @param tree		the tree to get the selection path. 
	 * @param popupMenu	the popup menu to be shown when LEFT clicked.
	 */
	public GenericTreeMouseAdapter(JTree tree, JPopupMenu popupMenu) {
		this.tree = tree;
		this.popupMenu = popupMenu;
		this.commandAnnouncer = Announcer.to( CommandEventListener.class );
	}
	
	/**
	 * Add a listener.
	 * 
	 * @param listener	the listener to be added.
	 * @return			true if added, false otherwise.
	 */
	public boolean addCommandEventListener(CommandEventListener listener) {
		return commandAnnouncer.addListener( listener );
	}

	/**
	 * Remove a listener.
	 * 
	 * @param listener	the listener to be removed.
	 * @return			true if removed, false otherwise.
	 */	
	public boolean removeCommandEventListener(CommandEventListener listener) {
		return commandAnnouncer.removeListener( listener );
	}
	
	/**
	 * Remove all the listeners.
	 */
	public void removeAllCommandEventListeners() {
		commandAnnouncer.clear();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if ( e.isPopupTrigger() ) {
			showPopup( e );
		} else if ( 2 == e.getClickCount() ) {
			Object node = ( tree.getSelectionPath() != null )
					? tree.getSelectionPath().getLastPathComponent() : null;
			if ( node != null ) {
				commandAnnouncer.announce().execute( node );
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if ( e.isPopupTrigger() ) {
			showPopup( e );
		}
	}
	
	private void showPopup(MouseEvent e) {
		popupMenu.show( e.getComponent(), e.getX(), e.getY() );	
	}
}