package org.funtester.core.process.scenario;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.funtester.core.process.scenario.combinator.PushFrontScenarioCombinator;
import org.funtester.core.process.scenario.combinator.ScenarioListCombinator;
import org.funtester.core.process.scenario.combinator.UseCaseCallBasedScenarioCombinator;
import org.funtester.core.process.usecase.GraphBasedUseCaseDependencyAnalyzer;
import org.funtester.core.process.usecase.UseCaseDependencyAnalyzer;
import org.funtester.core.profile.StepKind;
import org.funtester.core.software.Flow;
import org.funtester.core.software.Scenario;
import org.funtester.core.software.Step;
import org.funtester.core.software.UseCase;
import org.funtester.core.software.UseCaseCallStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generate all the {@code Scenario}s for a {@code Software}.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class SoftwareScenariosGenerator {
	
	private static final Logger logger = LoggerFactory.getLogger( SoftwareScenariosGenerator.class );
		
	private final UseCaseDependencyAnalyzer useCaseDependencyAnalyzer;
	private final UseCaseScenarioGenerator useCaseScenarioGenerator;
	private final ScenarioListCombinator useCaseCallBasedScenarioListCombinator;
	private final ScenarioListCombinator pushFrontScenarioListCombinator;
 
	public SoftwareScenariosGenerator() {
		
		useCaseDependencyAnalyzer = new GraphBasedUseCaseDependencyAnalyzer();
		useCaseScenarioGenerator = new UseCaseScenarioGenerator();
		
		useCaseCallBasedScenarioListCombinator = new ScenarioListCombinator(
				new UseCaseCallBasedScenarioCombinator() );
		
		pushFrontScenarioListCombinator = new ScenarioListCombinator(
				new PushFrontScenarioCombinator() );
	}

	/**
	 * Generate a map with the use cases and their generated scenarios.
	 * 
	 * @param useCaseToMaxFlowRepetitionsMap
	 * 			the map from a use case to a max flow repetitions value
	 * @param defaultMaxFlowRepetitions
	 * 			the default value to repetitions, use when the integer
	 * 			value in the map is null
	 * @return	a map
	 */
	public Map< UseCase, List< Scenario > > generate(
			final Map< UseCase, Integer > useCaseToMaxFlowRepetitionsMap,
			final int defaultMaxFlowRepetitions
			) {
		
		//
		// 1- Order use cases by dependency;
		//
		// > This guarantee that the generated scenarios respect the dependency
		//   order. Each use case will only call scenarios from use cases whose
		//   scenarios were generated before.
		//
		List< UseCase > orderedUseCases = useCaseDependencyAnalyzer.topologicalOrder(
			useCaseToMaxFlowRepetitionsMap.keySet() );
		
		//
		// 2- Generate basic scenarios for each use case;
		//
		// > The scenarios do not come with preconditions steps, activation
		//   steps nor use case case called steps. All these steps will be
		//   added soon.
		//
		// TODO make this in multiple threads
		
		ScenarioMapGenerator scenarioMapGenerator = new ScenarioMapGenerator(
				useCaseScenarioGenerator, defaultMaxFlowRepetitions );
		
		Map< UseCase, List< Scenario > > scenarioMap =
				scenarioMapGenerator.generate( useCaseToMaxFlowRepetitionsMap );
		
		//
		// 3- Replace steps with use case calls, creating new scenarios;
		//
		//		> Because of the topological order, the scenarios of the
		//		  called use cases will be available to use. 
		//
		// IDEA:
		//
		//	3.1)	Select only the scenarios that call use cases to a new sublist;
		//	3.2)	Remove this sublist from the current use case scenarios list;
		//  3.3)	Combines the sublist scenarios with the scenarios of the called
		//			use case, generating (a lot of) new scenarios;
		//  3.4)	Add the sublist back to the use case scenarios.
		//
		
		for ( UseCase useCase : orderedUseCases ) {
			
			List< Scenario > useCaseScenarios = scenarioMap.get( useCase );
			
			// 3.1
			List< Scenario > useCaseScenariosThatCallOtherUseCases =
				scenariosThatCallsUseCases( useCaseScenarios );
			
			logger.debug( "Those that calls use cases: " + useCaseScenariosThatCallOtherUseCases.size() );
			
			if ( ! useCaseScenariosThatCallOtherUseCases.isEmpty() ) {
				
				logger.debug( "Before remove all that calls use case: " + useCaseScenarios.size() );
				// 3.2
				useCaseScenarios.removeAll( useCaseScenariosThatCallOtherUseCases );
				logger.debug( "After remove all that call use case: " + useCaseScenarios.size() );
				
				for ( Scenario s : useCaseScenariosThatCallOtherUseCases ) {
					
					Collection< UseCase > calledUseCases = s.calledUseCases();
					
					for ( UseCase calledUseCase : calledUseCases ) {	
					
						//
						// Get the SUCCESSFUL scenarios from called use case.
						//
						// IMPORTANT: Because of the topological order, the called
						// use case scenarios will already be combined with other
						// use cases and so on.
						//
						List< Scenario > scenariosFromTheCalledUseCase =
							scenariosWithSuccessfulEnd( scenarioMap.get( calledUseCase ) );
						
						// 3.3
						List< Scenario > combinedScenarios = useCaseCallBasedScenarioListCombinator.combine(
								useCaseScenariosThatCallOtherUseCases,
								scenariosFromTheCalledUseCase
								);
	
						// 3.4
						useCaseScenarios.addAll( combinedScenarios );
						// currentScenarios is not ordered anymore.
						
						logger.debug( "After combine scenarios that call use cases: " + useCaseScenarios.size() );
						
					} // calledUseCases
					
				} // scenariosThatCallUseCases
			
			} // if
			
			
			//
			// 4-	Combine the current scenarios with the use cases that
			//		call the current use case.
			//
			//		> The NEXT use cases' scenarios still have steps that
			//		  call use cases. So, they can have steps that call the
			//		  current use case. The idea is to select all the
			//		  scenarios (not only those that have successful ends)
			//		  that have a step that call to the current use case.
			//		  Each selected scenario will be cut from the first step
			//		  to the previous of the one that calls the current use
			//		  case.
			//
			
			logger.debug( "Use case scenarios before combining with the previous: " + useCaseScenarios.size() );
			
			List< Scenario > combinedPreviousScenarios = 
					extractPreviousScenarios( useCase, orderedUseCases, scenarioMap );
			
			logger.debug( "Previous scenarios: " + useCaseScenarios.size() );
			
			// Merge the caller scenarios with the current scenarios
			
			List< Scenario > mergedScenarios = pushFrontScenarioListCombinator.combine(
					useCaseScenarios, combinedPreviousScenarios );
			
			logger.debug( "Use case scenarios after combining with the previous: " + mergedScenarios.size() );
			
			scenarioMap.put( useCase, mergedScenarios );
			
		} // ordered use cases
	
		
		return scenarioMap;
	}
	

	/**
	 * Extract previous scenarios where they call the given use case.
	 * <p></p>
	 * For instance, given the use cases "A", "B", and "C" so that
	 * "A" calls "B" that calls "C". When we sort them topologically, the
	 * order is C -> B -> A. However, the previous scenarios of "C" are
	 * those from "B" and "A". 
	 * <p></p>
	 * The selected scenarios from "B" are those that have steps until
	 * the call to "C". The selected scenarios from "A" are those that
	 * have steps until the call to "B".
	 * <p></p>
	 * 
	 * @param useCase			the current use case.
	 * @param orderedUseCases	the topologically ordered use cases.
	 * @param scenarioMap		the scenario map.
	 * @return					the combined previous scenarios.
	 */
	private List< Scenario > extractPreviousScenarios(
			final UseCase useCase,
			final List< UseCase > orderedUseCases,
			final Map< UseCase, List< Scenario > > scenarioMap
			) {
		
		List< Scenario > allPreviousScenarios = new ArrayList< Scenario >();
		
		// Select the use cases that call the current use case
		List< UseCase > callerUseCases = useCasesThatCall( useCase, orderedUseCases );
		for ( UseCase caller : callerUseCases ) {
			
			// Select the scenarios that call some use case
			List< Scenario > callerScenariosThatCallUseCases = scenariosThatCallsUseCases(
					scenarioMap.get( caller ) );
			
			List< Scenario > cutScenarios = new ArrayList< Scenario >();
			
			// If the scenario calls the current use case, create a new
			// scenario containing the steps before the caller step
			for ( Scenario scenario : callerScenariosThatCallUseCases ) {
				
				List< Step > cutSteps = stepsBeforeTheUseCaseCall( useCase, scenario.getSteps() );
				final boolean currentUseCaseIsCalled = ! cutSteps.isEmpty();
				if ( currentUseCaseIsCalled ) {
					Scenario cutScenario = ( new Scenario() )
							.withUseCase( scenario.getUseCase() )
							.withName( scenario.getName() )
							.addSteps( cutSteps )
							.addIncludeFiles( scenario.getUseCase().getIncludeFiles() )
							;
					// Add the scenario
					cutScenarios.add( cutScenario );
				}
				
			}
			
			// Recursively extract the caller previous scenarios
			List< Scenario > previousScenarios = extractPreviousScenarios(
					caller, orderedUseCases, scenarioMap );
			
			// Merge the scenarios
			List< Scenario > mergedScenarios = pushFrontScenarioListCombinator.combine(
					cutScenarios, previousScenarios );
			
			// Add the merged scenarios to the final list
			allPreviousScenarios.addAll( mergedScenarios );
		}
		
		return allPreviousScenarios;
	}
	

	/**
	 * Return a step list with all the steps before the call to the given
	 * use case. If the use case is not called, return an empty list. 
	 * 
	 * @param useCase	the use case
	 * @param steps		the steps to verify
	 * @return
	 */
	private List< Step > stepsBeforeTheUseCaseCall(
			UseCase useCase,
			List< Step > steps
			) {
		List< Step > stepsBefore = new ArrayList< Step >();
		boolean currentUseCaseIsCalled = false;
		for ( Step step : steps ) {
			
			if ( step.kind() == StepKind.USE_CASE_CALL ) {
				UseCaseCallStep uccStep = (UseCaseCallStep) step;
				if ( uccStep.getUseCase().equals( useCase ) ) {
					currentUseCaseIsCalled = true;
					break;
				}
			}
			
			stepsBefore.add( step );
		}
		
		if ( ! currentUseCaseIsCalled ) {
			return new ArrayList< Step >();
		}
		
		return stepsBefore;
	}

	/**
	 * Return a list of use cases that call the given use case.
	 * 
	 * @param useCase			the called use case
	 * @param orderedUseCases	the use cases to verify
	 * @return					a list of use cases
	 */
	private List< UseCase > useCasesThatCall(
			final UseCase useCase,
			final List< UseCase > orderedUseCases
			) {
		List< UseCase > useCases = new ArrayList< UseCase >();
		boolean canBreak;
		for ( UseCase uc : orderedUseCases ) {
			canBreak = false;
			for ( Flow flow : uc.getFlows() ) {
				for ( Step step : flow.getSteps() ) {
					if ( step.kind() == StepKind.USE_CASE_CALL ) {
						UseCaseCallStep uccStep = (UseCaseCallStep) step;
						if ( useCase.equals( uccStep.getUseCase() ) ) {
							useCases.add( uc );
							canBreak = true;
							break; // Exit step loop
						}
					}
				} // for step
				if ( canBreak ) { break; } // Exit flow loop
			} // for flow
		}
		return useCases;
	}

	/**
	 * Filter scenarios that could have a successful end.
	 * 
	 * @param scenarios The scenarios to filter.
	 * @return			Only those that can have a successful end.
	 */
	private List< Scenario > scenariosWithSuccessfulEnd(
			List< Scenario > scenarios) {
		List< Scenario > subList = new ArrayList< Scenario >();
		for ( Scenario s : scenarios ) {
			if ( s.couldHaveASuccessfulEnd() ) {
				subList.add( s );
			}
		}
		return subList;
	}
	
	/**
	 * Filter scenarios that could call any use case.
	 *  
	 * @param scenarios The scenarios to filter.
	 * @return			Only those that call any use case.
	 */
	private List< Scenario > scenariosThatCallsUseCases(List< Scenario > scenarios) {
		List< Scenario > subList = new ArrayList< Scenario >();
		for ( Scenario s : scenarios ) {
			if ( s.callAnyUseCase() ) {
				logger.debug( "Scenario that call use cases: " + s.getName() );
				subList.add( s );
			}
		}
		return subList;
	}
	
}
