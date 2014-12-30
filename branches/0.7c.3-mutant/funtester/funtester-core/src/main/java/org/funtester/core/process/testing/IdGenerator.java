package org.funtester.core.process.testing;

/**
 * Id generator for classes that need different identifications, like
 * flow steps or semantic steps.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public interface IdGenerator {

	/**
	 * Generates the id.
	 * @return the generated id.
	 */
	long generate();

}