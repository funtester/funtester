package org.funtester.common.at;

import java.io.Serializable;
import java.util.Arrays;

import org.funtester.common.util.Copier;

/**
 * Base class for other test classes.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class AbstractTest
	implements Serializable, Copier< AbstractTest > {

	private static final long serialVersionUID = 2525749949023909633L;
	
	private String name = "";
	
	public AbstractTest() {
	}
	
	public AbstractTest(String name) {
		this();
		setName( name );
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;		
	}
	
	public AbstractTest copy(final AbstractTest that) {
		this.name = that.name;
		return this;
	}
	public AbstractTest newCopy() {
		return new AbstractTest().copy( this );
	}
	
	@Override
	public String toString() {
		return name;
	}	
	
	@Override
	public int hashCode() {
		return Arrays.hashCode( new Object[] {
			name
		} );
	}	
	
	@Override
	public boolean equals(final Object o) {
		if ( !( o instanceof AbstractTest ) ) {
			return false;
		}
		final AbstractTest that = (AbstractTest) o;
		if ( this.name != null && that.name != null ) {
			return name.equalsIgnoreCase( that.name );
		}
		return false;
	}
}
