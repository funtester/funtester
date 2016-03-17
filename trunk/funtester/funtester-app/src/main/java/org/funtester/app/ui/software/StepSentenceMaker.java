package org.funtester.app.ui.software;

import org.funtester.app.i18n.Messages;
import org.funtester.core.profile.StepKind;
import org.funtester.core.software.NewStep;
import org.funtester.core.software.Step;
import org.funtester.core.vocabulary.ActionNickname;

/**
 * Make a sentence for a {@link Step}.
 *
 * @author Thiago Delgado Pinto
 *
 */
public class StepSentenceMaker {

	public String makeSentence(final Step aStep) {

		if ( ! ( aStep instanceof NewStep ) ) {
			return aStep.asSentence();
		}

		NewStep step = (NewStep) aStep;

		if ( StepKind.DOC == step.kind() ) {
			return step.valuesAsText();
		}

		ActionNickname nickname = step.getActionNickname();
		if ( null == nickname || null == nickname.getAction() ) { return ""; }

		switch ( nickname.getAction().getType() ) {
			case USE_CASE_CALL:
				return step.getUseCase().getName();
			case PURE:
				return step.getActionNickname().getNickname();
			case BUSINESS_RULE_CHECK:
				; // continue
			case WIDGET:
				return step.getActionNickname().actionName()
					+ " " + step.elementsAsText();
			case VALUE:
				return step.getActionNickname().actionName()
					+ " " + step.valuesAsText();
			case PROPERTY: {
				String to = Messages.alt( "_TO", "to" );
				return step.getActionNickname().actionName()
					+ " " + step.elementsAsText()
					+ " " + to + " " + step.valuesAsText()
					;
			}
			default: return "";
		}
	}


}
