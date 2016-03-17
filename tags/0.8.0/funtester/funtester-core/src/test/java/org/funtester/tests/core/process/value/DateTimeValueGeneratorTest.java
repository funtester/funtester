package org.funtester.tests.core.process.value;

import static org.fest.assertions.Assertions.assertThat;

import org.funtester.core.process.value.DateTimeValueGenerator;
import org.funtester.core.process.value.InvalidValueOption;
import org.funtester.core.process.value.ValidValueOption;
import org.funtester.core.process.value.ValueGenerator;
import org.joda.time.DateTime;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class DateTimeValueGeneratorTest {
	
	public static final DateTime MIN = DateTimeValueGenerator.MIN_VALUE;
	public static final DateTime MAX = DateTimeValueGenerator.MAX_VALUE;
	
	public static DateTime dt(int y, int m, int d, int h, int n, int s) {
		return new DateTime( y, m, d, h, n, s );
	}
	
	public static DateTime dt(int y, int m, int d, int h, int n, int s, int ms) {
		return new DateTime( y, m, d, h, n, s, ms );
	}
		
	@Test
	public void assumesMinimumWhenMinIsNotDefined() {
		DateTimeValueGenerator gen = new DateTimeValueGenerator( null, DateTime.now() );
		assertThat( gen.min() ).isEqualTo( MIN );
	}
	
	@Test
	public void assumesMaximumWhenMaxIsNotDefined() {
		DateTimeValueGenerator gen = new DateTimeValueGenerator( DateTime.now(), null );
		assertThat( gen.max() ).isEqualTo( MAX );
	}
	
	@Test
	public void hasNoValuesOutOfTheRangeIfMinAndMaxAreNotDefined() {
		DateTimeValueGenerator gen = new DateTimeValueGenerator( null, null );
		assertThat( gen.hasAvailableValuesOutOfTheRange() ).isFalse();
	}
	
	@Test
	public void hasValuesOutOfTheRangeWhenMinAndMaxAreNotTheMinimumAndTheMaximumAtTheSameTime() {
		final DateTime NOW = DateTime.now();
		
		DateTimeValueGenerator gen1 = new DateTimeValueGenerator( NOW, NOW );			
		assertThat( gen1.hasAvailableValuesOutOfTheRange() ).isTrue();
		
		DateTimeValueGenerator gen2 = new DateTimeValueGenerator( NOW, null );
		assertThat( gen2.hasAvailableValuesOutOfTheRange() ).isTrue();
		
		DateTimeValueGenerator gen3 = new DateTimeValueGenerator( null, NOW );
		assertThat( gen3.hasAvailableValuesOutOfTheRange() ).isTrue();
	}
	
	@Test
	public void generatesNullAsInvalidValueIfHasNoAvailableValuesOutOfTheRange() {		
		DateTimeValueGenerator gen = new DateTimeValueGenerator( MIN, MAX );
		assertThat( gen.hasAvailableValuesOutOfTheRange() ).isFalse();
		
		assertThat( gen.invalidValue( InvalidValueOption.RIGHT_BEFORE_MIN ) )
			.isNull();				
		assertThat( gen.invalidValue( InvalidValueOption.RIGHT_AFTER_MAX ) )
			.isNull();			
		assertThat( gen.invalidValue( InvalidValueOption.RANDOM_BEFORE_MIN ) )
			.isNull();		
		assertThat( gen.invalidValue( InvalidValueOption.RANDOM_AFTER_MAX ) )
			.isNull();
	}

	
	@Test(expectedExceptions={IllegalArgumentException.class})
	public void throwsExceptionIfMinIsGreaterThanMax() {
		final DateTime NOW = DateTime.now();
		new DateTimeValueGenerator( NOW, NOW.minusMillis( 1 ) );
	}
	
	@DataProvider(name="createValidValues")
	public Object[][] createValidValues() {
		return new Object[][] {
			// ZERO
			{ dt( 2012, 12, 1, 00, 00, 00 ), dt( 2012, 12, 31, 23, 59, 59 ), ValidValueOption.ZERO, dt( 2012, 12, 1, 00, 00, 00 ) },			
			{ dt( 0, 1, 1, 0, 0, 00 ), dt( 2012, 12, 31, 23, 59, 59 ), ValidValueOption.ZERO, dt( 1, 1, 1, 00, 00, 00 ) },						
			// MIN
			{ dt( 2012, 12, 1, 00, 00, 00 ), dt( 2012, 12, 31, 23, 59, 59 ), ValidValueOption.MIN, dt( 2012, 12, 1, 00, 00, 00 ) },				
			// MAX
			{ dt( 2012, 12, 1, 00, 00, 00 ), dt( 2012, 12, 31, 23, 59, 59 ), ValidValueOption.MAX, dt( 2012, 12, 31, 23, 59, 59 ) },				
			// MEDIAN
			{ dt( 2012, 12, 1, 00, 00, 00 ), dt( 2012, 12, 1, 23, 59, 59 ), ValidValueOption.MEDIAN, dt( 2012, 12, 1, 11, 59, 59, 500 ) },				
			// RIGHT_AFTER_MIN
			{ dt( 2012, 12, 1, 00, 00, 00 ), dt( 2012, 12, 31, 23, 59, 59 ), ValidValueOption.RIGHT_AFTER_MIN, dt( 2012, 12, 1, 00, 00, 01 ) },		
			// RIGHT_BEFORE_MAX
			{ dt( 2012, 12, 1, 00, 00, 00 ), dt( 2012, 12, 31, 23, 59, 59 ), ValidValueOption.RIGHT_BEFORE_MAX, dt( 2012, 12, 31, 23, 59, 58 ) },				
		};
	}
	
	@Test(dataProvider="createValidValues")
	public void testValidOptions(
			DateTime min, DateTime max, ValidValueOption option, DateTime expected) {
		ValueGenerator< DateTime > gen1 = new DateTimeValueGenerator( min, max );		
		assertThat( gen1.validValue( option ) ).isEqualTo( expected );
	}
	
	
	@DataProvider(name="rangeForRandomValues")
	public Object[][] rangeForRandomValues() {
		return new Object[][] {
			{ dt( 2012, 12, 1, 00, 00, 00 ), dt( 2012, 12, 31, 23, 59, 59 ) },
			{ dt( 1900, 1, 1, 00, 00, 00 ), dt( 2012, 12, 31, 23, 59, 59 ) },	
			{ dt( 0, 1, 1, 00, 00, 00 ), dt( 0, 1, 1, 00, 00, 01 ) }	
		};
	}
		
	@Test(dataProvider="rangeForRandomValues", invocationCount=5) 
	public void randomValidValueShouldBeInsideRange(DateTime min, DateTime max) {		
		ValueGenerator< DateTime > gen1 = new DateTimeValueGenerator( min, max );		
		DateTime value = gen1.validValue( ValidValueOption.RANDOM_INSIDE_RANGE );
		assertThat( value.isAfter( min ) );
		assertThat( value.isBefore( max ) );
		assertThat( value.minusSeconds( 1 ).isAfter( min ) );
		assertThat( value.plusSeconds( 1 ).isBefore( max ) );
	}

}