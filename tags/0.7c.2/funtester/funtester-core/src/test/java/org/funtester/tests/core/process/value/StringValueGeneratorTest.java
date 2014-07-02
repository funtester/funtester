package org.funtester.tests.core.process.value;

import static org.fest.assertions.Assertions.assertThat;

import org.funtester.core.process.value.InvalidValueOption;
import org.funtester.core.process.value.StringValueGenerator;
import org.funtester.core.process.value.ValidValueOption;
import org.funtester.core.process.value.ValueGenerator;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class StringValueGeneratorTest {
	
	// Shorten versions
	public static final int DEF = StringValueGenerator.DEFAULT_LENGTH;
	public static final int MIN = StringValueGenerator.MIN_LENGTH;
	public static final int MAX = StringValueGenerator.MAX_LENGTH;
	
	
	@Test
	public void assumesMinimumLengthWhenMinIsNotDefined() {
		StringValueGenerator gen = new StringValueGenerator( null, 0 );
		assertThat( gen.min() ).isEqualTo( MIN );
	}
	
	@Test
	public void assumesDefaultLengthWhenMaxIsNotDefined() {
		StringValueGenerator gen = new StringValueGenerator( 0, null );
		assertThat( gen.max() ).isEqualTo( DEF );
	}
	
	@Test
	public void hasValuesOutOfTheRangeIfMinAndMaxAreNotDefined() {
		//
		// Remember: DEFAULT_LENGTH < MAX_LENGTH
		//
		StringValueGenerator gen = new StringValueGenerator( null, null );
		assertThat( gen.hasAvailableValuesOutOfTheRange() ).isTrue();
	}
	
	@Test
	public void hasNoValuesOutOfTheRangeIfMinAndMaxAreDefinedAsTheMinimumAndTheMaximum() {
		StringValueGenerator gen = new StringValueGenerator( MIN, MAX );
		assertThat( gen.hasAvailableValuesOutOfTheRange() ).isFalse();
	}
	
	@Test
	public void hasValuesOutOfTheRangeWhenMinAndMaxAreNotTheMinimumAndTheMaximumAtTheSameTime() {
		StringValueGenerator gen1 = new StringValueGenerator( 0, 0 );
		assertThat( gen1.hasAvailableValuesOutOfTheRange() ).isTrue();
		StringValueGenerator gen2 = new StringValueGenerator( 0, null );
		assertThat( gen2.hasAvailableValuesOutOfTheRange() ).isTrue();
		StringValueGenerator gen3 = new StringValueGenerator( null, 0 );
		assertThat( gen3.hasAvailableValuesOutOfTheRange() ).isTrue();
	}
	
	@Test
	public void generatesNullAsInvalidValueIfHasNoAvailableValuesOutOfTheRange() {		
		StringValueGenerator gen = new StringValueGenerator( MIN, MAX );
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
	public void throwsExceptionIfMinIsUnderZero() {
		new StringValueGenerator( -1, null );
	}
	
	@Test(expectedExceptions={IllegalArgumentException.class})
	public void throwsExceptionIfMaxIsUnderZero() {
		new StringValueGenerator( null, -1 );
	}
	
	@Test(expectedExceptions={IllegalArgumentException.class})
	public void throwsExceptionIfMinIsGreaterThanMax() {
		new StringValueGenerator( 1, 0 );
	}
	
	@Test
	public void generatesNullAsInvalidValueIfMinAndMaxAreTheMinimumAndMaximumAllowed() {		
		StringValueGenerator gen = new StringValueGenerator( MIN, MAX );
		assertThat( gen.invalidValue( InvalidValueOption.RIGHT_BEFORE_MIN ) )
			.isNull();				
		assertThat( gen.invalidValue( InvalidValueOption.RIGHT_AFTER_MAX ) )
			.isNull();			
		assertThat( gen.invalidValue( InvalidValueOption.RANDOM_BEFORE_MIN ) )
			.isNull();		
		assertThat( gen.invalidValue( InvalidValueOption.RANDOM_AFTER_MAX ) )
			.isNull();
	}

	@DataProvider(name="createValidValues")
	public Object[][] createValidValues() {
		return new Object[][] {
			// ZERO (equal to MIN)
			{ 0, 50, ValidValueOption.ZERO, 0 },
			{ 1, 50, ValidValueOption.ZERO, 1 },
			{ 2, 2, ValidValueOption.ZERO, 2 },
			// MIN
			{ 0, 50, ValidValueOption.MIN, 0 },
			{ 2, 2, ValidValueOption.MIN, 2 },
			// MAX
			{ 0, 50, ValidValueOption.MAX, 50 },
			{ 2, 2, ValidValueOption.MAX, 2 },
			// MEDIAN
			{ 0, 50, ValidValueOption.MEDIAN, 25 },		
			{ 1, 50, ValidValueOption.MEDIAN, 25 },
			{ 2, 2, ValidValueOption.MEDIAN, 2 },
			// RIGHT_AFTER_MIN
			{ 0, 50, ValidValueOption.RIGHT_AFTER_MIN, 1 },		
			{ 2, 2, ValidValueOption.RIGHT_AFTER_MIN, 2 },
			// RIGHT_BEFORE_MAX
			{ 0, 50, ValidValueOption.RIGHT_BEFORE_MAX, 49 },		
			{ 2, 2, ValidValueOption.RIGHT_BEFORE_MAX, 2 },			
		};
	}	
	
	@Test(dataProvider="createValidValues")
	public void testValidOptions(
			Integer min, Integer max,
			ValidValueOption option,
			Integer expectedSize
			) {
		System.out.println( "testValidOptions: min=" + min + ", max=" + max + ", option=" + option + ", expectedSize=" + expectedSize );
		
		ValueGenerator< String > gen1 = new StringValueGenerator( min, max );
		String value = gen1.validValue( option );
		assertThat( value.length() )
			.isEqualTo( expectedSize );
	}
	
	
	@DataProvider(name="rangeForRandomValues")
	public Object[][] rangeForRandomValues() {
		return new Object[][] {
			{  0, 10 },
			{ 10, 20 }
		};
	}
	
	// 5 invocation for random tests	
	@Test(dataProvider="rangeForRandomValues", invocationCount=5)
	public void randomValidValueShouldBeInsideRange(Integer min, Integer max) {		
		ValueGenerator< String > gen1 =	new StringValueGenerator( min, max );		
		String value = gen1.validValue( ValidValueOption.RANDOM_INSIDE_RANGE );
		assertThat( value.length() )
			.isGreaterThanOrEqualTo( min )
			.isLessThanOrEqualTo( max );
	}
	
	
	@DataProvider(name="createInvalidValues")
	public Object[][] createInvalidValues() {
		return new Object[][] {
				// RIGHT_BEFORE_MIN
				{ null, 10, InvalidValueOption.RIGHT_BEFORE_MIN, 0, 0 },
				{ 0, 10, InvalidValueOption.RIGHT_BEFORE_MIN, 0, 0 },
				{ 10, 20, InvalidValueOption.RIGHT_BEFORE_MIN, 0, 9 },
				// RIGHT_AFTER_MAX			
				{ 0, null, InvalidValueOption.RIGHT_AFTER_MAX, DEF + 1, MAX },
				{ 0, DEF, InvalidValueOption.RIGHT_AFTER_MAX, DEF + 1, MAX },					
				{ 0, 20, InvalidValueOption.RIGHT_AFTER_MAX, 21, MAX }
		};
	}

	
	// 5 invocation for random tests
	@Test(dataProvider="createInvalidValues", invocationCount=5)
	public void testInvalidOptions(
			Integer min, Integer max,
			InvalidValueOption option,
			Integer expectedMin, Integer expectedMax
			) {
		StringValueGenerator gen1 = new StringValueGenerator( min, max );		
		String value = gen1.invalidValue( option );
		assertThat( value.length() )
			.isGreaterThanOrEqualTo( expectedMin )
			.isLessThanOrEqualTo( expectedMax );
	}
	
	@Test
	public void shouldUseZeroAsMinimumLengthIfNotDefinedInRange() {
		ValueGenerator< String > gen1 =	new StringValueGenerator( null, 100 );
		String value = gen1.validValue( ValidValueOption.RIGHT_AFTER_MIN );
		assertThat( value.length() ).isEqualTo( 1 );
	}
	
	@Test
	public void shouldAcceptABigStringIfMaximumIsNotDefined() {
		StringValueGenerator gen1 =	new StringValueGenerator( 100, null );
		assertThat( gen1.max() ).isEqualTo( DEF );
		String aValue = gen1.validValue( ValidValueOption.RIGHT_BEFORE_MAX );
		assertThat( aValue.length() ).isEqualTo( DEF - 1 );
	}

}