package org.funtester.plugin.profile;

import org.funtester.common.at.AbstractTestActionStep;
import org.funtester.common.at.AbstractTestElement;
import org.funtester.common.at.AbstractTestStep;
import org.funtester.common.at.StepType;

public class ActionStepCodeGenerator implements StepCodeGenerator {

	private final ActionChecker actionChecker;
	private final Finder actionFinder;
	private final Finder elementFinder;


	public ActionStepCodeGenerator(
			ActionChecker actionChecker,
			Finder actionFinder,
			Finder elementFinder
			) {
		this.actionChecker = actionChecker;
		this.actionFinder = actionFinder;
		this.elementFinder = elementFinder;
	}

	@Override
	public boolean canGenerateCodeTo(AbstractTestStep step) {
		return StepType.ACTION == step.stepType();
	}

	@Override
	public String generateCode(AbstractTestStep step) {

		AbstractTestActionStep actionStep = (AbstractTestActionStep) step;
		if ( null == actionStep ) { return ""; }

		StringBuilder sb = new StringBuilder();

		final String actionName = actionStep.getActionName();

		ActionCodeGenerator actionCG = (ActionCodeGenerator)
				actionFinder.find( actionName );
		if ( actionCG != null ) {
			sb.append( actionCG.generateCode( actionStep.getElements() ) );
		}

		for ( AbstractTestElement e : actionStep.getElements() ) {

			// IMPORTANT: It should be getName(), not getType()
			ElementCodeGenerator el = (ElementCodeGenerator)
					elementFinder.find( e.getType() );
			if ( null == el ) { continue; } // Not found

			String code = el.generateCode( actionChecker, actionName, e );
			sb.append( code );
		}

		return sb.toString();
	}

}
