package org.funtester.core.process.value;

import org.funtester.common.util.rand.StringRandom;

/**
 * String value generator
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class StringValueGenerator implements ValueGenerator< String > {
	
	public static final int MIN_LENGTH		= 0;	
	public static final int MAX_LENGTH		= Short.MAX_VALUE;	// 32767
	public static final int DEFAULT_LENGTH	= Byte.MAX_VALUE;	// 127

	private final int min;
	private final int max;
	private final StringRandom stringRandomGenerator = new StringRandom();
	
	/**
	 * Constructs with a minimum and a maximum value for length.
	 * 
	 * @param min	Minimum value. Assumes <code>MIN_LENGTH</code> if <code>null</code>.
	 * @param max	Maximum value. Assumes <code>DEFAULT_LENGTH</code> if <code>null</code>.
	 */		
	public StringValueGenerator(
			final Integer min,
			final Integer max
			) {		
		int aMin = ( null == min ) ? MIN_LENGTH : min;
		int aMax = ( null == max ) ? DEFAULT_LENGTH : max;
		checkValues( aMin, aMax );
		aMin = normalizeBetween( aMin, MIN_LENGTH, MAX_LENGTH );
		aMax = normalizeBetween( aMax, aMin, MAX_LENGTH );		
		this.min = aMin;
		this.max = aMax;	
	}
	
	private int normalizeBetween(final int value, final int aMin, final int aMax) {
		if ( value > aMax ) return aMax;
		if ( value < aMin ) return aMin;		
		return value;
	}

	private void checkValues(final int min, final int max) {
		if ( min < MIN_LENGTH ) {
			throw new IllegalArgumentException( "min should be >= 0." );
		}
		if ( max < MIN_LENGTH ) {
			throw new IllegalArgumentException( "max should be >= 0." );			
		}
		if ( max < min ) {
			throw new IllegalArgumentException( "max should not be lesser than min." );
		}
	}
	
	public boolean hasAvailableValuesOutOfTheRange() {
		return min > MIN_LENGTH || max < MAX_LENGTH;
	}

	public String validValue(ValidValueOption option) {		
		if ( ValidValueOption.ZERO == option ) {
			if ( min > 0 ) {
				return between( min, min );
			}
			return "";		
		} else if ( ValidValueOption.MIN == option ) {
			if ( min <= 0 ) {
				return "";
			}
			return exactly( min );
		} else if ( ValidValueOption.MAX == option ) {
			return exactly( max );
		} else if ( ValidValueOption.MEDIAN == option ) {
			return exactly( middle() ); 
		} else if ( ValidValueOption.RIGHT_AFTER_MIN == option ) {
			if ( min == max ) {
				return exactly( max );
			}
			return exactly( min + 1 );
		} else if ( ValidValueOption.RIGHT_BEFORE_MAX == option ) {
			if ( min == max ) {
				return exactly( min );
			}			
			return exactly( max - 1 );
		} else if ( ValidValueOption.RANDOM_INSIDE_RANGE == option ) {
			return between( min, max );
		}
		return null;
	}
	
	public String invalidValue(InvalidValueOption option) {
		if ( ! hasAvailableValuesOutOfTheRange() ) {
			return null;
		}
		if ( InvalidValueOption.RIGHT_BEFORE_MIN == option ) {
			return ( min > MIN_LENGTH ) ? exactly( min - 1 ) : "";			
		} else if ( InvalidValueOption.RIGHT_AFTER_MAX == option ) {				
			return ( max < MAX_LENGTH ) ? exactly( max + 1 ) : exactly( MAX_LENGTH );			
		} else if ( InvalidValueOption.RANDOM_BEFORE_MIN == option ) {
			return ( min > MIN_LENGTH ) ? between( MIN_LENGTH, min - 1 ) : ""; 
		} else if ( InvalidValueOption.RANDOM_AFTER_MAX == option ) {			
			if ( max < MAX_LENGTH ) {
				if ( max < DEFAULT_LENGTH ) { // Optimization
					return between( max + 1, DEFAULT_LENGTH );
				}
				return between( max + 1, MAX_LENGTH );
			}			
			return exactly( MAX_LENGTH );
		}
		return null;
	}
		
	public int min() {
		return min;
	}
	
	public int max() {
		return max;
	}
	
	private int middle() {
		return min + ( ( max - min ) / 2 );
	}
	
	private String exactly(int size) {		
		return stringRandomGenerator.exactly( size );
	}
	
	private String between(final int aMin, final int aMax) {		
		return stringRandomGenerator.between( aMin, aMax );
	}	
}