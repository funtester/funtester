package org.funtester.core.process.scenario.combinator;

import org.funtester.core.software.Scenario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Creates a new scenario that copies the first one and add the second one at
 * the beginning.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class PushFrontScenarioCombinator implements ScenarioCombinator {

	private static final Logger logger = LoggerFactory.getLogger( PushFrontScenarioCombinator.class );
	
	public Scenario combine(Scenario targetScenario, Scenario scenarioToCombine) {
		Scenario newScenario = targetScenario.newCopy();
		logger.debug( "INSERTING into \"" + targetScenario.longName()
				+ "\" the steps of steps from \"" + scenarioToCombine.longName() + "\"" );
		// Add other steps at the beginning
		newScenario.addAllSteps( 0, scenarioToCombine.getSteps() );
		// Add the include files
		newScenario.addIncludeFiles( scenarioToCombine.getIncludeFiles() );
		return newScenario;
	}

}
