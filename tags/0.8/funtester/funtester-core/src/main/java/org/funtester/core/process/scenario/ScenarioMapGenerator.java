package org.funtester.core.process.scenario;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.funtester.core.software.Flow;
import org.funtester.core.software.Scenario;
import org.funtester.core.software.UseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generate scenarios for each use case and put them in a map.
 * 
 * The scenarios are generated using a {@code UseCaseScenarioGenerator}.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class ScenarioMapGenerator {

	private final Logger logger = LoggerFactory.getLogger( ScenarioMapGenerator.class );
	
	private final UseCaseScenarioGenerator scenarioListGenerator;
	private final int defaultFlowRepetitions;
	
	public ScenarioMapGenerator(
			final UseCaseScenarioGenerator scenarioListGenerator,
			final int defaultFlowRepetitions
			) {
		this.scenarioListGenerator = scenarioListGenerator;
		this.defaultFlowRepetitions = defaultFlowRepetitions;
	}
	
	public Map< UseCase, List< Scenario > > generate(
			final Map< UseCase, Integer > useCaseToMaxFlowRepetitionsMap
			) {
		Map< UseCase, List< Scenario > > useCaseScenariosMap =
				new LinkedHashMap< UseCase, List< Scenario > >();
		
		Collection< UseCase > useCases = useCaseToMaxFlowRepetitionsMap.keySet();
		for ( UseCase uc : useCases ) {

			Flow flow = uc.basicFlow();
			List< Flow > flows = uc.getFlows();
			logger.debug( "___GENERATING__ Use Case: " + uc.getName() + " and flow " + flow.shortName() );
			
			Integer maxFlowRepetitions = useCaseToMaxFlowRepetitionsMap.get( uc );
			if ( null == maxFlowRepetitions ) {
				maxFlowRepetitions = new Integer( defaultFlowRepetitions );
			}

			List< Scenario > scenarios = scenarioListGenerator.generate(
					flow, flows, maxFlowRepetitions, uc );
			
			useCaseScenariosMap.put( uc, scenarios );
		}
		
		return useCaseScenariosMap;
	}

}
