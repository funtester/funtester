package org.funtester.core.software;

import java.util.Arrays;

import org.funtester.common.util.EqUtil;

/**
 * Value configuration that allows to define a single (manual) value.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class SingleVC extends ValueConfiguration {

	private Object value = null;
	
	public SingleVC() {
	}
	
	public SingleVC(Object value) {
		setValue( value );
	}
	
	@Override
	public ValueConfigurationKind kind() {
		return ValueConfigurationKind.SINGLE;
	}
	
	public Object getValue() {
		return value;
	}
	
	public void setValue(Object value) {
		this.value = value;
	}
	
	public boolean isCompatibleWith(BusinessRuleType type) {
		return BusinessRuleType.MIN_VALUE.equals( type ) 
			|| BusinessRuleType.MAX_VALUE.equals( type )
			|| BusinessRuleType.MIN_LENGTH.equals( type )
			|| BusinessRuleType.MAX_LENGTH.equals( type )
			;
	}

	@Override
	public SingleVC copy(final ValueConfiguration obj) {
		super.copy( obj );
		if ( obj instanceof SingleVC ) {
			SingleVC that = (SingleVC) obj;
			this.value = that.value; // Try to copy the value ! (String, Integer, Double, etc.)
		}
		return this;
	}

	@Override
	public SingleVC newCopy() {
		return ( new SingleVC() ).copy( this );
	}
	
	@Override
	public String toString() {
		return value != null ? "value: " + value.toString() : "";
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode( new Object[] { value } );
	}
	
	@Override
	public boolean equals(Object obj) {
		if ( ! ( obj instanceof SingleVC ) ) return false;
		final SingleVC that = (SingleVC) obj;
		return EqUtil.equals( this.value, that.value );
	}
}
