package org.funtester.core.software;


/**
 * Flow that can cancel the use case execution.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class CancelatorFlow extends TerminatorFlow {

	private static final long serialVersionUID = -312848386486210467L;

	public CancelatorFlow() {
		super();
	}
	
	public CancelatorFlow(UseCase useCase, String description) {
		super( useCase, description );
	}
	
	/**
	 * Returns the flow type. Should be overridden in child classes.
	 * @return
	 */
	public FlowType type() {
		return FlowType.CANCELATOR;
	}
	
	@Override
	public Flow copy(Flow obj) {
		super.copy( obj );
		return this;
	}
	
	@Override
	public Flow newCopy() {
		return ( new CancelatorFlow() ).copy( this );
	}
	
	//
	// Not necessary to override hashCode(), toString(), equals()
	//	
}
