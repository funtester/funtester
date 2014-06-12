package org.funtester.core.software;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.funtester.common.util.EqUtil;

import com.fasterxml.jackson.annotation.JsonIdentityReference;

/**
 * Alternate flow
 * 
 * @author Thiago Delgado Pinto
 *
 */
public abstract class AlternateFlow extends Flow {

	private static final long serialVersionUID = 1915789028180843466L;

	private String description = "";
	@JsonIdentityReference(alwaysAsId=true)
	private Flow starterFlow = null;
	@JsonIdentityReference(alwaysAsId=true)
	private Step starterStep = null;	
	@JsonIdentityReference(alwaysAsId=true)
	private List< AlternateFlow > influencingFlows = new ArrayList< AlternateFlow >();
	
	public AlternateFlow() { 
		super();
	}
	
	public AlternateFlow(UseCase useCase, String description) {
		super( useCase );
		setDescription( description );
	}
	
	//
	// Not necessary to override type()
	//
	
	@Override
	public String shortName() {
		return "AF" + getUseCase().indexOfFlow( this );
	}	

	public void setStarterFlow(Flow flow) {
		if ( this == flow ) {
			throw new IllegalArgumentException( "StarterFlow cannot be *this*" );
		}
		this.starterFlow = flow;		
	}

	public Flow getStarterFlow() {
		return this.starterFlow;
	}

	public void setStarterStep(Step step) {
		this.starterStep = step;
	}

	public Step getStarterStep() {
		return starterStep;
	}
	
	/** 
	 * @return the steps before the starter step (from starter flow). Returns
	 * a empty list if any of them are null.
	 */
	public List< Step > stepsBeforeTheStarterStep() {
		List< Step > steps = new ArrayList< Step >();
		Flow starterFlow = getStarterFlow();
		Step starterStep = getStarterStep();		
		if ( starterFlow != null && starterStep != null ) {
			List< Step > starterFlowSteps = starterFlow.getSteps();
			for ( Step s : starterFlowSteps ) {
				if ( s != starterStep ) { // Compare address
					steps.add( s );
				} else {
					break;
				}
			}
		}
		return steps;
	}

	public void setDescription(String description) {
		this.description  = description;		
	}

	public String getDescription() {
		return description;
	}
	
	public List< AlternateFlow > getInfluencingFlows() {
		return influencingFlows;
	}

	public void setInfluencingFlows(List< AlternateFlow > influencingFlows) {
		this.influencingFlows = influencingFlows;
	}
	
	/**
	 * Adds a influencing flow.
	 * 
	 * @param anotherFlow	the flow to add.
	 * @return				true if added, false otherwise.
	 */
	public boolean addInfluencingFlow(AlternateFlow anotherFlow) {
		if ( influencingFlows.contains( anotherFlow ) ) {
			return false;
		}
		return influencingFlows.add( anotherFlow );		
	}

	/**
	 * Returns a influencing flow at a certain index.
	 * 
	 * @param index	the index of the desired flow.
	 * @return		the flow.
	 */
	public AlternateFlow influencingFlowAt(int index) {
		return getInfluencingFlows().get( index );
	}

	/**
	 * Returns the number of influencing flows.
	 * 	
	 * @return	the number of influencing flows.
	 */
	public int numberOfInfluencingFlows() {
		return influencingFlows.size();
	}
	
	@Override
	public Flow copy(final Flow obj) {
		super.copy( obj );
		if ( ! ( obj instanceof AlternateFlow ) ) return this; // As is
		final AlternateFlow that = (AlternateFlow) obj;
		this.description = that.description;
		this.starterFlow = that.starterFlow; // Reference
		this.starterStep = that.starterStep; // Reference
		
		if ( this.influencingFlows != null ) {
			this.influencingFlows.clear();
			if ( that.influencingFlows != null ) {
				this.influencingFlows.addAll( that.influencingFlows ); // References
			}
		}
		return this;
	}
	
	@Override
	public String toString() {
		if ( getDescription().isEmpty() ) {
			return shortName();
		}		
		return shortName() + " - " + getDescription();
	}
	
	@Override
	public int hashCode() {
		return super.hashCode() * Arrays.hashCode( new Object[] {
			description, starterFlow, starterStep, influencingFlows
		} );
	}
	
	@Override
	public boolean equals(Object obj) {
		if ( ! ( obj instanceof AlternateFlow ) ) return false;
		final AlternateFlow that = (AlternateFlow) obj;
		return super.equals( that )
			/*
			&& ( EqUtil.equalsIgnoreCase( this.description, that.description )
				|| ( EqUtil.equals( this.starterFlow, that.starterFlow )
					&& EqUtil.equals( this.starterStep, that.starterStep ) ) )
			*/
			&& EqUtil.equalsIgnoreCase( this.description, that.description )
			&& EqUtil.equals( this.starterFlow, that.starterFlow )
			&& EqUtil.equals( this.starterStep, that.starterStep )
			;
	}
}
