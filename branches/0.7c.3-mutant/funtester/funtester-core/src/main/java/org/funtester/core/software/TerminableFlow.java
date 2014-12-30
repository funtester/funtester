package org.funtester.core.software;

/**
 * A terminable flow.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public interface TerminableFlow {
	
	/**
	 * Returns the step where the flow terminates. Usually the last one.
	 * 
	 * @return	a step.
	 */
	Step terminatorStep();
	
}
