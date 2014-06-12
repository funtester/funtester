package org.funtester.core.software;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Flow information
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class FlowInfo {

	/**
	 * Returns the flows in {@code flows} that have starter flow equals to the
	 * {@code targetFlow}).
	 * 
	 * @param flows			the flows to be analyzed.
	 * @param targetFlow	the flow to be compared with each stater flow. 
	 * @return				a collection of flows (that can be empty).
	 */
	public static Collection< Flow > flowsLeaving(
			final Collection< Flow > flows,
			final Flow targetFlow
			) {
		List< Flow > list = new ArrayList< Flow >();
		for ( Flow f : flows ) {
			// If is not the same flow and is an alternate flow
			if ( f != targetFlow && ( f instanceof AlternateFlow ) ) { // different object (compare address)
				// If the starter flow points to the targetFlow
				Flow starterFlow = ( (AlternateFlow) f ).getStarterFlow();
				if ( starterFlow != null && starterFlow.equals( targetFlow ) ) {
					list.add( f ); // Add to the list
				}
			}
		}
		return list;
	}
	
	
	/**
	 * Return the flows in {@code flows} that have the starter flow equals to
	 * the {@code targetStep}'s flow and starter step equals to the
	 * {@code targetStep}.
	 * 
	 * @param flows			the flows to be analyzed.
	 * @param targetStep	the step to be compared with each stater step. 
	 * @return				a collection of flows (that can be empty).
	 */
	public static Collection< Flow > flowsLeavingTheStep(
			final Collection< Flow > flows,
			final Step targetStep
			) {
		if ( null == targetStep ) throw new IllegalArgumentException( "step" );
		List< Flow > list = new ArrayList< Flow >();
		Flow stepFlow = targetStep.getFlow();
		//debug( "*** >>> STEP FLOW: " + stepFlow.shortName() );
		for ( Flow f : flows ) {	
			if ( f != stepFlow && ( f instanceof AlternateFlow ) ) {
				//debug( "*** DIFERENT flow: " + f.shortName() );
				AlternateFlow af = (AlternateFlow) f;
				if ( af.getStarterFlow() == stepFlow
						&& af.getStarterStep() == targetStep ) {
					list.add( f );					
				}
			}
			// else {
				//debug( "*** EQUALS OR not Alternate: " + f.shortName() );
			// }
		}
		return list;
	}
	
	
	/**
	 * Returns the flows in {@code flows} that have returning flow equals to the
	 * {@code targetFlow}).
	 * 
	 * @param flows			the flows to be analyzed.
	 * @param targetFlow	the flow to be compared with each stater flow. 
	 * @return				a collection of flows (that can be empty).
	 */	
	public static Collection< Flow > flowsReturningTo(
			final Collection< Flow > flows,
			final Flow flow
			) {
		List< Flow > list = new ArrayList< Flow >();
		for ( Flow f : flows ) {
			if ( f != flow && ( f instanceof ReturnableFlow ) ) { // different object (compare address)
				Flow returningFlow = ((ReturnableFlow) f ).getReturningFlow();
				if ( returningFlow != null && returningFlow.equals( flow ) ) { 
					list.add( f );
				}
			}
		}
		return list;
	}
}
