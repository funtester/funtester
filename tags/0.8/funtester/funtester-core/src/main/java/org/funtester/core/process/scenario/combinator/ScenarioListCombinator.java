package org.funtester.core.process.scenario.combinator;

import java.util.ArrayList;
import java.util.List;

import org.funtester.core.software.Scenario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Scenario list combinator. 
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class ScenarioListCombinator {
	
	private static final Logger logger = LoggerFactory.getLogger( ScenarioListCombinator.class );
	private final ScenarioCombinator combinator;
	
	public ScenarioListCombinator(ScenarioCombinator combinator) {
		this.combinator = combinator;
	}

	/**
	 * Try to combine each scenario from the <code>targetScenario</code> with
	 * each scenario from the <code>scenariosToCombine</code>.
	 * 
	 * @param targetScenarios		the target scenarios
	 * @param scenariosToCombine	the scenarios to combine
	 * @return						a list of combined scenarios
	 */
	public List< Scenario > combine(
			final List< Scenario > targetScenarios,
			final List< Scenario > scenariosToCombine
			) {
		List< Scenario > combinedScenarios = new ArrayList< Scenario >();
		
		if ( scenariosToCombine.isEmpty() ) {
			combinedScenarios.addAll( targetScenarios );
			return combinedScenarios;
		}
		
		for ( Scenario a : targetScenarios ) {
			for ( Scenario b : scenariosToCombine ) {
				
				logger.debug( "COMBINING \"" + a.getUseCase().getName() + " {" + a.getName() + "}\""
						+ " WITH \"" + b.getUseCase().getName() + " {" + b.getName() + "}\" "
						);
				Scenario c = combinator.combine( a, b );
				combinedScenarios.add( c );
			}
		}
		
		return combinedScenarios;
	}
	
	/**
	 * Returns the possible combinations.
	 * 
	 * @param first		First list of scenarios.
	 * @param second	Second list of scenarios.
	 * @return			The possible combinations.
	 */
	public int possibleCombinations(
			List< Scenario > first,
			List< Scenario > second
			) {
		return first.size() * second.size();
	}
	
}
