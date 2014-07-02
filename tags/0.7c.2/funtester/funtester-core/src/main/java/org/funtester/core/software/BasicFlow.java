package org.funtester.core.software;


/**
 * Basic flow
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class BasicFlow extends Flow implements TerminableFlow {
	
	private static final long serialVersionUID = 1025976476802500083L;	
	
	public BasicFlow() {
		super();
	}
	
	public BasicFlow(UseCase useCase) {
		super( useCase );
	}
	
	/**
	 * Returns the flow type. Should be overridden in child classes.
	 * @return
	 */
	public FlowType type() {
		return FlowType.BASIC;
	}
	
	@Override
	public String shortName() {
		return "BF";
	}	

	/**
	 * Returns the last step in the current implementation (BasicFlow).
	 */
	public Step terminatorStep() {		
		int size = getSteps().size();
		return ( size > 0 ) ? getSteps().get( size - 1 ) : null;
	}
	
	@Override
	public Flow copy(Flow obj) {
		super.copy( obj );		
		return this;
	}
	
	@Override
	public Flow newCopy() {
		return ( new BasicFlow() ).copy( this );
	}
	
	//
	// Not necessary to override toString(), hashCode(), equals()
	//
}
