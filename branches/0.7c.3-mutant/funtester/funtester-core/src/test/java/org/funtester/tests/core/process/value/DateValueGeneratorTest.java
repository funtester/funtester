package org.funtester.tests.core.process.value;

import static org.fest.assertions.Assertions.assertThat;

import org.funtester.core.process.value.DateValueGenerator;
import org.funtester.core.process.value.InvalidValueOption;
import org.funtester.core.process.value.ValidValueOption;
import org.funtester.core.process.value.ValueGenerator;
import org.joda.time.LocalDate;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class DateValueGeneratorTest {
	
	public static final LocalDate MIN = DateValueGenerator.MIN_VALUE;
	public static final LocalDate MAX = DateValueGenerator.MAX_VALUE;
	
	public static LocalDate dt(int y, int m, int d) {
		return new LocalDate( y, m, d  );
	}
		
	@Test
	public void assumesMinimumWhenMinIsNotDefined() {
		DateValueGenerator gen = new DateValueGenerator( null, LocalDate.now() );
		assertThat( gen.min() ).isEqualTo( MIN );
	}
	
	@Test
	public void assumesMaximumWhenMaxIsNotDefined() {
		DateValueGenerator gen = new DateValueGenerator( LocalDate.now(), null );
		assertThat( gen.max() ).isEqualTo( MAX );
	}
	
	@Test
	public void hasNoValuesOutOfTheRangeIfMinAndMaxAreNotDefined() {
		DateValueGenerator gen = new DateValueGenerator( null, null );
		assertThat( gen.hasAvailableValuesOutOfTheRange() ).isFalse();
	}
	
	@Test
	public void hasValuesOutOfTheRangeWhenMinAndMaxAreNotTheMinimumAndTheMaximumAtTheSameTime() {
		final LocalDate NOW = LocalDate.now();
		
		DateValueGenerator gen1 = new DateValueGenerator( NOW, NOW );			
		assertThat( gen1.hasAvailableValuesOutOfTheRange() ).isTrue();
		
		DateValueGenerator gen2 = new DateValueGenerator( NOW, null );
		assertThat( gen2.hasAvailableValuesOutOfTheRange() ).isTrue();
		
		DateValueGenerator gen3 = new DateValueGenerator( null, NOW );
		assertThat( gen3.hasAvailableValuesOutOfTheRange() ).isTrue();
	}
	
	@Test
	public void generatesNullAsInvalidValueIfHasNoAvailableValuesOutOfTheRange() {		
		DateValueGenerator gen = new DateValueGenerator( MIN, MAX );
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
		final LocalDate NOW = LocalDate.now();
		new DateValueGenerator( NOW, NOW.minusDays( 1 ) );
	}

	@DataProvider(name="createValidValues")
	public Object[][] createValidValues() {
		return new Object[][] {
			// ZERO
			{ dt( 2012, 12, 1 ), dt( 2012, 12, 31 ), ValidValueOption.ZERO, dt( 2012, 12, 1 ) },			
			{ dt( 0, 1, 1 ), dt( 2012, 12, 31 ), ValidValueOption.ZERO, dt( 1, 1, 1 ) },						
			// MIN
			{ dt( 2012, 12, 1 ), dt( 2012, 12, 31 ), ValidValueOption.MIN, dt( 2012, 12, 1 ) },				
			// MAX
			{ dt( 2012, 12, 1 ), dt( 2012, 12, 31 ), ValidValueOption.MAX, dt( 2012, 12, 31 ) },				
			// MEDIAN
			{ dt( 2012, 12, 1 ), dt( 2012, 12, 1 ), ValidValueOption.MEDIAN, dt( 2012, 12, 1 ) },				
			// RIGHT_AFTER_MIN
			{ dt( 2012, 12, 1 ), dt( 2012, 12, 31 ), ValidValueOption.RIGHT_AFTER_MIN, dt( 2012, 12, 2 ) },		
			// RIGHT_BEFORE_MAX
			{ dt( 2012, 12, 1 ), dt( 2012, 12, 31 ), ValidValueOption.RIGHT_BEFORE_MAX, dt( 2012, 12, 30 ) },				
		};
	}
	
	@Test(dataProvider="createValidValues")
	public void testValidOptions(
			LocalDate min, LocalDate max, ValidValueOption option, LocalDate expected) {
		ValueGenerator< LocalDate > gen1 = new DateValueGenerator( min, max );		
		assertThat( gen1.validValue( option ) )
			.isEqualTo( expected );
	}
		
	@DataProvider(name="rangeForRandomValues")
	public Object[][] rangeForRandomValues() {
		return new Object[][] {
			{ dt( 2012, 12, 1 ), dt( 2012, 12, 31 ) },
			{ dt( 1900, 1, 1 ), dt( 2012, 12, 31 ) },	
			{ dt( 0, 1, 1 ), dt( 0, 1, 1 ) }	
		};
	}
		
	@Test(dataProvider="rangeForRandomValues", invocationCount=5) 
	public void randomValidValueShouldBeInsideRange(LocalDate min, LocalDate max) {		
		ValueGenerator< LocalDate > gen1 = new DateValueGenerator( min, max );		
		LocalDate value = gen1.validValue( ValidValueOption.RANDOM_INSIDE_RANGE );
		assertThat( value.isAfter( min ) );
		assertThat( value.isBefore( max ) );
		assertThat( value.minusDays( 1 ).isAfter( min ) );
		assertThat( value.plusDays( 1 ).isBefore( max ) );
	}	
}