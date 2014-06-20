package org.funtester.core.process.value;

import org.funtester.common.util.rand.LongRandom;
import org.funtester.core.software.ValueType;
import org.joda.time.DateTime;
import org.joda.time.Instant;

/**
 * DateTime value generator for {@link ValueType#DATETIME} element.
 * <p>
 * Used seconds as precision. 
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class DateTimeValueGenerator extends DefaultValueGenerator< DateTime > {

	public static final DateTime MIN_VALUE = new DateTime( 1, 1, 1, 00, 00, 00, 000 );				
	public static final DateTime MAX_VALUE = new DateTime( 99999999, 12, 31, 23, 59, 59, 999 );	
	
	private final DateTime min;
	private final DateTime max;
	private final LongRandom random = new LongRandom();

	/**
	 * Constructs with a minimum and a maximum value.
	 * 
	 * @param min	Minimum value. Can be null (assumed MIN_DATE_TIME).
	 * @param max	Maximum value. Can be null (assumed MAX_DATE_TIME).
	 */
	public DateTimeValueGenerator(
			final DateTime min,
			final DateTime max
			) {
		if ( min != null && max != null && min.isAfter( max ) ) {
			throw new IllegalArgumentException( "min should not be greater than max." );
		}
		this.min = ( null == min ) ? MIN_VALUE : min;
		this.max = ( null == max ) ? MAX_VALUE : max;
	}
	
	@Override
	public boolean hasAvailableValuesOutOfTheRange() {
		return min.isAfter( MIN_VALUE ) || max.isBefore( MAX_VALUE );
	}		
	
	@Override
	public DateTime min() {
		return min;
	}

	@Override
	public DateTime max() {
		return max;
	}

	@Override
	protected DateTime zero() {
		return min.isAfter( MIN_VALUE ) ? min : MIN_VALUE;
	}

	@Override
	protected DateTime middle() {
		long minInMillis = toMillis( min );
		long maxInMillis = toMillis( max );
		long middle = minInMillis + ( ( maxInMillis - minInMillis ) / 2 );				
		return fromMillis( middle );
	}

	@Override
	protected boolean hasNext(DateTime value) {		
		return value.plusSeconds( 1 ).isBefore( max );
	}	

	@Override
	protected DateTime next(DateTime value) {
		return value.plusSeconds( 1 );
	}

	@Override
	protected boolean hasPrior(DateTime value) {
		return value.minusSeconds( 1 ).isAfter( min );
	}	

	@Override
	protected DateTime prior(DateTime value) {
		return value.minusSeconds( 1 );
	}

	@Override
	protected DateTime randomBefore(DateTime value) {
		return randomBetween( MIN_VALUE, prior( value ) );
	}
	
	@Override
	protected DateTime randomAfter(DateTime value) {
		return randomBetween( next( value ), MAX_VALUE );
	}
	
	@Override
	protected boolean hasAnyValueBetween(DateTime min, DateTime max) {
		return ( toMillis( max.minus( toMillis( min ) ) ) > 0 );
	}	

	@Override
	protected DateTime randomBetween(DateTime min, DateTime max) {
		long minInMillis = toMillis( min );
		long maxInMillis = toMillis( max );
		long millis = random.between( minInMillis, maxInMillis );
		return fromMillis( millis );
	}

	private long toMillis(DateTime dateTime) {
		return dateTime.toInstant().getMillis();
	}
	
	private DateTime fromMillis(long millis) {
		return ( new Instant( millis ) ).toDateTime();
	}
}