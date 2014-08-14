package org.funtester.common.at;

import java.io.Serializable;
import java.util.Arrays;

import org.funtester.common.util.Copier;

/**
 * Abstract test element
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class AbstractTestElement
	implements Serializable, Copier< AbstractTestElement > {

	private static final long serialVersionUID = -7318641503423642357L;
	
	private String type;
	private String internalName;
	private String name;
	private Object value;					/// Test value generated according to the business rules. Can be null, depending on the element.
	private Boolean valueConsideredValid;	/// If the value value is considered valid. Can be null if value is null.
	private String valueOption;				/// {@code ValidValueOption} or {@code InvalidValueOption} used to generate the value. Can be null if value is null.
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getInternalName() {
		return internalName;
	}
	
	public void setInternalName(String internalName) {
		this.internalName = internalName;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Object getValue() {
		return value;
	}
	
	public void setValue(Object value) {
		this.value = value;
	}
	
	public Boolean getValueConsideredValid() {
		return valueConsideredValid;
	}
	
	public void setValueConsideredValid(Boolean valueConsideredValid) {
		this.valueConsideredValid = valueConsideredValid;
	}

	public String getValueOption() {
		return valueOption;
	}

	public void setValueOption(String valueOption) {
		this.valueOption = valueOption;
	}

	// FROM Copier
	
	public AbstractTestElement copy(final AbstractTestElement that) {
		this.type = that.type;
		this.internalName = that.internalName;
		this.name = that.name;
		this.value = that.value;
		this.valueConsideredValid = that.valueConsideredValid;
		this.valueOption = that.valueOption;
		return this;
	}

	public AbstractTestElement newCopy() {
		return ( new AbstractTestElement() ).copy( this );
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode( new Object[] {
			type, internalName, name, value, valueConsideredValid, valueOption
		} );
	}
	
	@Override
	public boolean equals(Object o) {
		if ( ! ( o instanceof AbstractTestElement ) ) {
			return false;
		}
		AbstractTestElement that = (AbstractTestElement) o;
		
		return eq( this.type,					that.type )
			&& eq( this.internalName,			that.internalName )
			&& eq( this.name,					that.name )
			&& eq( this.value,					that.value )
			&& eq( this.valueConsideredValid,	that.valueConsideredValid )
			&& eq( this.valueOption, 			that.valueOption )
			;		
	}
	
	@Override
	public String toString() {
		return internalName;
	}
	
	private boolean eq(Object a, Object b) {
		if ( null == a && null == b ) {
			return true;
		}
		if ( a != null && b != null ) {
			a.equals( b );
		}
		return false;
	}
}
