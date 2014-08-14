package org.funtester.core.software;

import org.funtester.common.util.Copier;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.ObjectIdGenerators.PropertyGenerator;

/**
 * Use to define a value configuration for a business rule.
 * 
 * @author Thiago Delgado Pinto
 *
 */
@JsonTypeInfo(use=Id.CLASS, include=As.PROPERTY, property="@class")
@JsonIdentityInfo(generator=PropertyGenerator.class, property="id", scope=ValueConfiguration.class)
public abstract class ValueConfiguration implements Copier< ValueConfiguration > {
	
	private long id = 0;
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	@JsonIgnore
	public abstract ValueConfigurationKind kind();
	
	@Override
	public ValueConfiguration copy(final ValueConfiguration that) {
		this.id = that.id;
		return this;
	}
	
}
