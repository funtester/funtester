package org.funtester.plugin.profile;

import org.funtester.common.at.AbstractTestStep;

/**
 * Step code generator
 *
 * @author Thiago Delgado Pinto
 *
 */
public interface StepCodeGenerator {

	boolean canGenerateCodeTo(final AbstractTestStep step);

	String generateCode(final AbstractTestStep step);

}
