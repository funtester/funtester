package org.funtester.core.software;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.funtester.common.Importance;
import org.funtester.common.util.Copier;
import org.funtester.common.util.EqUtil;
import org.funtester.common.util.Identifiable;
import org.funtester.common.util.ListUtil;
import org.funtester.core.profile.StepKind;

/**
 * A path in the software execution, composed by one or more flows of one or
 * more use cases.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class Scenario
	implements
		Identifiable,
		Serializable,
		Copier< Scenario >,
		Comparable< Scenario > {
	
	private static final long serialVersionUID = -5243976705622118863L;

	/** Identification */
	private long id = 0;

	/** The name is usually a combination of short names of the flows that composes the scenario */
	private String name = "";
	
	/** Target use case */ 
	private UseCase useCase = null;
	
	/** Step from one or more flows */
	private List< Step > steps = new ArrayList< Step >();
	
	/** Files need to the test scenario work properly */
	private Set< IncludeFile > includeFiles = new LinkedHashSet< IncludeFile >();
	
	
	public Scenario() {
	}

	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public UseCase getUseCase() {
		return useCase;
	}

	public void setUseCase(UseCase useCase) {
		this.useCase = useCase;
	}
	
	public String longName() {		
		return (( useCase != null ) ? useCase.getName() + " {" : "{" )
			+ getName() + "}"; // For example: Some Use Case {BF, AF1}  
	}	

	public List< Step > getSteps() {
		return steps;
	}

	public void setSteps(List< Step > steps) {
		this.steps = steps;
	}
	
	/**
	 * Adds a list of steps to the scenario.
	 * 
	 * @param stepsToAdd	the steps to add.
	 * @return				true if added, false otherwise.
	 */
	public boolean addAllSteps(List< Step > stepsToAdd) {
		return steps.addAll( stepsToAdd );
	}
	
	/**
	 * Adds a list of steps to the scenario in a certain index.
	 * 
	 * @param index			the index where is to be added.
	 * @param stepsToAdd	the steps to add.
	 */
	public void addAllSteps(int index, List< Step > stepsToAdd) {
		steps.addAll( index, stepsToAdd );		
	}	
	
	/**
	 * Adds one step to the scenario.
	 * 
	 * @param step	the step to be added.
	 * @return		true if added, false otherwise.
	 */
	public boolean addStep(Step step) {
		return steps.add( step );
	}
	
	/**
	 * Return the number of steps.
	 * 
	 * @return the number of steps.
	 */
	public int numberOfSteps() {
		return steps.size();
	}
	
	/**
	 * Returns how many times a step appears in the scenario.
	 * 
	 * @param step	the step to verify.
	 * @return		how many times a step appears in the scenario.
	 */
	public int countForStep(Step step) {
		int count = 0;
		for ( Step s : steps ) {
			if ( s == step ) { // compare address
				++count;
			}
		}
		return count;
	}
	
	/**
	 * Replaces a step with a list of steps.
	 * 
	 * @param step	Target step
	 * @param list	List of steps
	 * @return		true in case of success. false if can't remove or add.
	 * @throws Exception
	 */
	public boolean replaceStepWithAStepList(Step step, List< Step > list) {
		return ListUtil.replace( steps, step, list );
	}
	

	
	public Set< IncludeFile > getIncludeFiles() {
		return includeFiles;
	}

	public void setIncludeFiles(Set< IncludeFile > includeFiles) {
		this.includeFiles = includeFiles;
	}
	
	// FLOWS

	/**
	 * Returns the flows involved in the scenario.
	 * @return A set of flows.
	 */
	public Set< Flow > flowsInvolved() {
		Set< Flow > flows = new LinkedHashSet< Flow >();
		for ( Step s : steps ) {
			flows.add( s.getFlow() );
		}
		return flows;
	}
	
	/**
	 * Return true if the flows involved contains the supplied flow. 
	 * @param flow	Flow to check.
	 * @return		true if the flow exists, false otherwise.
	 */
	public boolean containsFlow(Flow flow) {
		return flowsInvolved().contains( flow );
	}
	
	/**
	 * Returns true if the scenario could have a successful end.
	 * This is useful to filter preconditions' scenarios. 
	 * @return
	 */
	public boolean couldHaveASuccessfulEnd() {
		if ( steps.isEmpty() ) {
			return false;
		}
		Step lastStep = steps.get( steps.size() - 1 );
		FlowType flowType = lastStep.getFlow().type();
		return FlowType.FLOW == flowType
			|| FlowType.TERMINATOR == flowType
			|| FlowType.BASIC == flowType;
	}
	
	/**
	 * Returns true if the scenario calls any use case.
	 * This is useful to filter scenarios to combine with other scenarios.
	 * 
	 * @return true if the scenario calls any use case, false otherwise.
	 */
	public boolean callAnyUseCase() {
		return ! calledUseCases().isEmpty();
	}
	
	/**
	 * Returns the called use cases.
	 * This is useful to filter scenarios to combine with other scenarios.
	 * 
	 * @return the called use cases.
	 */
	public Set< UseCase > calledUseCases() {
		Set< UseCase > useCases = new LinkedHashSet< UseCase >();
		for ( Step step : steps ) {
			if ( ! step.kind().equals( StepKind.USE_CASE_CALL ) ) {
				continue;
			}
			UseCaseCallStep uccs = (UseCaseCallStep) step;
			useCases.add( uccs.getUseCase() );
		}
		return useCases;
	}
	
	
	// IMPORTANCE
	
	/**
	 * The scenario importance is the average importance for the flows of
	 * the target use case.
	 * 
	 * @return an {@link Importance} value.
	 */	
	public Importance importance() {
		
		Collection< Importance > values = new ArrayList< Importance >();
		Set< Flow > allFlows = flowsInvolved();
		for ( Flow flow : allFlows ) {
			// Just the flows of the target use case
			if ( flow.getUseCase().equals( getUseCase() ) ) {
				values.add( flow.getImportance() );
			}
		}
		return ImportanceCalculator.averageImportance( values );
	}
		
	// OTHER
	
	public int compareTo(final Scenario that) {
		final int thisImportance = ImportanceCalculator.ordinalOf( importance() );
		final int thatImportance = ImportanceCalculator.ordinalOf( that.importance() );
		return ( thisImportance > thatImportance ) ? 1
				: ( thisImportance < thatImportance ) ? -1 : 0;
	}
	
	// BUILDER METHODS
	
	public Scenario withId(long id) {
		setId( id );
		return this;
	}	

	public Scenario withName(String name) {
		setName( name );
		return this;
	}
	
	public Scenario withUseCase(UseCase obj) {
		setUseCase( obj );
		return this;
	}
	
	public Scenario addSteps(List< Step > steps) {
		getSteps().addAll( steps );
		return this;
	}
	
	public Scenario addIncludeFiles(Set< IncludeFile > includeFiles) {
		getIncludeFiles().addAll( includeFiles );
		return this;
	}
	
	// FROM Copier

	@Override
	public Scenario copy(final Scenario that) {
		this.id = that.id;
		this.name = that.name;
		this.useCase = that.useCase; // Reference

		this.steps.clear();
		this.steps.addAll( that.steps ); // Copy references !
		
		this.includeFiles.clear();
		this.includeFiles.addAll( that.includeFiles ); // Copy references !
		
		return this;
	}

	@Override
	public Scenario newCopy() {
		return ( new Scenario() ).copy( this );
	}
	
	// FROM Object

	@Override
	public String toString() {
		return getName(); //String.valueOf( id );
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode( new Object[] {
			id, name, useCase, steps, includeFiles
		} );
	}
	
	@Override
	public boolean equals(Object obj) {
		if ( ! ( obj instanceof Scenario ) ) {
			return false;
		}
		final Scenario that = (Scenario) obj;
		return // Not necessary to compare ids nor steps
			EqUtil.equals( this.name, that.name )
			&& EqUtil.equals( this.useCase, that.useCase )
			;
	}
}
