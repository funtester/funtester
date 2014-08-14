package org.funtester.core.software;

/**
 * Precondition
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class Precondition extends ConditionState {

	private static final long serialVersionUID = -992717336335151936L;

	@Override
	public ConditionStateKind kind() {
		return ConditionStateKind.PRECONDITION;
	}
	
	@Override
	public Precondition copy(final ConditionState obj) {
		super.copy( obj );
		return this;
	}

	@Override
	public Precondition newCopy() {
		return ( new Precondition() ).copy( this );
	}

	//
	// Not necessary to override toString(), hashCode(), equals()
	//	
}
