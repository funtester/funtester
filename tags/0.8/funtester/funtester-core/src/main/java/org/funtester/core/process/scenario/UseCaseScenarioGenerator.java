package org.funtester.core.process.scenario;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.funtester.core.software.Flow;
import org.funtester.core.software.FlowInfo;
import org.funtester.core.software.ReturnableFlow;
import org.funtester.core.software.Scenario;
import org.funtester.core.software.Step;
import org.funtester.core.software.TerminableFlow;
import org.funtester.core.software.UseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generate scenarios for an {@code UseCase}.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class UseCaseScenarioGenerator {

	private static final Logger logger = LoggerFactory.getLogger( UseCaseScenarioGenerator.class );
	
	/**
	 * Generate scenarios for the supplied flow.
	 * 
	 * @param flow [IN]
	 * 		Target flow. Should not be null.
	 * @param flows [IN]
	 * 		Flows where the target flow exists. Could be null.
	 * @param maxFlowRepetition [IN]
	 * 		Max flow repetitions. Should be a natural number.
	 * @param useCase [IN]
	 * 		Use case that will be associated to the generated scenarios. Could
	 * 		be null but its not recommended.
	 * @param scenarios [OUT]
	 * 		Generated scenarios. Should not be null.
	 */	
	public List< Scenario > generate(
			Flow flow,
			List< Flow > flows,
			int maxFlowRepetitions,
			UseCase useCase
			) {
		List< Scenario > scenarios = new ArrayList< Scenario >();
		logger.debug( "$ STARTED" );
		buildScenarios( flows, useCase, flow, maxFlowRepetitions, null, null, scenarios, null );
		logger.debug( "$ FINISHED" );
		return scenarios;
	}
	
	private void buildScenarios(
			List< Flow > flows,
			UseCase useCase,
			Flow flow,
			int maxFlowRepetitions,
			Step currentStep,
			Scenario currentScenario,
			List< Scenario > scenarios,
			Flow lastFlow
			) {
		if ( null == flow ) {
			throw new IllegalArgumentException( "flow cannot be null" );
		}
		if ( null == scenarios ) {
			throw new IllegalArgumentException( "scenarios cannot be null" );
		}
		List< Step > steps = flow.getSteps();
		
		// Starts at supplied index if it is not null OR at zero otherwise 
		int currentStepIndex = 0;
		if ( currentStep != null ) {
			currentStepIndex = steps.indexOf( currentStep );
			if ( currentStepIndex < 0 ) {
				currentStepIndex = 0;
			}
		}		
		int lastStepIndex = steps.size() - 1;
		
		if ( null == currentScenario ) {
			currentScenario = ( new Scenario() )
					.withUseCase( useCase )
					.addIncludeFiles( useCase.getIncludeFiles() )
					;
		}		
				
		StringBuilder sb = new StringBuilder();		
		if ( ! currentScenario.getName().isEmpty() ) {
			sb.append( currentScenario.getName() );
			sb.append( "," );
		}
		//sb.append( flow.getUseCase().shortName() );
		//sb.append( "-" );
		sb.append( flow.shortName() );		
		currentScenario.setName( sb.toString() );
		sb = null; // force delete
		
		
		logger.debug( "$ SCENARIO is [" + currentScenario.getName() + " @ " + System.identityHashCode( currentScenario ) +
				"] and FLOW is [" + flow.shortName() + " @ " + System.identityHashCode( flow ) + " ]" +
				"] and STEP is [" + ( currentStep != null ? currentStep.shortName() + " @ " + System.identityHashCode( currentStep ) : "null" )
				+ " ] at index " + currentStepIndex +
				" (scenario list @ " + System.identityHashCode( scenarios ) + ")" );
		
		
		// From the current step to the last one...			
		for ( int index = currentStepIndex; ( index <= lastStepIndex ); ++index ) {			
			Step step = steps.get( index );	
			
			Collection< Flow > flowsLeaving = FlowInfo.flowsLeavingTheStep( flows, step );
			if ( ! flowsLeaving.isEmpty() ) {	
				logger.debug( "$\t" + step.shortName() + " has " + flowsLeaving.size() + " flows leaving it." );
				for ( Flow f : flowsLeaving ) {
					
					// Prevent from entering the same flow again
					if ( f == lastFlow ) {
						continue;
					}
					
					// ---------------------------------------------------------
					if ( f instanceof ReturnableFlow ) {
						ReturnableFlow rf = (ReturnableFlow) f;
						if ( rf.isRecursive() ) {
							logger.debug( "$ is recursive" );
							// Using the returning step for counting because the
							// starter step will not make part of the scenario.
							Step returningStep = rf.getReturningStep();
							int count = currentScenario.countForStep( returningStep );
							if ( count > maxFlowRepetitions ) {
								logger.debug( "$ ignored because is greater than maxFlowRepetitions" );
								continue; // IGNORE FLOW
							}
						}
					}
					// ---------------------------------------------------------
					
					logger.debug( "$\t\t" + f.shortName() + " is leaving " + step.shortName() );
					Scenario flowScenario = currentScenario.newCopy();
					logger.debug( "$\t\tFlowScenario ["
							+ flowScenario.getName()
							+ " @ " + System.identityHashCode( flowScenario )
							+ "] copy from scenario [" +  currentScenario.getName()
							+ " @ " + System.identityHashCode( currentScenario )
							+ "]"  );
					// Starts a new scenario including all steps until the current step
					buildScenarios( flows, useCase, f, maxFlowRepetitions, null, flowScenario, scenarios, flow );					
				}
			}
			
			// Always add a step for the current scenario
			currentScenario.addStep( step );
			logger.debug( "$\t" + step.shortName()
					+ " added [" + currentScenario.getName()
					+ " @ " + System.identityHashCode( currentScenario )
					+ "] = " + step.asSentence() );
		}		
		
		
		if ( flow instanceof TerminableFlow ) {
			logger.debug( "$ finish scenario in " + flow.shortName() );
			scenarios.add( currentScenario );
			currentScenario = null;
		} else if ( flow instanceof ReturnableFlow ) {
			ReturnableFlow rf = (ReturnableFlow) flow;
			logger.debug( "$ \t will return to flow " + rf.getReturningFlow() );
			logger.debug( "$ --> returningStep " + rf.getReturningStep()
					+ " @ " + System.identityHashCode( rf.getReturningStep() )
					+ " is \"" + rf.getReturningStep().asSentence() + "\"" );
			
			buildScenarios(
					flows,
					useCase,
					rf.getReturningFlow(),
					maxFlowRepetitions,
					rf.getReturningStep(),
					currentScenario,
					scenarios,
					flow
					);
			logger.debug( "$ after returnable flow " + rf.shortName() );
		}
	}	

}
