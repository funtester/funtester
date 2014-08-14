package org.funtester.core.software;

import java.io.Serializable;
import java.util.Arrays;

import org.funtester.common.util.Copier;
import org.funtester.common.util.EqUtil;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators.PropertyGenerator;

/**
 * Something (a person, system, hardware, etc.) that interacts with the system. 
 * 
 * @author Thiago Delgado Pinto
 *
 */
@JsonIdentityInfo(generator=PropertyGenerator.class, property="id", scope=Actor.class)
public class Actor
	implements Serializable, Copier< Actor > {

	private static final long serialVersionUID = -4568909988321010023L;
	
	private long id = 0;
	private String name = ""; 
	private String description = "";
	
	public Actor() {
	}
	
	public Actor(final String name, final String description) {	
		this();
		setName( name );
		setDescription( description );
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}   
	
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public Actor copy(final Actor that) {
		this.id = that.id;
		this.name = that.name;
		this.description = that.description;
		return this;
	}

	@Override
	public Actor newCopy() {
		return ( new Actor() ).copy( this );
	}

	@Override
	public String toString() {
		return getName();
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode( new Object[] {
			id, name, description
		} );
	}	
	
	@Override
	public boolean equals(Object obj) {
		if ( ! ( obj instanceof Actor ) ) return false;
		Actor that = (Actor) obj;
		return // Id nor description are compared
			EqUtil.equalsIgnoreCase( this.name, that.name );
	}
}
