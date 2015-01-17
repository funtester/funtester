package org.funtester.app.project.mutation.types;

import org.funtester.app.project.mutation.SpecMutator;
import org.funtester.common.util.rand.LongRandom;
import org.funtester.core.software.Flow;
import org.funtester.core.software.UseCase;

/**
 * Mutant that removes a flow.
 *
 * @author Thiago Delgado Pinto
 *
 */
public class RemoveFlowSpecMutator implements SpecMutator {

	/** @inheritDoc */
	@Override
	public String mnemonic() {
		return "FM-RM";
	}

	/** @inheritDoc */
	@Override
	public boolean mutate(UseCase useCase) {

		// No flows -> exit
		int numberOfFlows = useCase.getFlows().size();
		if ( numberOfFlows <= 1 ) { return false; }

		// Choose a random flow
		LongRandom rand = new LongRandom();
		int flowIndex = rand.between( 1L, numberOfFlows - 1L ).intValue();

		// Remove the flow
		Flow flow = useCase.flowAt( flowIndex );
		useCase.removeFlow( flow );

		return true;
	}

}
