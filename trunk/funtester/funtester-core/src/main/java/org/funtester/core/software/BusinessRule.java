package org.funtester.core.software;

import java.io.Serializable;
import java.util.Arrays;

import org.funtester.common.Importance;
import org.funtester.common.util.Copier;
import org.funtester.common.util.EqUtil;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators.PropertyGenerator;

/**
 * Business Rule for a step element. Allows to define a value restriction that
 * can be used for generating tests.
 * 
 * @author Thiago Delgado Pinto
 *
 */
@JsonIdentityInfo(generator=PropertyGenerator.class, property="id", scope=BusinessRule.class)
public final class BusinessRule
	implements Serializable, Copier< BusinessRule > {

	private static final long serialVersionUID = 6059967433041142899L;

	private long id = 0;
	private BusinessRuleType type = BusinessRuleType.REQUIRED;
	private Importance importance = Importance.MEDIUM;
	private String message = "";
	private ValueConfiguration valueConfiguration = null; // 0..1
	
	
	public BusinessRule() {
	}
	
	public BusinessRule(
			BusinessRuleType type,
			String message,
			ValueConfiguration valueConfiguration
			) {
		setType( type );
		setMessage( message );
		setValueConfiguration( valueConfiguration );
	}
	
	public BusinessRule(BusinessRuleType type, String message) {
		setType( type );
		setMessage( message );
	}
	
	public BusinessRule(
			BusinessRuleType type,
			ValueConfiguration valueConfiguration
			) {
		setType( type );
		setValueConfiguration( valueConfiguration );
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public BusinessRuleType getType() {
		return type;
	}
	
	public void setType(BusinessRuleType type) {
		this.type = type;
	}
	
	public Importance getImportance() {
		return importance;
	}

	public void setImportance(Importance importance) {
		this.importance = importance;
	}

	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public ValueConfiguration getValueConfiguration() {
		return valueConfiguration;
	}
	
	public void setValueConfiguration(ValueConfiguration valueConfiguration) {
		this.valueConfiguration = valueConfiguration;
	}

	@Override
	public BusinessRule copy(final BusinessRule that) {
		this.id = that.id;
		this.type = that.type;
		this.importance = that.importance;
		this.message = that.message;
		
		if ( this.valueConfiguration != null
			&& that.valueConfiguration != null
			&& this.valueConfiguration.kind().equals( that.valueConfiguration.kind() )
			) {
			this.valueConfiguration.copy( that.valueConfiguration );
		} else {
			this.valueConfiguration = null;
			if ( that.valueConfiguration != null ) {
				this.valueConfiguration = that.valueConfiguration.newCopy();
			}
		}
		return this;
	}

	@Override
	public BusinessRule newCopy() {
		return ( new BusinessRule() ).copy( this );
	}
	
	@Override
	public boolean equals(Object obj) {
		if ( ! ( obj instanceof BusinessRule ) ) return false;
		BusinessRule that = (BusinessRule) obj;
		return // id is not compared
			EqUtil.equals( this.type, that.type )
			&& EqUtil.equalsIgnoreCase( this.message, that.message )
			&& EqUtil.equals( this.valueConfiguration, that.valueConfiguration )
			;
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode( new Object[] {
			id, type, importance, message, valueConfiguration
		} );
	}
	
	@Override
	public String toString() {
		final String fmt = "\"%s\" : \"%s\", ";
		return ( new StringBuilder() )
				.append( "{ ")
				.append( String.format( fmt, "id", id ) )
				.append( String.format( fmt, "type", type.toString() ) )
				.append( String.format( fmt, "importance", importance.toString() ) )
				.append( String.format( fmt, "message", ( message != null ? message : "null" ) ) )
				.append( String.format( fmt, "valueConfiguration", ( valueConfiguration != null ? valueConfiguration.toString() : "null" ) ) )
				.append( " }")
				.toString()
				;
	}
}
