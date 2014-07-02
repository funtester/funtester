package org.funtester.core.software;

import java.util.Arrays;
import java.util.List;

import org.funtester.common.util.EqUtil;

import com.fasterxml.jackson.annotation.JsonIdentityReference;

/**
 * A flow that can return to another flow.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class ReturnableFlow extends AlternateFlow {

	private static final long serialVersionUID = -3128690385661321285L;
	
	@JsonIdentityReference(alwaysAsId=true)
	private Flow returningFlow = null;
	@JsonIdentityReference(alwaysAsId=true)
	private Step returningStep = null;
	
	public ReturnableFlow() {
		super();
	}
	
	public ReturnableFlow(UseCase useCase, String description) {
		super( useCase, description );
	}
	
	/**
	 * Returns the flow type. Should be overridden in child classes.
	 * @return
	 */
	public FlowType type() {
		return FlowType.RETURNABLE;
	}
	
	@Override
	public void setStarterFlow(Flow starterFlow) {
		super.setStarterFlow( starterFlow );
		// By default, the returning flow is the starter flow
		if ( null == getReturningFlow() ) {
			setReturningFlow( getStarterFlow() );
		}
	}

	public Flow getReturningFlow() {
		return returningFlow;
	}

	public void setReturningFlow(Flow returningFlow) {
		this.returningFlow = returningFlow;
	}

	public Step getReturningStep() {
		return returningStep;
	}

	public void setReturningStep(Step returningStep) {
		this.returningStep = returningStep;
	}

	/**
	 * Returns true if the flows are equals and the returning step is prior to
	 * the starter step. Otherwise returns false.
	 * 
	 * @return
	 */
	public boolean isRecursive() {
		if ( getReturningFlow() != getStarterFlow() ) {
			return true; // ?
		}
		List< Step > steps = getStarterFlow().getSteps();
		int indexOfStarterStep = steps.indexOf( getStarterStep() );
		int indexOfReturningStep = steps.indexOf( getReturningStep() );
		return ( indexOfReturningStep < indexOfStarterStep );
	}	
	
	@Override
	public Flow copy(final Flow obj) {
		super.copy( obj );
		if ( obj instanceof ReturnableFlow ) {
			final ReturnableFlow that = (ReturnableFlow) obj;
			this.returningFlow = that.returningFlow; // Reference
			// The starter flow will be the returning flow by default
			if ( null == this.returningFlow ) {
				this.returningFlow = getStarterFlow(); 
			}
			this.returningStep = that.returningStep; // Reference
		}
		return this;
	}

	@Override
	public Flow newCopy() {
		return ( new ReturnableFlow() ).copy( this );
	}
	
	//
	// Not necessary to override toString()
	//
	
	@Override
	public int hashCode() {
		return super.hashCode() * Arrays.hashCode( new Object[] {
			returningFlow, returningStep
		} );
	}
	
	@Override
	public boolean equals(Object obj) {
		if ( ! ( obj instanceof ReturnableFlow ) ) { return false; }
		final ReturnableFlow that = (ReturnableFlow) obj;
		return super.equals( that )
			&& EqUtil.equalsAdresses( this.returningFlow, that.returningFlow )
			&& EqUtil.equalsAdresses( this.returningStep, that.returningStep )
			;
	}	
}
