package org.funtester.common.util.rand;

import java.util.Random;

/**
 * Double random value generator.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class DoubleRandom {
	
	// Used a really big negative number. Float.MIN_VALUE is giving an error.
	public static final double MIN = -9E307;
	
	public static final double MAX = (double) Float.MAX_VALUE;

	/**
	 * Generates a random number between a minimum and a maximum value, both
	 * inclusive.
	 * 
	 * @param min	The minimum value (inclusive).
	 * @param max	The maximum value (inclusive).
	 * @return		A number between the minimum and the maximum.
	 */
	public Double between(Double min, Double max) {		
        double number = ( new Random() ).nextDouble(); // 0d <= number < 1d        
        return min + ( number * (max - min) );          										
	}
	
	/**
	 * Generates a random value before a maximum value.
	 * 
	 * @param max	The maximum value.
	 * @return		A random value before the maximum value.
	 */
	public Double before(Double value, Double delta) {
		// Used Float.MIN_VALUE instead of Double.MIN_VALUE to not underflow
		return between( MIN, value - delta );
	}

	/**
	 * Generates a random value after a minimum value.
	 * 
	 * @param min	The minimum value.
	 * @return		A random value after the minimum value.
	 */
	public Double after(Double value, Double delta) {
		// Used Float.MAX_VALUE instead of Double.MAX_VALUE to not overflow		
		return between( value + delta, (double) Float.MAX_VALUE );
	}	

}
