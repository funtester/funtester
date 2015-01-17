package org.funtester.app.project.mutation.types;

import org.funtester.app.project.mutation.SpecMutator;
import org.funtester.common.util.rand.LongRandom;
import org.funtester.core.software.Flow;
import org.funtester.core.software.UseCase;

/**
 * Mutant that removes a mandatory step.
 *
 * @author Thiago Delgado Pinto
 *
 */
public class RemoveMandatoryStepSpecMutator implements SpecMutator {

	/** @inheritDoc */
	@Override
	public String mnemonic() {
		return "SM-RM";
	}

	/** @inheritDoc */
	@Override
	public boolean mutate(UseCase useCase) {

		// No flows -> exit
		int numberOfFlows = useCase.getFlows().size();
		if ( numberOfFlows < 1 ) { return false; }

		// Choose the basic flow
		Flow flow = useCase.getFlows().get( 0 ); // Basic flow

		// No steps -> exit
		int numberOfSteps = flow.getSteps().size();
		if ( numberOfSteps < 1 ) { return false; }

		// Choose a random step
		LongRandom rand = new LongRandom();
		int index = rand.between( 0L, numberOfSteps - 1L ).intValue();

		// Remove the step
		flow.removeStepAt( index );

		return true;
	}

}
