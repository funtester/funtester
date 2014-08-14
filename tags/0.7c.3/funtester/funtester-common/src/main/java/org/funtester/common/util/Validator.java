package org.funtester.common.util;

/**
 * A simple validator.
 * 
 * @author Thiago Delgado Pinto
 *
 * @param <T>	The type to be validated.
 */
public interface Validator< T > {

	/**
	 * Validates a object. Throws an exception if the object has some invalid
	 * value.
	 * 
	 * @param obj	the object to be validated. 
	 * @throws Exception
	 */
	void validate(final T obj) throws Exception;
	
}
