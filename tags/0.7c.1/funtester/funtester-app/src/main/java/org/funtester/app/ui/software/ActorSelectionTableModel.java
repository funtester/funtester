package org.funtester.app.ui.software;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import org.funtester.core.software.Actor;

/**
 * Actor selection table model.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class ActorSelectionTableModel extends DefaultTableModel {
	
	private static final long serialVersionUID = 6375681574497243364L;
	
	//
	// These attributes are lazy loaded because Java's problem on initializing
	// child class attributes after parent's constructor. DefaultTableModel
	// call some virtual methods during construction that are overridden in
	// this class and uses the (still not initialized) attributes causing a
	// NullPointerException.
	//
	private List< Actor > actors;
	private Collection< Actor > selectedActors;

	public ActorSelectionTableModel(final List< Actor > actors) {
		if ( actors == null ) throw new IllegalArgumentException( "actors is null" );
		getActors().addAll( actors );		
	}
	
	protected List< Actor > getActors() {
		if ( null == actors ) {
			actors = new ArrayList< Actor >();
		}
		return actors;
	}
	
	public Collection< Actor > getSelectedActors() {
		if ( null == selectedActors ) {
			selectedActors = new ArrayList< Actor >();
		}
		return selectedActors;
	}
	
	public void setSelectedActors(Collection< Actor > selectedActors) {
		this.selectedActors = selectedActors;
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Actor actor = getActors().get( rowIndex );
		if ( null == actor ) {
			return "";
		}
		return ( 0 == columnIndex )
				? getSelectedActors().contains( actor )
				: actor.getName();
	}
	
	@Override
	public void setValueAt(Object value, int row, int col) {
		if ( col != 0 ) { return; }
		Actor actor = getActors().get( row );
		//System.out.println( "actor is " + actor );
		if ( (Boolean) value ) {
			getSelectedActors().add( actor );
			//System.out.println( "added " + actor + " contains " + getSelectedActors().contains( actor )  );
		} else {
			getSelectedActors().remove( actor );
			//System.out.println( "removed " + actor );
		}
	}
	
	@Override
	public int getRowCount() {
		return getActors().size();
	}
	
	@Override
	public int getColumnCount() {
		return 2;
	}
	
	public Class< ? extends Object > getColumnClass(int c) {
        return getValueAt( 0, c ).getClass();
    }
				
	public boolean isCellEditable(int row, int column) {
		return ( 0 == column );
	}
}
