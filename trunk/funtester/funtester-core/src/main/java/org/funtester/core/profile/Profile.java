package org.funtester.core.profile;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import org.funtester.common.util.Copier;
import org.funtester.common.util.CopierUtil;
import org.funtester.common.util.EqUtil;

/**
 * A set of configurations used by the software to better describe the use
 * case's documentation.    
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class Profile
	implements Serializable, Copier< Profile > {

	private static final long serialVersionUID = -1780131872616061570L;

	private long id = 0;
	private String name = "";
	private String description = "";
	private Set< ElementType > types = new LinkedHashSet< ElementType >();
	private Set< Action > actions = new LinkedHashSet< Action >();
	
	
	public Profile() {
	}
	
	public Profile(String name) {
		this();
		setName( name );
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set< ElementType > getTypes() {
		return types;
	}

	public void setTypes(Set< ElementType > types) {
		this.types = types;
	}
	
	/**
	 * Add a element type to the profile.
	 * 
	 * @param type	the element type to be added.
	 * @return		true if added, false otherwise.
	 */
	public boolean addType(ElementType type) {
		return types.add( type );
	}
	
	/**
	 * Return a {@code ElementType} with a given id.
	 * 
	 * @param anId	the id of the element to find.
	 * @return		the element or {@code null} if not found.
	 */
	public ElementType typeWithId(long anId) {
		for ( ElementType et : types ) {
			if ( anId == et.getId() ) return et;
		}
		return null;
	}	

	public Set< Action > getActions() {
		return actions;
	}

	public void setActions(Set< Action > actions) {
		this.actions = actions;
	}
	
	/**
	 * Add a action to the profile.
	 * 
	 * @param action	the action to be added.
	 * @return			true if added, false otherwise.
	 */
	public boolean addAction(Action action) {
		return actions.add( action );		
	}
	
	/**
	 * Return an action with the given id.
	 * 
	 * @param id	the id of the action to find.
	 * @return		the action or {@code null} if not found.
	 */
	public Action actionWithId(final long id) {
		for ( Action a : actions ) {
			if ( id == a.getId() ) return a; 
		}
		return null;
	}	

	@Override
	public Profile copy(final Profile that) {
		this.id = that.id; 
		this.name = that.name;
		this.description = that.description;
		CopierUtil.copyCollection( that.types, types );
		CopierUtil.copyCollection( that.actions, actions );
		return this;
	}

	@Override
	public Profile newCopy() {
		return ( new Profile() ).copy( this );
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode( new Object[] {
			id, name, description, types, actions
		} );
	}
	
	@Override
	public boolean equals(Object obj) {
		if ( ! ( obj instanceof Profile ) ) return false;
		final Profile that = (Profile) obj;
		return // It is not necessary to compare the ids nor the descriptions
			EqUtil.equalsIgnoreCase( this.name, that.name )
			|| ( EqUtil.equals( this.types, that.types )
				&& EqUtil.equals( this.actions, that.actions ) )
			;
	}
}
