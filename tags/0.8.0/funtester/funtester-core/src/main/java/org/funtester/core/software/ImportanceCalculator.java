package org.funtester.core.software;

import java.util.Collection;

import org.funtester.common.Importance;

/**
 * Calculate an Importance value based on Priority, Complexity and Frequency.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class ImportanceCalculator {
	
	public static final Importance DEFAULT_IMPORTANCE_ON_EMPTY_COLLECTION = Importance.VERY_LOW;
	
	private static final int PRIORITY_WEIGHT = 1;
	private static final int COMPLEXITY_WEIGHT = 2;
	private static final int FREQUENCY_WEIGHT = 4;
	private static final int DIVISOR = 5;

	/**
	 * Calculate the importance value based on priority, complexity and
	 * frequency.
	 *  
	 * @param priority
	 * @param complexity
	 * @param frequency
	 * @return
	 */
	public static Importance calculate(
			final Priority priority,
			final Complexity complexity,
			final Frequency frequency
			) {
		final int p = ordinalOf( priority );
		final int c = ordinalOf( complexity );
		final int f = ordinalOf( frequency );
		final int i = Math.round(
				( p * PRIORITY_WEIGHT +
				  c * COMPLEXITY_WEIGHT +
				  f * FREQUENCY_WEIGHT ) / DIVISOR ); // 1 .. 5 
		return Importance.values()[ i - 1 ];
	}
	
	/**
	 * Returns the ordinal value from a enumerated value plus one.
	 * @param	v the enumerated value.
	 * @return	the original ordinal value plus one.
	 */
	public static int ordinalOf(final Enum< ? > v) {
		return 1 + v.ordinal();
	}

	/**
	 * Calculate the average importance of a collection of values.
	 * @param values
	 * @return
	 */
	public static Importance averageImportance(Collection< Importance > values) {
		if ( values.isEmpty() ) {
			return DEFAULT_IMPORTANCE_ON_EMPTY_COLLECTION;
		}
		int sum = 0;
		for ( Importance importance : values ) {
			sum += 1 + importance.ordinal();
		}
		int avg = Math.round( sum / values.size() );
		return Importance.values()[ avg - 1 ];
	}
}
