package org.funtester.core.software;

import java.io.Serializable;
import java.util.Arrays;

import org.funtester.common.util.Copier;
import org.funtester.common.util.EqUtil;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.ObjectIdGenerators.PropertyGenerator;

/**
 * Represents a condition state, that can be a Precondition or a Postcondition.
 * 
 * @author Thiago Delgado Pinto
 *
 */
@JsonTypeInfo(use=Id.CLASS, include=As.PROPERTY, property="@class")
@JsonIdentityInfo(generator=PropertyGenerator.class, property="id", scope=ConditionState.class)
public abstract class ConditionState
	implements Serializable, Copier< ConditionState > {

	private static final long serialVersionUID = -452136486363034295L;

	private long id = 0;
	private String description = "";	
	
	public ConditionState() {
	}
	
	public ConditionState(String description) {
		setDescription( description );
	}
	
	public long getId() {
		return id;
	}

	public void setId(final long id) {
		this.id = id;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Returns the kind of the condition state.
	 * 
	 * @return the kind of the condition state.
	 */
	public abstract ConditionStateKind kind();

	@Override
	public ConditionState copy(final ConditionState that) {
		this.id = that.id;
		this.description = that.description;
		return this;
	}

	public abstract ConditionState newCopy();
	
	@Override
	public String toString() {
		return description;
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode( new Object[] {
			id, description, kind()
		} );
	}
	
	@Override
	public boolean equals(Object o) {		
		if ( ! ( o instanceof ConditionState ) ) return false;
		ConditionState that = (ConditionState) o;
		return // Not necessary to compare the ids
			EqUtil.equals( this.kind(), that.kind() )
			&& EqUtil.equalsIgnoreCase( this.description, that.description )
			;
	}
}