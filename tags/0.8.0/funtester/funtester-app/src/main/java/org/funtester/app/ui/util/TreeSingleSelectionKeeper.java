package org.funtester.app.ui.util;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Tree single selection keeper 
 * 
 * @author Thiago Delgado Pinto
 *
 * @param <T> the type of the selected object
 */
public class TreeSingleSelectionKeeper< T >
	extends SelectionKeeper< T >
	implements TreeSelectionListener {

	// When the tree is selected, the value is set.
	@SuppressWarnings("unchecked")
	@Override
	public void valueChanged(TreeSelectionEvent e) {
		// If the item has been deleted...
		if ( null == e.getNewLeadSelectionPath() ) {
			return; // ...exit
		}
		// The last component is the selected one
		Object object = e.getPath().getLastPathComponent();
		// If the object is a DefaultMutableTreeNode, extract the real object
		if ( object instanceof DefaultMutableTreeNode ) {
			object = ( (DefaultMutableTreeNode) object ).getUserObject();
		}
		
		T lastSelected = getSelectedObject();
		setSelectedObject( null );
		
		if ( object != null	&& object.getClass().isInstance( object ) ) {
			setSelectedObject( (T) object );
			if ( getSelectedObject() != lastSelected ) { // Has change? (compare address)
				getAnnouncer().announce().updated( object ); // Announce updated
			}
		}
	}

}
