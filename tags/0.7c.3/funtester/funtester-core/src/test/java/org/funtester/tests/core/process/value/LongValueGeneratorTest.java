package org.funtester.tests.core.process.value;

import static org.fest.assertions.Assertions.assertThat;

import org.funtester.core.process.value.InvalidValueOption;
import org.funtester.core.process.value.LongValueGenerator;
import org.funtester.core.process.value.ValidValueOption;
import org.funtester.core.process.value.ValueGenerator;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Tests the {@link LongValueGenerator}.
 *  
 * @author Thiago Delgado Pinto
 *
 */
public class LongValueGeneratorTest {
	
	public static final long MIN = LongValueGenerator.MIN_VALUE;
	public static final long MAX = LongValueGenerator.MAX_VALUE;
	
	@Test
	public void assumesMinimumLongWhenMinIsNotDefined() {
		LongValueGenerator gen = new LongValueGenerator( null, 0L );
		assertThat( gen.min() ).isEqualTo( Long.MIN_VALUE );
	}
	
	@Test
	public void assumesMaximumLongWhenMaxIsNotDefined() {
		LongValueGenerator gen = new LongValueGenerator( 0L, null );
		assertThat( gen.max() ).isEqualTo( Long.MAX_VALUE );
	}
	
	@Test
	public void hasNoValuesOutOfTheRangeIfMinAndMaxAreNotDefined() {
		LongValueGenerator gen = new LongValueGenerator( null, null );
		assertThat( gen.hasAvailableValuesOutOfTheRange() ).isFalse();
	}
	
	@Test
	public void hasValuesOutOfTheRangeWhenMinAndMaxAreNotTheMinimumAndTheMaximumAtTheSameTime() {
		LongValueGenerator gen1 = new LongValueGenerator( 0L, 0L );
		assertThat( gen1.hasAvailableValuesOutOfTheRange() ).isTrue();
		LongValueGenerator gen2 = new LongValueGenerator( 0L, null );
		assertThat( gen2.hasAvailableValuesOutOfTheRange() ).isTrue();
		LongValueGenerator gen3 = new LongValueGenerator( null, 0L );
		assertThat( gen3.hasAvailableValuesOutOfTheRange() ).isTrue();
	}
	
	@Test
	public void generatesNullAsInvalidValueIfHasNoAvailableValuesOutOfTheRange() {		
		LongValueGenerator gen = new LongValueGenerator( MIN, MAX );
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
		new LongValueGenerator( 0L, -1L );
	}
					
	@DataProvider(name="createValidValues")
	public Object[][] createValidValues() {
		return new Object[][] {
			// ZERO
			{ -10L, 10L, ValidValueOption.ZERO, 0L }, 					// Zero
			{ 10L, 20L, ValidValueOption.ZERO, 10L },					// Min as Zero
			{ -20L, -10L, ValidValueOption.ZERO, -20L },				// Min as Zero
			// MIN
			{ -10L, 10L, ValidValueOption.MIN, -10L }, 					// Min
			{ 10L, 20L, ValidValueOption.MIN, 10L },					// Min
			{ -20L, -10L, ValidValueOption.MIN, -20L },					// Min
			// MAX
			{ -10L, 10L, ValidValueOption.MAX, 10L }, 					// Max
			{ 10L, 20L, ValidValueOption.MAX, 20L },					// Max
			{ -20L, -10L, ValidValueOption.MAX, -10L },					// Max
			// MEDIAN
			{ -10L, 10L, ValidValueOption.MEDIAN, 0L }, 				// Middle
			{ 10L, 20L, ValidValueOption.MEDIAN, 15L },					// Middle
			{ -20L, -10L, ValidValueOption.MEDIAN, -15L },				// Middle
			// RIGHT_AFTER_MIN
			{ -10L, 10L, ValidValueOption.RIGHT_AFTER_MIN, -9L }, 		// Min+1
			{ 10L, 20L, ValidValueOption.RIGHT_AFTER_MIN, 11L },		// Min+1
			{ -20L, -10L, ValidValueOption.RIGHT_AFTER_MIN, -19L },		// Min+1
			// RIGHT_BEFORE_MAX
			{ -10L, 10L, ValidValueOption.RIGHT_BEFORE_MAX, 9L }, 		// Max-1
			{ 10L, 20L, ValidValueOption.RIGHT_BEFORE_MAX, 19L },		// Max-1
			{ -20L, -10L, ValidValueOption.RIGHT_BEFORE_MAX, -11L },	// Max-1			
		};
	}
	
	@Test(dataProvider="createValidValues")
	public void testValidOptions(
			Long min, Long max, ValidValueOption option, Long expected) {
		ValueGenerator< Long > gen1 = new LongValueGenerator( min, max );
		
		assertThat( gen1.validValue( option ) )
			.isEqualTo( expected );
	}
	
	
	@DataProvider(name="rangeForRandomValues")
	public Object[][] rangeForRandomValues() {
		return new Object[][] {
			{ -10L, 10L },
			{ 10L, 20L },
			{ -20L, -10L }
		};
	}
	
	
	@Test(dataProvider="rangeForRandomValues", invocationCount=5) 
	public void randomValidValueShouldBeInsideRange(Long min, Long max) {		
		ValueGenerator< Long > gen1 = new LongValueGenerator( min, max );		
		Long value = gen1.validValue( ValidValueOption.RANDOM_INSIDE_RANGE );
		assertThat( value )
			.isGreaterThanOrEqualTo( min )
			.isLessThanOrEqualTo( max );
	}

	
	@DataProvider(name="createInvalidValues")
	public Object[][] createInvalidValues() {
		return new Object[][] {
				// RIGHT_BEFORE_MIN
				{ -10L, 10L, InvalidValueOption.RIGHT_BEFORE_MIN, -11L },				
				{ 10L, 20L, InvalidValueOption.RIGHT_BEFORE_MIN, 9L },
				{ -20L, -10L, InvalidValueOption.RIGHT_BEFORE_MIN, -21L },
				// RIGHT_AFTER_MAX
				{ -10L, 10L, InvalidValueOption.RIGHT_AFTER_MAX, 11L },				
				{ 10L, 20L, InvalidValueOption.RIGHT_AFTER_MAX, 21L },
				{ -20L, -10L, InvalidValueOption.RIGHT_AFTER_MAX, -9L }
		};
	}
	
	@Test(dataProvider="createInvalidValues")
	public void testInvalidOptions(
			Long min, Long max, InvalidValueOption option, Long expected) {
		ValueGenerator< Long > gen1 = new LongValueGenerator( min, max );		
		assertThat( gen1.invalidValue( option ) )
			.isEqualTo( expected );
	}

	
	@Test(dataProvider="rangeForRandomValues", invocationCount=5)
	public void randomInvalidValueShouldBeOutsideRange(Long min, Long max) {		
		ValueGenerator< Long > gen1 = new LongValueGenerator( min, max );
		
		Long valueBeforeMin = gen1.invalidValue( InvalidValueOption.RANDOM_BEFORE_MIN );
		assertThat( valueBeforeMin )
			.isLessThan( min );
		
		Long valueAfterMax = gen1.invalidValue( InvalidValueOption.RANDOM_AFTER_MAX );
		assertThat( valueAfterMax )
			.isGreaterThan( max );
	}
}
