package org.funtester.core.process.value;

import org.funtester.common.util.rand.DoubleRandom;
import org.funtester.core.software.ValueType;

/**
 * Value generator for {@link ValueType#DOUBLE} element.
 * <p>
 * It was used <code>java.lang.Double</code> to represent float point values.
 * Instead of using <code>Double.MIN_VALUE</code> as the minimum float point
 * valid value, it was used <code>-Double.MAX_VALUE</code> because it is lesser
 * than the first one (without underflow).
 *  
 * @see	<a href="http://books.google.com.br/books?id=nfiRsXvQAj4C&printsec=frontcover" >
   A Programmer's Guide to Java Certification, Second Edition</a> Page 56  
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class DoubleValueGenerator extends DefaultValueGenerator< Double > {
			// TODO Would it be better using BigDecimal instead of Double ?
	
	/**
	 * Used <code>-Double.MAX_VALUE</code> instead of <code>Double.MIN_VALUE</code>.
	 */
	public final static double MIN_VALUE = Double.MAX_VALUE * -1;
	public final static double MAX_VALUE = Double.MAX_VALUE;
	
	
	public final static double DEFAULT_DELTA = 0.01;
	
	private final double min;
	private final double max;
	private final double delta;
	private final DoubleRandom random = new DoubleRandom();
	
	// Double.MAX_VALUE is (2- 2^-52)* 2^1023	= 1.7976931348623157e308
	// Double.MIN_VALUE is 2^-1074				= 4.9406564584124654417656879286822e-324
	// diff is									= 
	
	
	/**
	 * Constructs with a minimum value, a maximum value and a delta value.
	 * 
	 * @param min	Minimum value. Assumes <code>Double.MIN_VALUE</code> if <code>null</code>.
	 * @param max	Maximum value. Assumes <code>Double.MAX_VALUE</code> if <code>null</code>.
	 * @param delta Precision used to generate new values.
	 */		
	public DoubleValueGenerator(
			final Double min,
			final Double max,
			final double delta
			) {
		checkValues( min, max, delta ); // can throw IllegalArgumentException
	
		this.min = ( null == min ) ? MIN_VALUE : min;
		this.max = ( null == max ) ? MAX_VALUE : max;
		this.delta = delta;
		
		if ( this.min > this.max ) {
			throw new IllegalArgumentException( "min can't be greater than max." );
		}
	}


	private void checkValues(final Double min, final Double max,
			final double delta) {
		// min
		if ( min != null ) {
			if ( min.isNaN() ) {
				throw new IllegalArgumentException( "min is NaN." );
			}
			if ( min.isInfinite() ) {
				throw new IllegalArgumentException( "min is Infinite." );
			}
		}
		// max
		if ( max != null ) {			
			if ( max.isNaN() ) {
				throw new IllegalArgumentException( "max is NaN." );
			}
			if ( max.isInfinite() ) {
				throw new IllegalArgumentException( "max is Infinite." );
			}
		}
		// delta
		if ( delta < 0 ) {
			throw new IllegalArgumentException( "delta can't be negative." );
		}
	}
	

	/**
	 * Constructs with a minimum and a maximum value. Calculates delta as the
	 * greatest rest from min and max.
	 * 
	 * @param min	Minimum value. Can be null (assumed Double.MIN_VALUE + delta).
	 * @param max	Maximum value. Can be null (assumed Double.MIN_VALUE - delta).
	 */	
	public DoubleValueGenerator(
			final Double min,
			final Double max
			) {
		this( min, max, greatestFractionalPart( min, max ) );
	}
	
	/**
	 * Return the greatest fractional part.
	 * 
	 * @param min	The minimum value.
	 * @param max	The maximum value.
	 */
	private static double greatestFractionalPart(Double min, Double max) {
		if ( null == min && null == max ) { // both are null
			return DEFAULT_DELTA;
		}
		String minStr = ( min != null ) ? min.toString() : "";
		String maxStr = ( max != null ) ? max.toString() : "";
		String restStr = minStr.length() > maxStr.length() ? minStr : maxStr;
		int restLen = restStr.length() - "0.".length();
		double frac = 1d / Math.pow( 10, restLen );
		return frac;
	}
	
	public double delta() {
		return delta;
	}

	@Override
	public boolean hasAvailableValuesOutOfTheRange() {
		return min > MIN_VALUE || max < MAX_VALUE;
	}	

	@Override
	public Double min() {
		return min;
	}

	@Override
	public Double max() {
		return max;
	}

	@Override
	protected Double zero() {
		return ( 0d >= min && 0d <= max ) ? 0d : min;
	}

	@Override
	protected Double middle() {
		return min + ( ( max - min ) / 2 );
	}

	@Override
	protected boolean hasNext(Double value) {
		return value < max;
	}
	
	@Override
	protected Double next(Double value) {
		return value + delta;
	}

	@Override
	protected boolean hasPrior(Double value) {
		return min < value; 
	}
	
	@Override
	protected Double prior(Double value) {
		return value - delta;
	}

	@Override
	protected Double randomBefore(Double value) {
		return random.before( value, delta );
	}

	@Override
	protected Double randomAfter(Double value) {
		return random.after( value, delta );
	}

	@Override
	protected boolean hasAnyValueBetween(Double min, Double max) {
		return ( max - min ) > delta;
	}
	
	@Override
	protected Double randomBetween(Double min, Double max) {
		return random.between( min, max );
	}
}