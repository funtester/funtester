package org.funtester.tests.core.process.value;

import static org.fest.assertions.Assertions.assertThat;

import org.funtester.core.process.value.InvalidValueOption;
import org.funtester.core.process.value.TimeValueGenerator;
import org.funtester.core.process.value.ValidValueOption;
import org.funtester.core.process.value.ValueGenerator;
import org.joda.time.LocalTime;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TimeValueGeneratorTest {
	
	public static final LocalTime MIN = TimeValueGenerator.MIN_VALUE;
	public static final LocalTime MAX = TimeValueGenerator.MAX_VALUE;
	
	public static LocalTime t(int h, int m, int s) {
		return new LocalTime( h, m, s, 0  );
	}
	
	public static LocalTime t(int h, int m, int s, int ms) {
		return new LocalTime( h, m, s, ms  );
	}
		
	@Test
	public void assumesMinimumWhenMinIsNotDefined() {
		TimeValueGenerator gen = new TimeValueGenerator( null, LocalTime.now() );
		assertThat( gen.min() ).isEqualTo( MIN );
	}
	
	@Test
	public void assumesMaximumWhenMaxIsNotDefined() {
		TimeValueGenerator gen = new TimeValueGenerator( LocalTime.now(), null );
		assertThat( gen.max() ).isEqualTo( MAX );
	}
	
	@Test
	public void hasNoValuesOutOfTheRangeIfMinAndMaxAreNotDefined() {
		TimeValueGenerator gen = new TimeValueGenerator( null, null );
		assertThat( gen.hasAvailableValuesOutOfTheRange() ).isFalse();
	}
	
	@Test
	public void hasValuesOutOfTheRangeWhenMinAndMaxAreNotTheMinimumAndTheMaximumAtTheSameTime() {
		final LocalTime NOW = LocalTime.now();
		
		TimeValueGenerator gen1 = new TimeValueGenerator( NOW, NOW );			
		assertThat( gen1.hasAvailableValuesOutOfTheRange() ).isTrue();
		
		TimeValueGenerator gen2 = new TimeValueGenerator( NOW, null );
		assertThat( gen2.hasAvailableValuesOutOfTheRange() ).isTrue();
		
		TimeValueGenerator gen3 = new TimeValueGenerator( null, NOW );
		assertThat( gen3.hasAvailableValuesOutOfTheRange() ).isTrue();
	}
	
	@Test
	public void generatesNullAsInvalidValueIfHasNoAvailableValuesOutOfTheRange() {		
		TimeValueGenerator gen = new TimeValueGenerator( MIN, MAX );
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
		final LocalTime NOW = LocalTime.now();
		new TimeValueGenerator( NOW, NOW.minusSeconds( 1 ) );
	}
	
	@DataProvider(name="createValidValues")
	public Object[][] createValidValues() {
		return new Object[][] {
			// ZERO
			{ t( 0, 0, 0 ), t( 23, 59, 59 ), ValidValueOption.ZERO, t( 0, 0, 0 ) },			
			{ t( 8, 0, 0 ), t( 23, 59, 59 ), ValidValueOption.ZERO, t( 8, 0, 0 ) },						
			// MIN
			{ t( 8, 0, 0 ), t( 23, 59, 59 ), ValidValueOption.MIN, t( 8, 0, 0 ) },				
			// MAX
			{ t( 8, 0, 0 ), t( 23, 59, 59 ), ValidValueOption.MAX, t( 23, 59, 59 ) },				
			// MEDIAN
			{ t( 0, 0, 0 ), t( 23, 59, 59 ), ValidValueOption.MEDIAN, t( 11, 59, 59, 500 ) },				
			// RIGHT_AFTER_MIN
			{ t( 0, 0, 0 ), t( 23, 59, 59 ), ValidValueOption.RIGHT_AFTER_MIN, t( 0, 0, 1 ) },		
			// RIGHT_BEFORE_MAX
			{ t( 0, 0, 0 ), t( 23, 59, 59 ), ValidValueOption.RIGHT_BEFORE_MAX, t( 23, 59, 58 ) },				
		};
	}
	
	@Test(dataProvider="createValidValues")
	public void testValidOptions(
			LocalTime min, LocalTime max, ValidValueOption option, LocalTime expected) {
		ValueGenerator< LocalTime > gen1 = new TimeValueGenerator( min, max );		
		assertThat( gen1.validValue( option ) ).isEqualTo( expected );
	}
		
	@DataProvider(name="rangeForRandomValues")
	public Object[][] rangeForRandomValues() {
		return new Object[][] {
			{ t( 0, 0, 0 ), t( 23, 59, 59 ) },			
			{ t( 0, 0, 0 ), t( 0, 0, 0 ) },
			{ t( 23, 59, 59 ), t( 23, 59, 59 ) }
		};
	}
		
	@Test(dataProvider="rangeForRandomValues", invocationCount=5) 
	public void randomValidValueShouldBeInsideRange(LocalTime min, LocalTime max) {		
		ValueGenerator< LocalTime > gen1 = new TimeValueGenerator( min, max );		
		LocalTime value = gen1.validValue( ValidValueOption.RANDOM_INSIDE_RANGE );
		
		assertThat( value.isAfter( min ) || value.isEqual( min ) );
		assertThat( value.isBefore( max ) || value.isEqual( min ) );
		
		assertThat( value.minusSeconds( 1 ).isAfter( min )
				|| value.minusSeconds( 1 ).isEqual( min ) );
		
		assertThat( value.plusSeconds( 1 ).isBefore( max )
				|| value.plusSeconds( 1 ).isEqual( max ) );
	}	
}