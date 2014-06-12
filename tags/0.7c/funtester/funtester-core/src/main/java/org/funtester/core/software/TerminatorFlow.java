package org.funtester.core.software;

import java.util.Arrays;

import org.funtester.common.util.EqUtil;

/**
 * An alternate flow that terminates the use case.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class TerminatorFlow extends AlternateFlow implements TerminableFlow {

	private static final long serialVersionUID = -194791136749582627L;

	public TerminatorFlow() {
		super();
	}
	
	public TerminatorFlow(UseCase useCase, String description) {
		super( useCase, description );
	}
	
	/**
	 * Returns the flow type. Should be overridden in child classes.
	 * @return
	 */
	public FlowType type() {
		return FlowType.TERMINATOR;
	}

	public Step terminatorStep() { // Returns the last step
		int size = getSteps().size();
		return ( size > 0 ) ? getSteps().get( size - 1 ) : null;
	}
	
	@Override
	public Flow copy(final Flow obj) {
		super.copy( obj );
		return this;
	}
	
	@Override
	public Flow newCopy() {
		return ( new TerminatorFlow() ).copy( this );
	}
	
	@Override
	public int hashCode() {
		return super.hashCode() * Arrays.hashCode( new Object[] {
			terminatorStep()
		} );
	}
	
	@Override
	public boolean equals(Object obj) {
		if ( ! ( obj instanceof TerminatorFlow ) ) { return false; }
		final TerminatorFlow that = (TerminatorFlow) obj;
		return super.equals( obj )
			&& EqUtil.equals( this.terminatorStep(), that.terminatorStep() )
			;
	}
	
	//
	// Not necessary to override toString()
	//
}
