package org.funtester.app.project;

import org.funtester.core.software.Software;

/**
 * Generate documentation for a software.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public interface DocGenerator {

	/**
	 * Generate the documentation.
	 * 
	 * @param s	the software used to generate the documentation.
	 * @throws DocException
	 */
	void generate(final Software s) throws DocGenerationException;
}
