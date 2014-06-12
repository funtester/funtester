package org.funtester.core.software;

import java.io.Serializable;
import java.util.Arrays;

import org.funtester.common.util.Copier;
import org.funtester.common.util.EqUtil;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators.PropertyGenerator;

/**
 * Regular expression used in some business rules.
 * 
 * @author Thiago Delgado Pinto
 *
 */
@JsonIdentityInfo(generator=PropertyGenerator.class, property="id", scope=RegEx.class)
public class RegEx implements Serializable, Copier< RegEx >{

	private static final long serialVersionUID = 3141472418544801563L;
	
	private long id = 0;
	private String name = "";
	private String expression = "";
	
	public RegEx() {		
	}
	
	public RegEx(String name, String expression) {
		this();
		setName( name );
		setExpression( expression );
	}
	
	public RegEx(RegEx that) {
		this.name = that.name;
		this.expression = that.expression;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(final long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getExpression() {
		return expression;
	}
	
	public void setExpression(String expression) {
		this.expression = expression;
	}
	
	@Override
	public RegEx copy(final RegEx that) {
		this.id = that.id;
		this.name = that.name;
		this.expression = that.expression;
		return this;
	}

	@Override
	public RegEx newCopy() {
		return ( new RegEx() ).copy( this );
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode( new Object[] {
			id, name, expression
		} );
	}
	
	@Override
	public boolean equals(Object o) {
		if ( ! ( o instanceof RegEx ) ) return false;
		RegEx that = (RegEx) o;
		return // Do not compare the ids
			EqUtil.equalsIgnoreCase( this.name, that.name )
			&& EqUtil.equalsIgnoreCase( this.expression, that.expression )
			;
	}
}
