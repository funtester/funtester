package org.funtester.core.software;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.funtester.common.Importance;
import org.funtester.common.util.Copier;
import org.funtester.common.util.CopierUtil;
import org.funtester.common.util.EqUtil;
import org.funtester.common.util.Identifiable;
import org.funtester.core.profile.StepKind;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.ObjectIdGenerators.PropertyGenerator;

/**
 * A flow of a use case.
 * 
 * @author Thiago Delgado Pinto
 *
 */
@JsonTypeInfo(use=Id.CLASS, include=As.PROPERTY, property="@class")
@JsonIdentityInfo(generator=PropertyGenerator.class, property="id", scope=Flow.class)
public class Flow
	implements Identifiable, Serializable, Copier< Flow > {

	private static final long serialVersionUID = 8201204103776374473L;

	private long id = 0;
	//@JsonBackReference
	@JsonIgnore
	private UseCase useCase; // owner use case
	private Priority priority = Priority.SHOULD;
	private Complexity complexity = Complexity.MEDIUM;
	private Frequency frequency = Frequency.MEDIUM;
	private Importance importance = Importance.MEDIUM;
	// @JsonManagedReference
	private List< Step > steps = new ArrayList< Step >();
	// @JsonManagedReference
	private List< Postcondition > postconditions = new ArrayList< Postcondition >();

		
	public Flow() {
	}

	/**
	 * Create the flow for a certain use case. 
	 * 
	 * @param useCase	the owner use case.
	 */
	public Flow(UseCase useCase) {
		this.useCase = useCase;
	}
	
	/**
	 * Return the flow type. Should be overridden in child classes.
	 * @return
	 */
	public FlowType type() {
		return FlowType.FLOW;
	}
	
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	
	public UseCase getUseCase() {
		return useCase;
	}
	
	public void setUseCase(UseCase useCase) {
		this.useCase = useCase;
	}	
	
	/**
	 * Returns a short name for the flow. This name can be followed by a number.
	 * <p>It is recommended to override this method in child classes.</p>
	 * 
	 * @return
	 */
	public String shortName() {
		return "F";
	}

	public void setSteps(List< Step > steps) {
		this.steps = steps;
		pointStepsToMe( this.steps ); // Guarantee in deserialization
	}
	
	public List< Step > getSteps() {
		return (List< Step >) steps;
	}

	public boolean addStep(Step step) {
		return steps.add( step );
	}
	
	public boolean removeStepAt(int index) {
		return ( steps.remove( stepAt( index ) ) );
	}
	
	/**
	 * Clone the step in a certain index, add it to the flow and returns the
	 * new step index (the original index plus one).
	 * 
	 * @param index	the index of the step to be cloned.
	 * @return		the index of the cloned step in the flow.
	 */
	public int cloneStepAt(final int index) {
		Step current = stepAt( index );
		Step newStep = current.newCopy();
		final int newIndex = index + 1;
		getSteps().add( newIndex, newStep );
		return newIndex;
	}
	
	/**
	 * Move a step up in the list.
	 * 
	 * @param index	the index of the step to be moved.
	 * @return		the new index.
	 */
	public int moveStepUp(final int index) {
		if ( index < 1 ) {
			return index;
		}
		final int newIndex = index - 1;
		Collections.swap( getSteps(), index, newIndex );
		return newIndex;
	}
		
	/**
	 * Move a step down in the list.
	 * 
	 * @param index	the index of the step to be moved.
	 * @return		the new index.
	 */
	public int moveStepDown(final int index) {
		if ( index >= ( numberOfSteps() - 1 ) ) {
			return index;	
		}
		final int newIndex = index + 1;
		Collections.swap( getSteps(), index, newIndex );
		return newIndex;
	}	

	/**
	 * Return the step in a certain index.
	 * 
	 * @param index	the index of the step.
	 * @return		the step or null if not found.
	 */
	public Step stepAt(final int index) {
		return getSteps().get( index );
	}
	
	/**
	 * Return the index of a certain step.
	 * 
	 * @param step	the step to find.
	 * @return		the index or -1 if not found.
	 */
	public int indexOfStep(Step step) {
		return getSteps().indexOf( step );
	}	

	/**
	 * Return the number of steps in the flow.
	 * @return	the number of steps in the flow.
	 */
	public int numberOfSteps() {	
		return steps.size();
	}

	public void setPriority(Priority priority) {
		this.priority  = priority;		
	}

	public Priority getPriority() {
		return priority;
	}

	public void setComplexity(Complexity complexity) {
		this.complexity = complexity;
	}

	public Complexity getComplexity() {
		return complexity;
	}

	public void setFrequency(Frequency frequency) {
		this.frequency = frequency;		
	}

	public Frequency getFrequency() {
		return this.frequency;
	}
		
	public Importance getImportance() {
		return importance;
	}

	public void setImportance(Importance importance) {
		this.importance = importance;
	}


	public List< Postcondition > getPostconditions() {
		return postconditions;
	}
	
	public void setPostconditions(List< Postcondition > postconditions) {
		this.postconditions = postconditions;
		pointPostconditionsToMe( this.postconditions ); // Guarantee deserialization
	}
	

	/**
	 * Add a postcondition.
	 * 
	 * @param state	the postcondition to add.
	 * @return		true if added, false otherwise.
	 */
	public boolean addPostcondition(Postcondition state) {
		return postconditions.add( state );
	}
	
	/**
	 * Return the number of postconditions. 
	 * @return	the number of postconditions.
	 */
	public int numberOfPostconditions() {
		return postconditions.size();
	}
	
	/**
	 * Remove a postcondition.
	 * 
	 * @param state	the postcondition to remove.
	 * @return		true if removed, false otherwise.
	 */
	public boolean removePostcondition(Postcondition state) {
		return postconditions.remove( state );
	}

	/**
	 * Return a postcondition in a certain index.
	 * 
	 * @param index	the index of the desired postcondition.
	 * @return		the postcondition.
	 */
	public Postcondition postconditionAt(int index) {
		return postconditions.get( index );
	}

	/**
	 * Return the index of a postcondition.
	 * 
	 * @param state	the postcondition to find.
	 * @return		the index or -1 if not found.
	 */
	public int indexOfPostcondition(Postcondition state) {
		return postconditions.indexOf( state );
	}
	
	/**
	 * Return the use case flows.
	 *  
	 * @return	a list of flows.
	 */
	public List< Flow > useCaseFlows() {
		if ( null == getUseCase() ) {
			return new ArrayList< Flow >();
		}
		return getUseCase().getFlows();
	}
	
	/**
	 * Return the dependencies of the flow's use case.
	 * @return	a set of use cases. 
	 */
	public Set< UseCase > useCaseDependencies() {
		Set< UseCase > useCases = new LinkedHashSet< UseCase >();
		List< Step > steps = getSteps();
		for ( Step s : steps ) {
			if ( ! s.kind().equals( StepKind.USE_CASE_CALL ) ) {
				continue;
			}
			UseCaseCallStep uccs = (UseCaseCallStep) s;
			useCases.add( uccs.getUseCase() ); // Add the use case (if not already exists)
		}
		return useCases;
	}

	@Override
	public Flow copy(final Flow that) {
		
		this.id = that.id;
		this.useCase = that.useCase; // Reference
		this.priority = that.priority;
		this.complexity = that.complexity;
		this.frequency = that.frequency;
		this.importance = that.importance;
			
		CopierUtil.copyCollection( that.steps, this.steps );
		pointStepsToMe( this.steps );

		CopierUtil.copyCollection( that.postconditions, this.postconditions );
		pointPostconditionsToMe( this.postconditions );

		return this;
	}
	
	@Override
	public Flow newCopy() {
		return ( new Flow() ).copy( this );
	}
	
	@Override
	public String toString() {
		return shortName();
	}
	
	@Override 
	public int hashCode() {
		return Arrays.hashCode( new Object[] {
			id, priority, complexity, frequency, importance, steps, postconditions
		} );
	}
	
	@Override
	public boolean equals(Object obj) {
		if ( ! ( obj instanceof Flow ) ) return false;
		final Flow that = (Flow) obj;
		return // Do not compare ids, priorities, complexities, and frequencies
			EqUtil.equalsAdresses( this.useCase, that.useCase ) // Compare address to avoid recursion
			&& this.type().equals( that.type() )
			&& EqUtil.equals( this.steps, that.steps )
			&& EqUtil.equals( this.postconditions, that.postconditions )
			;
	}
	
	protected void pointStepsToMe(List< Step > list) {
		for ( Step s : list ) {
			s.setFlow( this );
		}
	}
	
	private void pointPostconditionsToMe(List< Postcondition > list) {
		for ( Postcondition p : list ) {
			p.setOwnerFlow( this );
		}
	}
}
