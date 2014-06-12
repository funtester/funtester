package org.funtester.core.process.value;

import org.funtester.common.util.rand.LongRandom;
import org.funtester.core.software.ValueType;

/**
 * Value generator for {@link ValueType#INTEGER} elements.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class LongValueGenerator extends DefaultValueGenerator< Long > {
		
	public static final long MIN_VALUE = Long.MIN_VALUE;
	public static final long MAX_VALUE = Long.MAX_VALUE;
	
	private final long min;
	private final long max;
	private final LongRandom random = new LongRandom();

	// Long.MAX_VALUE is  2^63 -1	=  9223372036854775807
	// Long.MIN_VALUE is -2^63		= -9223372036854775808
	// The diff is					= -1
	
	/**
	 * Constructs with a minimum and a maximum value.
	 * 
	 * @param min	Minimum value. Assumes <code>Long.MIN_VALUE</code> if <code>null</code>.
	 * @param max	Maximum value. Assumes <code>Long.MAX_VALUE</code> if <code>null</code>.
	 * @throws IllegalArgumentException if min > max.
	 */	
	public LongValueGenerator(Long min, Long max) {
		this.min = ( null == min ) ? MIN_VALUE : min;
		this.max = ( null == max ) ? MAX_VALUE : max;
		if ( this.min > this.max ) {
			throw new IllegalArgumentException( "min can't be greater than max." );
		}
	}
	
	@Override
	public boolean hasAvailableValuesOutOfTheRange() {		
		return min > MIN_VALUE || max < MAX_VALUE;		
	}

	@Override
	public Long min() {
		return min;
	}

	@Override
	public Long max() {
		return max;
	}

	@Override
	protected Long zero() {
		return ( 0L >= min && 0L <= max ) ? 0L : min;
	}

	@Override
	protected Long middle() {
		return min + ( ( max - min ) / 2 );
	}
	
	@Override
	protected boolean hasNext(Long value) {	
		return value < max;
	}	

	@Override
	protected Long next(Long value) {
		return value + 1;
	}
	
	@Override
	protected boolean hasPrior(Long value) {
		return min < value;
	}

	@Override
	protected Long prior(Long value) {
		return value - 1;
	}

	@Override
	protected Long randomBefore(Long value) {
		return random.before( value );
	}

	@Override
	protected Long randomAfter(Long value) {
		return random.after( value );
	}
	
	@Override
	protected boolean hasAnyValueBetween(Long min, Long max) {
		return ( ( max - min ) > 0 );
	}

	@Override
	protected Long randomBetween(Long min, Long max) {
		return random.between( min, max );
	}
}