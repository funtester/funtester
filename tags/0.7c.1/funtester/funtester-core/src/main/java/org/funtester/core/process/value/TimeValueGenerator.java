package org.funtester.core.process.value;

import org.funtester.common.util.rand.LongRandom;
import org.joda.time.LocalTime;

/**
 * Time value generator
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class TimeValueGenerator extends DefaultValueGenerator< LocalTime > {
	
	public static final LocalTime MIN_VALUE = new LocalTime( 00, 00, 00, 000 );
	public static final LocalTime MAX_VALUE = new LocalTime( 23, 59, 59, 999 );

	private final LocalTime min;
	private final LocalTime max;
	private final LongRandom random = new LongRandom();
	

	/**
	 * Constructs with a minimum and a maximum value.
	 * 
	 * @param min	Minimum value. Assumes <code>MIN_TIME</code> if <code>null</code>.
	 * @param max	Maximum value. Assumes <code>MAX_TIME</code> if <code>null</code>.
	 */	
	public TimeValueGenerator(
			final LocalTime min,
			final LocalTime max
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
	public LocalTime min() {
		return min;
	}

	@Override
	public LocalTime max() {
		return max;
	}

	@Override
	protected LocalTime zero() {
		return min.isAfter( MIN_VALUE ) ? min : MIN_VALUE;
	}

	@Override
	protected LocalTime middle() {
		long minInMillis = toMillis( min );
		long maxInMillis = toMillis( max );
		long middle = minInMillis + ( ( maxInMillis - minInMillis ) / 2 );				
		return fromMillis( middle );
	}

	@Override
	protected boolean hasNext(LocalTime value) {
		return value.plusSeconds( 1 ).isBefore( max );
	}
	
	@Override
	protected LocalTime next(LocalTime value) {
		return value.plusSeconds( 1 );
	}
	
	@Override
	protected boolean hasPrior(LocalTime value) {
		return value.minusSeconds( 1 ).isAfter( min );
	}

	@Override
	protected LocalTime prior(LocalTime value) {
		return value.minusSeconds( 1 );
	}
	
	@Override
	protected boolean hasAnyValueBetween(LocalTime min, LocalTime max) {
		return ( toMillis( max ) - toMillis( min ) ) > 0;
	}

	@Override
	protected LocalTime randomBefore(LocalTime value) {
		return randomBetween( MIN_VALUE, prior( value ) );
	}

	@Override
	protected LocalTime randomAfter(LocalTime value) {
		return randomBetween( next( value ), MAX_VALUE );
	}

	@Override
	protected LocalTime randomBetween(LocalTime min, LocalTime max) {
		long minInMillis = toMillis( min );
		long maxInMillis = toMillis( max );
		long millis = random.between( minInMillis, maxInMillis );
		return fromMillis( millis );
	}

	private static long toMillis(LocalTime value) {
		return value.getMillisOfSecond()
			+ secToMs( value.getSecondOfMinute() )
			+ secToMs( minToSec( value.getMinuteOfHour() ) )
			+ secToMs( minToSec( hToMin( value.getHourOfDay() ) ) );
	}
	
	private static long secToMs(long seconds) {
		return seconds * 1000;
	}
	
	private static long minToSec(long minutes) {
		return minutes * 60;
	}
	
	private static long hToMin(long hours) {
		return hours * 60;
	}
	
	private LocalTime fromMillis(long ms) {
		int millis = (int) ms % 1000;
		int sec = (int) ms / 1000;
		int seconds = sec % 60;
		int min = sec / 60;								
		int minutes = min % 60;
		int hours = min / 60;
		return new LocalTime( hours, minutes, seconds, millis );
	}	
}