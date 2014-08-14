package org.funtester.common.util.rand;

import java.util.Random;

/**
 * Long random value generator.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class LongRandom {
	
	/**
	 * Generates a random number between a minimum and a maximum value, both
	 * inclusive.
	 * 
	 * @param min	The minimum value (inclusive).
	 * @param max	The maximum value (inclusive).
	 * @return		A number between the minimum and the maximum.
	 */
	public Long between(Long min, Long max) {
		long dif = max - min;
        float number = ( new Random() ).nextFloat(); // 0 <= number < 1
        return min + Math.round( number * dif );
	}

	/**
	 * Generates a random value before a maximum value.
	 * 
	 * @param max	The maximum value.
	 * @return		A random value before the maximum value.
	 */
	public Long before(Long max) {
		// Used Integer.MIN_VALUE instead of Long.MIN_VALUE to not underflow
		return between( (long) Integer.MIN_VALUE, max - 1 );
	}

	/**
	 * Generates a random value after a minimum value.
	 * 
	 * @param min	The minimum value.
	 * @return		A random value after the minimum value.
	 */	
	public Long after(Long min) {
		// Used Integer.MAX_VALUE instead of Long.MAX_VALUE to not overflow		
		return between( min + 1, (long) Integer.MAX_VALUE );
	}

}
