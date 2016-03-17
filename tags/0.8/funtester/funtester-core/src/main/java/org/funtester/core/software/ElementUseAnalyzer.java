package org.funtester.core.software;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import org.funtester.core.profile.StepKind;

/**
 * Use analyzer for a {@link Element}.
 *
 * @author Thiago Delgado Pinto
 *
 */
public class ElementUseAnalyzer {

	/**
	 * Return the flows using the given element.
	 *
	 * @param e		the given element.
	 * @param flows	the collection of flows to find the element.
	 * @return		the flows that uses the element (in their steps).
	 */
	public Collection< Flow > flowsUsingElement(
			final Element e,
			final Collection< Flow > flows
			) {
		Set< Flow > use = new LinkedHashSet< Flow >();
		for ( Flow f : flows ) {
			for ( Step s : f.getSteps() ) {
				if ( s.kind().equals( StepKind.ACTION )
					|| s.kind().equals( StepKind.ORACLE ) ) {

					ElementBasedStep ebs = (ElementBasedStep) s;
					if ( ebs.containsElement( e ) ) {
						use.add( f );
					}
				}
			}
		}
		return use;
	}

}
