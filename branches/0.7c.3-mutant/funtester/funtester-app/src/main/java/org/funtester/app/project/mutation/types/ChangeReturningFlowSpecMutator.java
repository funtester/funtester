package org.funtester.app.project.mutation.types;

import java.util.ArrayList;
import java.util.List;

import org.funtester.app.project.mutation.SpecMutator;
import org.funtester.common.util.rand.LongRandom;
import org.funtester.core.software.Flow;
import org.funtester.core.software.FlowType;
import org.funtester.core.software.ReturnableFlow;
import org.funtester.core.software.Step;
import org.funtester.core.software.UseCase;

/**
 * Mutant that changes the returning flow.
 *
 * @author Thiago Delgado Pinto
 *
 */
public class ChangeReturningFlowSpecMutator implements SpecMutator {

	/** @inheritDoc */
	@Override
	public String mnemonic() {
		return "FM-CF";
	}

	/** @inheritDoc */
	@Override
	public boolean mutate(UseCase useCase) {

		// No flow -> exit
		int numberOfFlows = useCase.getFlows().size();
		if ( numberOfFlows < 1 ) { return false; }

		// Choose the returnable flows
		List< Flow > returnableFlows = new ArrayList< Flow >();
		for ( Flow flow : useCase.getFlows() ) {
			if ( flow.type() == FlowType.RETURNABLE ) {
				returnableFlows.add( flow );
			}
		}

		// No returnable flows -> exit
		int numberOfReturnableFlows = returnableFlows.size();
		if ( numberOfReturnableFlows < 1 ) { return false; }

		// Choose a random returnable flow
		LongRandom rand = new LongRandom();
		int rfIndex = rand.between( 0L, numberOfReturnableFlows - 1L ).intValue();
		ReturnableFlow rFlow = (ReturnableFlow) returnableFlows.get( rfIndex );

		// Choose a different returning flow (randomly)
		Flow returningFlow = rFlow.getReturningFlow();
		Flow newReturningFlow;
		do {
			int flowIndex = rand.between( 0L, numberOfFlows - 1L ).intValue();
			newReturningFlow = useCase.flowAt( flowIndex );
		} while ( newReturningFlow.equals( returningFlow ) );

		// No steps -> exit
		int numberOfSteps = newReturningFlow.numberOfSteps();
		if ( numberOfSteps < 1 ) { return false; }

		// Choose a returning step randomly
		int stepIndex = rand.between( 0L, numberOfSteps - 1L ).intValue();
		Step newReturningStep = newReturningFlow.stepAt( stepIndex );

		// Change the returning flow and returning step
		rFlow.setReturningFlow( newReturningFlow );
		rFlow.setReturningStep( newReturningStep );

		return true;
	}

}
