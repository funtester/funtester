package org.funtester.plugin.profile;

import org.funtester.common.at.AbstractTestStep;

/**
 * Comment generator
 *
 * @author Thiago Delgado Pinto
 *
 */
public class CommentGenerator implements StepCodeGenerator {

	@Override
	public boolean canGenerateCodeTo(final AbstractTestStep step) {
		return true;
	}

	@Override
	public String generateCode(final AbstractTestStep step) {
		return "// id=" + step.getId()+ "|" +  step.getStepId() + "";
	}

}
