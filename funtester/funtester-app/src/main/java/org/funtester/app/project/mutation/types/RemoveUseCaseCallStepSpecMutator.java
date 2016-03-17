package org.funtester.app.project.mutation.types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.funtester.app.project.mutation.SpecMutator;
import org.funtester.common.util.rand.LongRandom;
import org.funtester.core.profile.StepKind;
import org.funtester.core.software.Flow;
import org.funtester.core.software.Step;
import org.funtester.core.software.UseCase;

/**
 * Mutant that removes a step that calls a use case.
 *
 * @author Thiago Delgado Pinto
 *
 */
public class RemoveUseCaseCallStepSpecMutator implements SpecMutator {

	/** @inheritDoc */
	@Override
	public String mnemonic() {
		return "SM-RUC";
	}

	/** @inheritDoc */
	@Override
	public boolean mutate(UseCase useCase) {

		// No flows -> exit
		int numberOfFlows = useCase.getFlows().size();
		if ( numberOfFlows < 1 ) {
			return false;
		}

		// Map flow indexes to lists of steps
		Map< Integer, List< Step > > useCaseStepMap = new HashMap< Integer, List< Step > >();

		for ( int i = 0; i < numberOfFlows; ++i ) {
			Flow flow = useCase.getFlows().get( i );
			for ( Step step : flow.getSteps() ) {
				if ( step.kind() == StepKind.USE_CASE_CALL ) {
					List< Step > steps = useCaseStepMap.get( flow );
					if ( null == steps ) { steps = new ArrayList< Step >(); }
					steps.add( step );
					useCaseStepMap.put( i, steps );
				}
			}
		}

		// No steps -> exit
		if ( useCaseStepMap.size() < 1 ) { return false; }

		// Choose a random flow
		LongRandom rand = new LongRandom();
		int flowIndex = rand.between( 1L, numberOfFlows - 1L ).intValue();

		// Choose a random step
		List< Step > stepsThatCallUseCases = useCaseStepMap.get( flowIndex );
		if ( null == stepsThatCallUseCases ) { return false; }
		int numberOfSteps = stepsThatCallUseCases.size();
		if ( numberOfSteps < 1 ) { return false; }
		int stepIndex = rand.between( 0L, numberOfSteps - 1L ).intValue();

		// Remove the step
		Flow flow = useCase.flowAt( flowIndex );
		flow.removeStepAt( stepIndex );

		return true;
	}

}
