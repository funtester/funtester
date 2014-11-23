package org.funtester.plugin.profile;

import org.funtester.common.at.AbstractTestStep;

/**
 * Code generator
 *
 * @author Thiago Delgado Pinto
 *
 */
public class CodeGenerator {

	private StepCodeGenerator stepCodeGenerators[];

	public CodeGenerator(final StepCodeGenerator ... stepCodeGenerators) {
		this.stepCodeGenerators = stepCodeGenerators;
	}

	public String generateCode(final AbstractTestStep step) {

		StringBuilder sb = new StringBuilder();

		for ( StepCodeGenerator p : stepCodeGenerators ) {
			if ( p.canGenerateCodeTo( step ) ) {
				sb.append( p.generateCode( step ) );
			}
		}

		return sb.toString();
	}
}
