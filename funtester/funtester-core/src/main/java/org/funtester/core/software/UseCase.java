package org.funtester.core.software;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.funtester.common.util.Copier;
import org.funtester.common.util.CopierUtil;
import org.funtester.common.util.EqUtil;
import org.funtester.common.util.Identifiable;
import org.funtester.common.util.ItemsParser;
import org.funtester.core.profile.StepKind;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Use case
 * 
 * @author Thiago Delgado Pinto
 *
 */
//@JsonIdentityInfo(generator=PropertyGenerator.class, property="id", scope=UseCase.class)
@JsonPropertyOrder({ "id", "name"," description", "elements", "flows", "actors",
	"preconditions", "activationFlows" }) // don't care the others for now
public class UseCase
	implements
		Identifiable
		, Serializable
		, Comparable< UseCase >
		, Copier< UseCase >
		{
	
	private static final long serialVersionUID = 54544180763733996L;
	private static final Logger logger = LoggerFactory.getLogger( UseCase.class );
	
	private long id = 0;
	private boolean ignoreToGenerateTests = false;
	private String name = "";
	private String description = "";
	
	// Elements kept by the use case and that appears in one or more flows.
	// Should appear before the flows because of the deserialization.
	@JsonManagedReference
	private List< Element > elements = new ArrayList< Element >();
	
	// Flows includes basic flows and alternate flows (and it's children).
	//@JsonManagedReference
	private List< Flow > flows = new ArrayList< Flow >();
	
	// Actors that interacts with the use case
	@JsonIdentityReference(alwaysAsId=true)
	private Set< Actor > actors = new LinkedHashSet< Actor >();

	// Precondition is defined as a {@link ConditionState} because it can accept
	// {@code Precondition}s (from the current use case) or {@code Postcondition}s
	// from other use cases.
	@JsonIdentityReference
	private List< ConditionState > preconditions = new ArrayList< ConditionState >();
	
	// Files to be included by the test so it can compile correctly
	private Set< IncludeFile > includeFiles = new LinkedHashSet< IncludeFile >();
	
	// Scripts to be executed by the test case before each test method
	private List< DatabaseScript > databaseScripts = new ArrayList< DatabaseScript >();

	// TODO: Related use cases	
	// TODO: Related artifacts

	public UseCase() {
		super();
	}

	public UseCase(final String name) {
		setName( name );
	}

	public long getId() {
		return this.id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public boolean getIgnoreToGenerateTests() {
		return ignoreToGenerateTests;
	}
	
	public void setIgnoreToGenerateTests(boolean ignore) {
		ignoreToGenerateTests = ignore;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	// ACTORS

	public Set< Actor > getActors() {
		return actors;
	}
	
	public void setActors(Set< Actor > actors) {
		this.actors = actors;
	}

	/**
	 * Get actors names, separated by comma.
	 * 
	 * @return a string containg the actors' names.
	 */
	public String actorsNames() {
		return ItemsParser.textFromItems( ", ", getActors() );	
	}	
	
	/**
	 * Add an actor.
	 * 
	 * @param actor	Actor to be added.
	 * @return		<code>true</code> if successfully added.
	 */
	public boolean addActor(Actor actor) {
		return actors.add(  actor );
	}
	
	/**
	 * Remove an actor.
	 * 
	 * @param actor	Actor to be removed.
	 * @return		<code>true</code> if successfully removed.
	 */
	public boolean removeActor(Actor actor) {
		return actors.remove( actor );
	}
	
	/**
	 * Verifies if the use case contains a actor.
	 * 
	 * @param actor	Actor
	 * @return		<code>true</code> if it contains.
	 */
	public boolean containsActor(Actor actor) {
		return actors.contains( actor );
	}	
	
	// ELEMENTS
		
	/**
	 * Get use case elements
	 * 
	 * @return a collection of elements
	 */
	public List< Element > getElements() {
		return elements;
	}

	public void setElements(List< Element > elements) {
		this.elements = elements;
		pointElementsToMe( this.elements ); // Guarantee deserialization
	}
	
	/**
	 * Add a element.
	 * 
	 * @param element	Element to be added.
	 * @return			<code>true</code> if successfully added.
	 */
	public boolean addElement(Element element) {
		return elements.add( element );
	}
	
	/**
	 * Return an element at a given index.
	 * 
	 * @param index	the element index.
	 * @return		the element or {@code null} if not found.
	 */
	public Element elementAt(final int index) {
		int i = 0;
		for ( Element e : elements ) {
			if ( index == i ) return e;
			++i;
		}
		return null;
	}	
	
	/**
	 * Return an element with a given name.
	 * 
	 * @param name	the name of the element to find
	 * @return		the element or <code>null</code> if not found.
	 */
	public Element elementWithName(final String name) {
		for ( Element ie : elements ) {
			if ( name.equalsIgnoreCase( ie.getName() ) ) {
				return ie;
			}
		}
		return null;
	}

	/**
	 * Return an element with a given id.
	 * 
	 * @param anId	the id to find
	 * @return		the element or <code>null</code> if not found.
	 */	
	public Element elementWithId(final long anId) {
		for ( Element ie : elements ) {
			if ( ie.getId() == anId ) return ie;
		}
		return null;
	}	
	
	// FLOWS

	public List< Flow > getFlows() {
		return flows;
	}
	
	public void setFlows(List< Flow > flows) {
		this.flows = flows;
		pointFlowsToMe( this.flows ); // Guarantee in deserialization
	}
	
	/**
	 * Return a flow at a given index.
	 * 
	 * @param index	Position
	 * @return		the <code>Flow</code> at the position or <code>null</code>
	 * 				if not found
	 */
	public Flow flowAt(int index) {
		return getFlows().get( index ); 
	}
	
	/**
	 * Return the number of flows.
	 * 
	 * @return the number of flows
	 */
	public int numberOfFlows() {	
		return flows.size();
	}	
	
	/**
	 * Return the index of a given <code>Flow</code>.
	 * 
	 * @param flow	Flow	
	 * @return		the flow index (zero based) or -1 if not found
	 */
	public int indexOfFlow(Flow flow) {
		return getFlows().indexOf( flow );
	}
	
	/**
	 * Return <code>true</code> if the use case contains the flow.
	 * @param flow	the flow to find.
	 * @return		a boolean.
	 */
	public boolean containsFlow(Flow flow) {
		return getFlows().contains( flow );
	}	
	
	/**
	 * Add a flow.
	 * 
	 * @param flow	Flow to be added.
	 * @return		<code>true</code> if successfully added.
	 * @throws RuntimeException
	 */
	public boolean addFlow(Flow flow) throws RuntimeException {
		boolean newFlowIsABasicFlow = ( flow instanceof BasicFlow );
		// First flow should be a BasicFlow
		if ( flows.isEmpty() && ! newFlowIsABasicFlow ) {
			throw new RuntimeException( "The first flow should be a basic flow." );
		}
		// Cannot add more than one BasicFlow
		if ( hasBasicFlow() && newFlowIsABasicFlow ) {
			throw new RuntimeException( "This flow already exists." );
		}
		// Don't add a flow twice
		if ( flows.contains( flow ) ) {
			return false;
		}
		return flows.add( flow );
	}
	
	/**
	 * Remove a flow. If the flow is a basic flow, then remove all the flows.
	 * 
	 * @param flow	the flow to be removed.
	 * @return		<code>true</code> if successfully removed.
	 */
	public boolean removeFlow(Flow flow) {
		if ( flow.type() == FlowType.BASIC ) { 
			flows.clear(); // Remove ALL flows !!!
			return true;
		}
		return flows.remove( flow );
	}
	
	/**
	 * Returns a flow with a given name.
	 * 
	 * @param shortName	the flow's name
	 * @return			a {@link Flow} or <code>null</code> if not found.
	 */
	public Flow flowWithShortName(final String shortName) {
		for ( Flow f : flows ) {
			if ( f.shortName().equalsIgnoreCase( shortName ) ) {
				return f;
			}
		}
		return null;
	}
	
	// BASIC FLOW
	
	/**
	 * Returns the basic flow.
	 * 
	 * @return the basic flow
	 */
	public BasicFlow basicFlow() {
		for ( Flow f : flows ) {
			if ( f.type() == FlowType.BASIC ) {
				return (BasicFlow) f;
			}
		}
		return null;
	}
	
	/**
	 * Return true if it has a basic flow.
	 * 
	 * @return <code>true</code> if it has
	 */
	public boolean hasBasicFlow() {
		return ( basicFlow() != null );
	}
	
	// PRECONDITIONS
	
	public List< ConditionState > getPreconditions() {
		return preconditions;
	}
	
	public List< Postcondition > postconditionsInPreconditions() {
		List< Postcondition  > postconditions = new ArrayList< Postcondition >();
		for ( ConditionState cs : preconditions ) {
			if ( ConditionStateKind.POSTCONDITION == cs.kind() ) {
				postconditions.add( (Postcondition) cs );
			}
		}
		return postconditions;
	}

	public void setPreconditions(List< ConditionState > preconditions) {
		this.preconditions = preconditions;
	}
	
	/**
	 * Returns the number of preconditions.
	 * 
	 * @return the number of preconditions.
	 */
	public int numberOfPreconditions() {
		return preconditions.size();
	}
	
	/**
	 * Returns a conditionState at given index.
	 * 
	 * @param index	the conditionState's index.
	 * @return		a conditionState or null if not found.
	 */
	public ConditionState preconditionAt(final int index) {
		return preconditions.get( index );
	}
	
	/**
	 * Returns the index of a conditionState.
	 * 
	 * @param state	the conditionState.
	 * @return		the index of the conditionState or -1 if not found.
	 */
	public int indexOfPrecondition(final ConditionState state) {
		return preconditions.indexOf( state );
	}
	
	/**
	 * Add a precondition.
	 * 
	 * @param precondition	the precondition to add.
	 * @return				<code>true</code> if successful, false otherwise.
	 */
	public boolean addPrecondition(ConditionState precondition) {
		if ( preconditions.contains( precondition ) ) {
			return false;
		}
		return preconditions.add( precondition );
	}
	
	/**
	 * Remove a precondition.
	 * 
	 * @param precondition	the precondition to remove.
	 * @return				<code>true</code> if successful, false otherwise.
	 */
	public boolean removePrecondition(ConditionState precondition) {
		return preconditions.remove( precondition );
	}
	
	// DEPENDENCY	

	/**
	 * Return use case dependencies.
	 * This is useful to generate a topological ordering.
	 */
	public Set< UseCase > useCaseDependencies() {
		Set< UseCase > useCases = new LinkedHashSet< UseCase >();
		// useCases.addAll( preconditionsUseCases() ); <<< edited 21/03/2014 - new scenario model
		useCases.addAll( calledUseCases() );
		return useCases;
	}

	
	/**
	 * Return use cases from preconditions (that are postconditions from
	 * other flows).
	 */
	public Set< UseCase > preconditionsUseCases() {
		Set< UseCase > useCases = new LinkedHashSet< UseCase >();
		List< Postcondition > postconditions = postconditionsInPreconditions();
		for ( Postcondition state : postconditions ) {
			useCases.add( state.getOwnerFlow().getUseCase() );
		}
		return useCases;
	}
	
	/**
	 * Return all use cases called by its flows.
	 */
	public Set< UseCase > calledUseCases() {
		Set< UseCase > useCases = new LinkedHashSet< UseCase >();
		// Add use cases from use case calls
		Collection< Flow > flows = getFlows();
		for ( Flow f : flows ) {
			Set< UseCase > flowUseCases = f.useCaseDependencies();
			useCases.addAll( flowUseCases );
		}
		return useCases;
	}	
	
	// INCLUDE FILES
	
	public Set< IncludeFile > getIncludeFiles() {
		return includeFiles;
	}
	
	public void setIncludeFiles(Set< IncludeFile > includeFiles) {
		this.includeFiles = includeFiles;
	}
	
	public boolean addIncludeFile(IncludeFile includeFile) {
		return includeFiles.add( includeFile );
	}
	
	public boolean removeIncludeFile(IncludeFile includeFile) {
		return includeFiles.remove( includeFile );
	}	
	
	/**
	 * Return the {@code IncludeFile} at the given index.
	 * @param index	the desired index.
	 * @return		the include file at the index.
	 */
	public IncludeFile includeFileAt(final int index) {
		int i = 0;
		for ( IncludeFile f : includeFiles ) {
			if ( index == i ) return f;
			++i;
		}
		return null;
	}
	
	/**
	 * Return the use case include files plus all the include files from use
	 * cases that the current use case depends on.
	 * 
	 * @return	a set of use cases.
	 */
	public Set< IncludeFile > allNeededIncludeFiles() {
		Set< IncludeFile > files = new LinkedHashSet< IncludeFile >();
		files.addAll( this.includeFiles );
		// Add include files from dependencies
		Set< UseCase > dependencies = useCaseDependencies();
		for ( UseCase uc : dependencies ) {
			files.addAll( uc.allNeededIncludeFiles() );
		}
		return files;
	}
	
	// DATABASE SCRIPTS
	
	public List< DatabaseScript > getDatabaseScripts() {
		return databaseScripts;
	}
	
	public void setDatabaseScripts(List< DatabaseScript > databaseScripts) {
		this.databaseScripts = databaseScripts;
	}
	
	public boolean addDatabaseScript(DatabaseScript databaseScript) {
		return this.databaseScripts.add( databaseScript );
	}
	
	//
	// RELATED USE CASES
	//
	
	/**
	 * Return the included use cases.
	 * @return
	 */
	public Collection< UseCase > inclusions() {
		return referencedUseCases( basicFlow().getSteps() );
	}

	/**
	 * Return the use cases that extend the current use case.
	 * @return
	 */
	public Collection< UseCase > extensions() {
		Collection< UseCase > referenced = new LinkedHashSet< UseCase >();
		for ( Flow flow : getFlows() ) {
			if ( flow.type() == FlowType.BASIC ) { continue; }
			referenced.addAll( referencedUseCases( flow.getSteps() ) );
		}
		return referenced;
	}

	/**
	 * Return the use cases referenced by the given steps.
	 * @param steps
	 * @return
	 */
	private Collection< UseCase > referencedUseCases(Collection< Step > steps) {
		Collection< UseCase > referenced = new LinkedHashSet< UseCase >();
		for ( Step step : steps ) {
			if ( step.kind() != StepKind.USE_CASE_CALL ) { continue; }
			UseCaseCallStep uccs = (UseCaseCallStep) step;
			if ( null == uccs || null == uccs.getUseCase() ) { continue; }
			referenced.add( uccs.getUseCase() );
		}
		return referenced;
	}
	
	//
	// OTHER
	//
	
	@Override
	public UseCase copy(final UseCase that) {
		if ( null == that ) return this;
		
		this.id = that.id;
		this.ignoreToGenerateTests = that.ignoreToGenerateTests;
		this.name = that.name;
		this.description = that.description;
		
		// When cloning the flows, they will continue pointing to the other
		// use case's elements. It is necessary to change the flows' elements
		// by replacing each element with the corresponding cloned element.
		// This can be made using the element id.

		CopierUtil.copyCollection( that.elements, this.elements );
		pointElementsToMe( this.elements );
		
		CopierUtil.copyCollection( that.flows, this.flows );
		pointFlowsToMe( this.flows );
		replaceUseCaseElementsInTheFlows( this.flows );
		
		this.actors.clear();
		this.actors.addAll( that.actors ); // Copy references because the actors belong to the software
		
		CopierUtil.copyCollection( that.preconditions, this.preconditions );
		
		CopierUtil.copyCollection( that.includeFiles, this.includeFiles );
		
		CopierUtil.copyCollection( that.databaseScripts, this.databaseScripts );

		return this;
	}

	@Override
	public UseCase newCopy() {
		return ( new UseCase() ).copy( this );
	}
	
	@Override
	public int compareTo(UseCase o) {
		return name.compareToIgnoreCase( o.name );
	}	
	
	@Override
	public String toString() {
		return getName();
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode( new Object[] {
			ignoreToGenerateTests,
			name,
			description,
			flows,
			actors,
			elements,
			preconditions,
			includeFiles,
			databaseScripts
		} );
	}
	
	@Override
	public boolean equals(Object obj) {
		if ( ! ( obj instanceof UseCase ) ) return false;
		final UseCase that = (UseCase) obj;
		return // Just compare the name
			EqUtil.equalsIgnoreCase( this.name, that.name )
			;
	}
	
	private void pointFlowsToMe(List< Flow > flows) {
		for ( Flow f : flows ) {
			f.setUseCase( this );
		}
	}
	
	private void pointElementsToMe(Collection< Element > elements) {
		for ( Element e : elements ) {
			e.setUseCase( this );
		}
	}
	
	
	/**
	 * Replaces the current flows' elements with the use case's elements.
	 * 
	 * When cloning the flows, they will continue pointing to the other
	 * use case's elements. It is necessary to change the flows' elements
	 * by replacing each element with the corresponding cloned element.
	 * This can be made using the element id.
	 * 
	 * @param flowsToAnalyze	the flows to analyze.
	 */
	private void replaceUseCaseElementsInTheFlows(
			Collection< Flow > flowsToAnalyze
			) {
		for ( Flow f : flowsToAnalyze ) {
			for ( Step s : f.getSteps() ) {
				if ( s instanceof ElementBasedStep ) {
					
					ElementBasedStep ebs = (ElementBasedStep) s;
					// Save the elements to replace in elementsToAdd,
					// remove the current elements, and add the saved elements.
					Collection< Element > elementsToAdd = new LinkedHashSet< Element >(); 
					Iterator< Element > oldElementIt = ebs.getElements().iterator();
					while ( oldElementIt.hasNext() ) {
						final Element oldElement = oldElementIt.next();
						final Element newElement = elementWithId( oldElement.getId() );
						
						if ( null == newElement ) continue;
						
						logger.debug( "oldElement @" + System.identityHashCode( oldElement ) +
								" newElement @" + System.identityHashCode( newElement ) );
						
						elementsToAdd.add( newElement );
						// Remove the old element
						oldElementIt.remove();
					}
					// Add all the new elements
					ebs.getElements().addAll( elementsToAdd );
				}
			}
		}
	}
}
