package org.funtester.core.process.value;

/**
 * Value generator
 * 
 * @author Thiago Delgado Pinto
 *
 * @param <T>	Value type
 */
public interface ValueGenerator< T > {
		
	/**
	 * Analyzes if there are available values out of the range. This can be
	 * used to determine if the value configuration is able to generate invalid
	 * values.
	 */
	boolean hasAvailableValuesOutOfTheRange();

	/**
	 * Generates a valid value according to the desired option.
	 * 
	 * @param option	The desired option.
	 * @return			A valid value.
	 */
	T validValue(final ValidValueOption option);

	/**
	 * Generates a invalid value according to the desired option.
	 * 
	 * @param option	The desired option.
	 * @return			A invalid value.
	 */	
	T invalidValue(final InvalidValueOption option);	
}
