package org.funtester.core.software;

import com.fasterxml.jackson.annotation.JsonIdentityReference;

/**
 * Postcondition
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class Postcondition extends ConditionState {

	private static final long serialVersionUID = 6692986643747740722L;
	
	// @JsonBackReference
	//@JsonIgnore
	@JsonIdentityReference(alwaysAsId=true)
	private Flow ownerFlow;

	public Postcondition() {
		super();
	}
	
	public Postcondition(String description) {
		super( description );
	}
	
	public Postcondition(String description, Flow ownerFlow) {
		super( description );
		this.ownerFlow = ownerFlow;
	}
	
	public Flow getOwnerFlow() {
		return ownerFlow;
	}
	
	public void setOwnerFlow(Flow ownerFlow) {
		this.ownerFlow = ownerFlow;
	}

	@Override
	public ConditionStateKind kind() {
		return ConditionStateKind.POSTCONDITION;
	}
	
	@Override
	public Postcondition copy(ConditionState obj) {
		super.copy( obj );
		if ( obj instanceof Postcondition ) {
			final Postcondition that = (Postcondition) obj;
			this.ownerFlow = that.ownerFlow; // Reference
		}
		return this;
	}

	@Override
	public Postcondition newCopy() {
		return ( new Postcondition() ).copy( this );
	}
	
	//
	// Not necessary to override toString(), hashCode(), equals()
	//
}
