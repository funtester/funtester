package org.funtester.core.process.value;

import org.funtester.common.util.rand.LongRandom;
import org.joda.time.Days;
import org.joda.time.Instant;
import org.joda.time.LocalDate;

/**
 * Date value generator
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class DateValueGenerator extends DefaultValueGenerator< LocalDate > {

	public static final LocalDate MIN_VALUE = new LocalDate( 1, 1, 1 );
	public static final LocalDate MAX_VALUE = new LocalDate( 99999999, 12, 31 );
	
	private final LocalDate min;
	private final LocalDate max;
	private final LongRandom random = new LongRandom();
	
	/**
	 * Constructs with a minimum and a maximum value.
	 * 
	 * @param min	Minimum value. Assumes <code>MIN_DATE</code> if <code>null</code>.
	 * @param max	Maximum value. Assumes <code>MAX_DATE</code> if <code>null</code>.
	 */	
	public DateValueGenerator(
			final LocalDate min,
			final LocalDate max
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
	public LocalDate min() {
		return min;
	}

	@Override
	public LocalDate max() {
		return max;
	}

	@Override
	protected LocalDate zero() {
		return min.isAfter( MIN_VALUE ) ? min : MIN_VALUE;
	}

	@Override
	protected LocalDate middle() {
		try {
			final int maxDays = Days.daysBetween( min, max ).getDays();		
			return min.plusDays( maxDays / 2 );
		} catch (Exception e) {
			// Old way
			long minInMillis = toMillis( min );
			long maxInMillis = toMillis( max );
			long middle = minInMillis + ( ( maxInMillis - minInMillis ) / 2 );				
			return fromMillis( middle );			
		}
	}
	
	@Override
	protected boolean hasNext(LocalDate value) {
		return value.plusDays( 1 ).isBefore( max );
	}

	@Override
	protected LocalDate next(LocalDate value) {
		return value.plusDays( 1 );
	}

	@Override
	protected boolean hasPrior(LocalDate value) {
		return value.minusDays( 1 ).isAfter( min );
	}
	
	@Override
	protected LocalDate prior(LocalDate value) {
		return value.minusDays( 1 );
	}

	@Override
	protected LocalDate randomBefore(LocalDate value) {
		return randomBetween( MIN_VALUE, value.minusDays( 1 ) );
	}

	@Override
	protected LocalDate randomAfter(LocalDate value) {
		return randomBetween( value.plusDays( 1 ), MAX_VALUE );
	}
	
	@Override
	protected boolean hasAnyValueBetween(LocalDate min, LocalDate max) {
		return ( toMillis( max ) - toMillis( min ) ) > 0;
	}

	@Override
	protected LocalDate randomBetween(final LocalDate min, final LocalDate max) {
		try {
			final int maxDays = Days.daysBetween( min, max ).getDays();
			final int days = random.between( 0L, new Long( maxDays ) ).intValue();
			return min.plusDays( days );
		} catch (Exception e) {
			// Old way
			long minInMillis = toMillis( min );
			long maxInMillis = toMillis( max );
			long millis = random.between( minInMillis, maxInMillis );
			return fromMillis( millis );			
		}
	}
	
	public long toMillis(LocalDate date) {
		return date.toDateMidnight().toDateTime().toInstant().getMillis();
	}
	
	public LocalDate fromMillis(long millis) {
		return ( new Instant( millis ) ).toDateTime().toLocalDate();
	}
}
