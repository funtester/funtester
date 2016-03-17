package org.funtester.core.software;

import java.util.Arrays;

import org.funtester.common.util.EqUtil;

import com.fasterxml.jackson.annotation.JsonIdentityReference;

/**
 * Value configuration based on a regular expression.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class RegExBasedVC extends ValueConfiguration {

	@JsonIdentityReference(alwaysAsId=true)
	private RegEx regEx = null;
	
	public RegExBasedVC() {
	}
	
	public RegExBasedVC(RegEx regEx) {
		setRegEx( regEx );
	}

	@Override
	public ValueConfigurationKind kind() {
		return ValueConfigurationKind.REGEX_BASED;
	}
	
	public RegEx getRegEx() {
		return regEx;
	}

	public void setRegEx(RegEx regEx) {
		this.regEx = regEx;
	}

	@Override
	public RegExBasedVC copy(final ValueConfiguration obj) {
		super.copy( obj );
		if ( obj instanceof RegExBasedVC ) {
			RegExBasedVC that = (RegExBasedVC) obj;
			this.regEx = that.regEx; // Reference
		}
		return this;
	}

	@Override
	public RegExBasedVC newCopy() {
		return ( new RegExBasedVC() ).copy( this );
	}
	
	@Override
	public String toString() {
		return regEx != null ? "regEx: " + regEx.toString() : "";
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode( new Object[] { regEx } ); 
	}
	
	@Override
	public boolean equals(Object obj) {
		if ( ! ( obj instanceof RegExBasedVC ) ) return false;
		final RegExBasedVC that = (RegExBasedVC) obj;
		return EqUtil.equals( this.regEx, that.regEx );
	}
}
