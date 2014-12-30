package org.funtester.core.software;

import java.io.Serializable;
import java.util.Arrays;

import org.funtester.common.util.Copier;
import org.funtester.common.util.EqUtil;
import org.funtester.common.util.Identifiable;
import org.funtester.core.profile.StepKind;
import org.funtester.core.profile.Trigger;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.ObjectIdGenerators.PropertyGenerator;

/**
 * Step of a use case's flow.
 * 
 * @author Thiago Delgado Pinto
 *
 */
// Include Java class name ("org.funtester.core.software.Step") as JSON property "class"
// Child classes will also use this feature, so that the right type can be recognized.
@JsonTypeInfo(use=Id.CLASS, include=As.PROPERTY, property="@class")
@JsonIdentityInfo(generator=PropertyGenerator.class, property="id", scope=Step.class)
public abstract class Step
	implements Identifiable, Serializable, Copier< Step > {
	
	private static final long serialVersionUID = -7240052159558879047L;
	
	private long id = 0;	
	//@JsonBackReference
	@JsonIgnore
	private Flow flow = null; // owner flow
	
	
	public Step() {
	}
	
	public Step(Flow flow) {
		this();
		setFlow( flow );
	}
	
	/**
	 * Returns the index of the step in the owner flow or -1 if the flow is null
	 * or the step is not found. 
	 * 
	 * @return
	 */
	public int index() {
		if ( null == getFlow() ) return -1;
		return getFlow().indexOfStep( this );
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public Flow getFlow() {
		return flow;
	}
	
	public void setFlow(Flow flow) {
		this.flow = flow;
	}
	
	/**
	 * Return the step kind.
	 * @return
	 */
	public abstract StepKind kind();

	/**
	 * Return the step trigger.
	 * @return
	 */
	public abstract Trigger trigger();
			
	/**
	 * Returns the step as a sentence WITHOUT the Trigger.
	 * 
	 * @return	a string containing the sentence.
	 */
	public abstract String asSentence();

	
	/**
	 * Returns <code>true</code> if the step is performable.
	 * 
	 * @return	true if the step is performable, false otherwise.
	 */
	@JsonIgnore
	public abstract boolean isPerformable();
	
	public String shortName() {
		return flow.shortName() + "-" + ( flow.indexOfStep( this ) + 1 );
	}
	
	public String triggerName() {
		return trigger().toString();
	}
	
	public UseCase useCase() {
		return getFlow().getUseCase();
	}
	
	@Override
	public Step copy(Step obj) {
		id = obj.id;
		flow = obj.flow; // Reference
		return this;
	}
	
	//
	// newCopy() should be created in child classes.
	//
	
	@Override
	public String toString() {
		return asSentence();
	}
	
	@Override
	public int hashCode() {
		// Cannot use "flow" because it causes recursion
		return Arrays.hashCode( new Object[] { id } );
	}
		
	@Override
	public boolean equals(Object obj) {
		if ( ! ( obj instanceof Step ) ) return false;
		final Step that = (Step) obj;
		return // It is necessary to compare the ids
			EqUtil.equals( this.getId(), that.getId() )
			&& EqUtil.equalsAdresses( this.flow, that.flow ) // Compare with "equals" can cause recursion
			&& EqUtil.equals( this.trigger(), that.trigger() )
			;
	}
}
