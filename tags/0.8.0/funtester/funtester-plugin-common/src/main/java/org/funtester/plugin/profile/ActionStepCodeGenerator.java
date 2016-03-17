package org.funtester.plugin.profile;

import org.funtester.common.at.AbstractTestActionStep;
import org.funtester.common.at.AbstractTestElement;
import org.funtester.common.at.AbstractTestStep;
import org.funtester.common.at.AbstractTestStepKind;

public class ActionStepCodeGenerator implements StepCodeGenerator {

	/** Passed to widgets, so they can recognize actions */
	private final ActionRecognizer actionRecognizer;

	/** Just for actions that do not have relationships with widgets */
	private final Finder actionFinder;

	/** All the used widgets */
	private final Finder widgetFinder;

	/**
	 *
	 * @param actionRecognizer
	 * @param actionFinder	Action finder, containing all the actions that
	 * 						have no widgets.
	 * @param widgetFinder	Widget finder, containing all the widgets.
	 */
	public ActionStepCodeGenerator(
			final ActionRecognizer actionRecognizer,
			final Finder actionFinder,
			final Finder widgetFinder
			) {
		this.actionRecognizer = actionRecognizer;
		this.actionFinder = actionFinder;
		this.widgetFinder = widgetFinder;
	}

	@Override
	public boolean canGenerateCodeTo(AbstractTestStep step) {
		return AbstractTestStepKind.ACTION == step.kind();
	}

	@Override
	public String generateCode(AbstractTestStep step) {

		AbstractTestActionStep actionStep = (AbstractTestActionStep) step;
		// Isn't it an action step? -> goodbye
		if ( null == actionStep ) { return ""; }

		// Let's build a string containing the command
		StringBuilder sb = new StringBuilder();

		final String actionName = actionStep.getActionName();

		// Find the action by name. Does it generate code ? -> Generate it!
		ActionCodeGenerator actionCG = (ActionCodeGenerator) actionFinder.find( actionName );
		if ( actionCG != null ) {
			sb.append( actionCG.generateCode(
					actionStep, actionStep.getElements() ) );
		}

		// If the action has elements, let's ask them to generate code
		for ( AbstractTestElement e : actionStep.getElements() ) {

			// IMPORTANT: It should be getName(), not getType(), but it can be
			//            changed in the future.
			ElementCodeGenerator el = (ElementCodeGenerator)
					widgetFinder.find( e.getType() );
			if ( null == el ) { continue; } // Not found

			// TEMPORARY DEFINITION !!!
			//String code = el.generateCode(
			//		actionRecognizer, actionName, e );
			String code = "NOT WORKING YET!!!";

			sb.append( code );
		}

		return sb.toString();
	}

}
