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
 * Mutant that changes the returning step.
 *
 * @author Thiago Delgado Pinto
 *
 */
public class ChangeReturningStepSpecMutator implements SpecMutator {

	/** @inheritDoc */
	@Override
	public String mnemonic() {
		return "FM-CS";
	}

	/** @inheritDoc */
	@Override
	public boolean mutate(UseCase useCase) {

		// No flows -> exit
		int numberOfFlows = useCase.getFlows().size();
		if ( numberOfFlows < 1 ) { return false; }

		// Select the returnable flows
		List< Flow > returnableFlows = new ArrayList< Flow >();
		for ( Flow flow : useCase.getFlows() ) {
			if ( flow.type() == FlowType.RETURNABLE ) {
				returnableFlows.add( flow );
			}
		}

		// No returnable flows -> exit
		int numberOfReturnableFlows = returnableFlows.size();
		if ( numberOfReturnableFlows < 1 ) { return false; }

		// Choose a returnable flow randomly
		LongRandom rand = new LongRandom();
		int rfIndex = rand.between( 0L, numberOfReturnableFlows - 1L ).intValue();
		ReturnableFlow rFlow = (ReturnableFlow) returnableFlows.get( rfIndex );

		// Get the current returning flow
		Flow returningFlow = rFlow.getReturningFlow();

		// Just one step -> exit
		int numberOfSteps = returningFlow.numberOfSteps();
		if ( numberOfSteps <= 1 ) { return false; }

		// Choose a step index greater than the current index (randomly)
		int stepIndex = rFlow.getReturningStep().index();
		int newStepIndex = rand.between( (long) stepIndex, numberOfSteps - 1L ).intValue();

		// Change to the new step
		Step newReturningStep = rFlow.stepAt( newStepIndex );
		rFlow.setReturningStep( newReturningStep );

		return true;
	}

}
