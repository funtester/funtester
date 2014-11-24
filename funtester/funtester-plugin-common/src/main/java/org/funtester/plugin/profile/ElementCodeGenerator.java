package org.funtester.plugin.profile;

import org.funtester.common.at.AbstractTestElement;

/**
 * Element code generator
 *
 * @author Thiago Delgado Pinto
 */
public interface ElementCodeGenerator {

	/**
	 * Return {@code true} weather the element has the given name.
	 *
	 * @see		file unified.fp
	 * @param	value
	 * @return
	 */
	boolean is(final String value);

	// TEMPORARY DEFINITION !!!
	String generateCode(
			final boolean isOracle,
			final ActionRecognizer actionChecker,
			final String actionName,
			final AbstractTestElement e
			);

}
