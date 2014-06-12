package org.funtester.core.process.scenario.combinator;

import java.util.ArrayList;
import java.util.List;

import org.funtester.core.profile.StepKind;
import org.funtester.core.software.Scenario;
import org.funtester.core.software.Step;
import org.funtester.core.software.UseCase;
import org.funtester.core.software.UseCaseCallStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * For each step from the {@code targetScenario} that calls the use case in the
 * {@code scenarioToCombine}, replace it with the steps from
 * {@code scenarioToCombine}. 
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class UseCaseCallBasedScenarioCombinator
	implements ScenarioCombinator {
	
	private static Logger logger = LoggerFactory.getLogger( UseCaseCallBasedScenarioCombinator.class ); 

	public Scenario combine(
			final Scenario targetScenario,
			final Scenario scenarioToCombine
			) {
		Scenario newScenario = targetScenario.newCopy();
		
		//
		// BASIC IDEA: Every time that a step that calls a use case is found,
		// it is tested if the called use case and the scenario's use case are
		// the same. If they are, the step is replaced by the other scenario's
		// steps.
		//
		int i = 0;
		
		// Use a separate list to reference the steps of the new (cloned)
		// scenario because the scenario steps are changed over time.
		List< Step > steps = new ArrayList< Step >();
		steps.addAll( newScenario.getSteps() );
		
		// Analyze the steps
		for ( Step s : steps ) {
			
			// Ignore steps that are not an use case call
			if ( ! s.kind().equals( StepKind.USE_CASE_CALL ) ) {
				continue;
			}
			
			// Check if the element's use case and the scenario's use case are
			// the same
			final UseCase stepUseCase = ( (UseCaseCallStep) s ).getUseCase();
			if ( ! stepUseCase.equals( scenarioToCombine.getUseCase() ) ) {
				logger.debug( ++i + ") Ignored step with use case \"" + stepUseCase
						+ "\" because it is different from the use case to combine: \""
						+ scenarioToCombine.getUseCase().getName() + "\"" );
				continue; // Go to next step
			}
			
			logger.debug( ++i + ") Combining \"" + newScenario.longName() + "\""
					+ " with steps from \"" + scenarioToCombine.longName() + "\"" );
			
			// Replace the step
			newScenario.replaceStepWithAStepList( s, scenarioToCombine.getSteps() );
		}
		
		// Add the include files
		newScenario.addIncludeFiles( scenarioToCombine.getIncludeFiles() );
		
		return newScenario;
	}

}
