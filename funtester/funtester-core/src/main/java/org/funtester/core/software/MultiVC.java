package org.funtester.core.software;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.funtester.common.util.EqUtil;

/**
 * Value configuration that uses multiple values manually defined by the
 * developer.
 *  
 * @author Thiago Delgado Pinto
 *
 */
public class MultiVC extends ValueConfiguration {
	
	private List< Object > values = new ArrayList< Object >();
	
	public MultiVC() {
	}
		
	@Override
	public ValueConfigurationKind kind() {
		return ValueConfigurationKind.MULTI;
	}
	
	public List< Object > getValues() {
		return values;
	}

	public void setValues(List< Object > values) {
		this.values = values;
	}
	
	/**
	 * Add a value to the configuration.
	 * 
	 * @param value	the value to be added.
	 * @return		true if added, false otherwise.
	 */
	public boolean addValue(Object value) {
		return values.add( value );
	}
	
	/**
	 * Add one or more values to the configuration.
	 *  
	 * @param values	the values to be added.
	 */
	public void addValues(Object ...values) {
		for ( Object value : values ) {
			this.values.add( value );
		}
	}
	
	/**
	 * Remove a value by its index.
	 * @param index	the value index.
	 */
	public void remove(int index) {
		values.remove( index );
	}
	
	/**
	 * Set a element at the defined index.
	 * 
	 * @param index	the new index.
	 * @param obj	the object to set.
	 * @return		the object previously at the specified position
	 */
	public Object set(int index, Object obj) {
		return values.set( index, obj );
	}

	@Override
	public MultiVC copy(final ValueConfiguration obj) {
		super.copy( obj );
		if ( obj instanceof MultiVC ) {
			final MultiVC that = (MultiVC) obj;
			// A values is expected to be a basic type (String, Integer, Double, ...)
			this.values.clear();
			this.values.addAll( that.values );
		}
		return this;
	}

	@Override
	public MultiVC newCopy() {
		return ( new MultiVC() ).copy( this );
	}
	
	@Override
	public String toString() {
		return values != null ? "values: " + values.toString() : "";
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode( new Object[] { values } );
	}
	
	@Override
	public boolean equals(Object obj) {
		if ( ! ( obj instanceof MultiVC ) ) return false;
		final MultiVC that = (MultiVC) obj;
		return EqUtil.equals( this.values, that.values );
	}
}
