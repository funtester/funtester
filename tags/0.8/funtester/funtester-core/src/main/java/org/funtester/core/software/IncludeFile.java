package org.funtester.core.software;

import java.util.Arrays;

import org.funtester.common.util.Copier;
import org.funtester.common.util.EqUtil;

/**
 * Include file
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class IncludeFile implements Copier< IncludeFile >{

	private String name = "";
	
	public IncludeFile() {
	}
	
	public IncludeFile(final String name) {
		setName( name );
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public IncludeFile copy(final IncludeFile that) {
		this.name = that.name;
		return this;
	}

	@Override
	public IncludeFile newCopy() {
		return ( new IncludeFile() ).copy( this );
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode( new Object[] { name }  );
	}
	
	@Override
	public boolean equals(Object o) {
		if ( ! ( o instanceof IncludeFile ) ) return false;
		final IncludeFile that = (IncludeFile) o;
		return EqUtil.equals( this.name, that.name );
	}
}
