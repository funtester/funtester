package org.funtester.core.software;

import java.util.Arrays;

import org.funtester.common.util.Copier;
import org.funtester.common.util.EqUtil;

/**
 * Query parameter configuration.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class ParameterConfig implements Copier< ParameterConfig > {
	
	private ValueConfiguration valueConfiguration = null;
	private ValueType valueType = ValueType.STRING;	
	
	public ParameterConfig() {
	}
	
	public ParameterConfig(ValueConfiguration vc, ValueType valueType) {
		this.valueConfiguration = vc;
		this.valueType = valueType;
	}
	
	public ValueConfiguration getValueConfiguration() {
		return valueConfiguration;
	}
	
	public void setValueConfiguration(ValueConfiguration valueConfiguration) {
		this.valueConfiguration = valueConfiguration;
	}
	
	public ValueType getValueType() {
		return valueType;
	}
	
	public void setValueType(ValueType valueType) {
		this.valueType = valueType;
	}
	
	@Override
	public ParameterConfig copy(final ParameterConfig that) {
		
//		if ( ! CopierUtil.copy( that.valueConfiguration, this.valueConfiguration ) ) {
//			this.valueConfiguration = null;
//		}
//		this.valueConfiguration = that.valueConfiguration; // reference

		if ( this.valueConfiguration != null && that.valueConfiguration != null
				&& this.valueConfiguration.kind().equals( that.valueConfiguration.kind() ) ) {
			this.valueConfiguration.copy( that.valueConfiguration );
		} else {
			this.valueConfiguration = null;
			if ( that.valueConfiguration != null ) {
				this.valueConfiguration = that.valueConfiguration.newCopy();
			}
		}
		
		this.valueType = that.valueType;
		return this;
	}

	@Override
	public ParameterConfig newCopy() {
		return ( new ParameterConfig() ).copy( this );
	}
	
	@Override
	public String toString() {
		return ( new StringBuffer() )
			.append( " valueConfiguration: "  )
			.append( ( valueConfiguration != null ) ? valueConfiguration.toString() : "null" )
			.append( " valueType: " )
			.append( valueType )
			.toString();
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode( new Object[] { valueConfiguration, valueType } );
	}
	
	@Override
	public boolean equals(Object obj) {
		if ( ! ( obj instanceof ParameterConfig ) ) { return false; }
		final ParameterConfig that = (ParameterConfig) obj;
		return
			EqUtil.equals( this.valueConfiguration, that.valueConfiguration )
			&& EqUtil.equals( this.valueType, that.valueType )
			;
	}
}
