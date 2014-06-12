package org.funtester.app.ui.util;

import org.funtester.common.util.Announcer;
import org.funtester.core.util.UpdateListener;

/**
 * Selection keeper
 * 
 * @author Thiago Delgado Pinto
 *
 * @param <T> the selected object type.
 */
public class SelectionKeeper < T > {
	
	private T selectedObject;
	private final Announcer< UpdateListener > announcer =
			Announcer.to( UpdateListener.class );

	public SelectionKeeper() {
	}
	
	public T getSelectedObject() {
		return selectedObject;
	}
	
	protected void setSelectedObject(T obj) {
		this.selectedObject = obj;
	}
	
	public boolean isSelectionEmpty() {
		return null == selectedObject;
	}

	public boolean addListener(UpdateListener l) {
		return getAnnouncer().addListener( l );
	}
	
	public boolean removeListener(UpdateListener l) {
		return getAnnouncer().removeListener( l );
	}	
	
	public void clearListeners() {
		getAnnouncer().clear();
	}
	
	protected Announcer< UpdateListener > getAnnouncer() {
		return announcer;
	}
}
