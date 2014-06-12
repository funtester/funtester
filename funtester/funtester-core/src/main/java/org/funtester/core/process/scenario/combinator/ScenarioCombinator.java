package org.funtester.core.process.scenario.combinator;

import org.funtester.core.software.Scenario;

/**
 * Allow to combine two scenarios. 
 *
 * @author Thiago Delgado Pinto
 */
public interface ScenarioCombinator {

	/**
	 * Combine two scenarios generating a new one.
	 *
	 * @param targetScenario	Target scenario.
	 * @param scenarioToCombine	Scenario to combine with the target scenario. 
	 * @return					A new combined scenario.
	 */
	Scenario combine(
			Scenario targetScenario,
			Scenario scenarioToCombine
			);
}