package org.funtester.tests.core.process.value;

import static org.fest.assertions.Assertions.assertThat;

import org.fest.assertions.Delta;
import org.funtester.core.process.value.DoubleValueGenerator;
import org.funtester.core.process.value.InvalidValueOption;
import org.funtester.core.process.value.ValidValueOption;
import org.funtester.core.process.value.ValueGenerator;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class DoubleValueGeneratorTest {
	
	public static final double MIN = DoubleValueGenerator.MIN_VALUE;
	public static final double MAX = DoubleValueGenerator.MAX_VALUE;
	private final static double DELTA = 0.00000000000000001; // Fixture
	
	@Test
	public void assumesNegativeMaximumDoubleWhenMinIsNotDefined() {
		DoubleValueGenerator gen = new DoubleValueGenerator( null, 0d );
		assertThat( gen.min() ).isEqualTo( MIN );
	}
	
	@Test
	public void assumesMaximumDoubleWhenMaxIsNotDefined() {
		DoubleValueGenerator gen = new DoubleValueGenerator( 0d, null );
		assertThat( gen.max() ).isEqualTo( MAX );
	}
	
	@Test
	public void hasNoValuesOutOfTheRangeIfMinAndMaxAreNotDefined() {
		DoubleValueGenerator gen = new DoubleValueGenerator( null, null );
		assertThat( gen.hasAvailableValuesOutOfTheRange() ).isFalse();
	}
	
	@Test
	public void hasValuesOutOfTheRangeWhenMinAndMaxAreNotTheMinimumAndTheMaximumAtTheSameTime() {
		DoubleValueGenerator gen1 = new DoubleValueGenerator( 0d, 0d );
		assertThat( gen1.hasAvailableValuesOutOfTheRange() ).isTrue();
		DoubleValueGenerator gen2 = new DoubleValueGenerator( 0d, null );
		assertThat( gen2.hasAvailableValuesOutOfTheRange() ).isTrue();
		DoubleValueGenerator gen3 = new DoubleValueGenerator( null, 0d );
		assertThat( gen3.hasAvailableValuesOutOfTheRange() ).isTrue();
	}
	
	@Test
	public void generatesNullAsInvalidValueIfHasNoAvailableValuesOutOfTheRange() {		
		DoubleValueGenerator gen = new DoubleValueGenerator( MIN, MAX );
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
		new DoubleValueGenerator( 0d, 0d - (DELTA * 2) );
	}

	@DataProvider(name="createValidValues")
	public Object[][] createValidValues() {
		return new Object[][] {
			// ZERO
			{ -10.0, 10.0, DELTA, ValidValueOption.ZERO, 0.0 }, 				
			{ 10.0, 20.0, DELTA, ValidValueOption.ZERO, 10.0 },					
			{ -20.0, -10.0, DELTA, ValidValueOption.ZERO, -20.0 },				
			// MIN
			{ -10.0, 10.0, DELTA, ValidValueOption.MIN, -10.0 }, 				
			{ 10.0, 20.0, DELTA, ValidValueOption.MIN, 10.0 },					
			{ -20.0, -10.0, DELTA, ValidValueOption.MIN, -20.0 },				
			// MAX
			{ -10.0, 10.0, DELTA, ValidValueOption.MAX, 10.0 }, 				
			{ 10.0, 20.0, DELTA, ValidValueOption.MAX, 20.0 },					
			{ -20.0, -10.0, DELTA, ValidValueOption.MAX, -10.0 },				
			// MEDIAN
			{ -10.0, 10.0, DELTA, ValidValueOption.MEDIAN, 0.0 }, 				
			{ 10.0, 20.0, DELTA, ValidValueOption.MEDIAN, 15.0 },				
			{ -20.0, -10.0, DELTA, ValidValueOption.MEDIAN, -15.0 },			
			// RIGHT_AFTER_MIN
			{ -10.0, 10.0, DELTA, ValidValueOption.RIGHT_AFTER_MIN, -10.0 + DELTA }, 	
			{ 10.0, 20.0, DELTA, ValidValueOption.RIGHT_AFTER_MIN, 10.0 + DELTA },		
			{ -20.0, -10.0, DELTA, ValidValueOption.RIGHT_AFTER_MIN, -20.0 + DELTA },	
			// RIGHT_BEFORE_MAX
			{ -10.0, 10.0, DELTA, ValidValueOption.RIGHT_BEFORE_MAX, 10.0 - DELTA }, 	
			{ 10.0, 20.0, DELTA, ValidValueOption.RIGHT_BEFORE_MAX, 20.0 - DELTA },	
			{ -20.0, -10.0, DELTA, ValidValueOption.RIGHT_BEFORE_MAX, -10.0 - DELTA },			
		};
	}
	
	@Test(dataProvider="createValidValues")
	public void testValidOptions (
			Double min, Double max, Double delta,
			ValidValueOption option, Double expected
			) throws Exception {
		ValueGenerator< Double > gen1 = new DoubleValueGenerator( min, max, delta );		
		assertThat( gen1.validValue( option ) )
			.isEqualTo( expected, Delta.delta( delta ) );
	}

	
	@DataProvider(name="rangeForRandomValues")
	public Object[][] rangeForRandomValues() {
		return new Object[][] {
			{ -10.0, 10.0, DELTA },
			{ 10.0, 20.0, DELTA },
			{ -20.0, -10.0, DELTA },
			{ -100.0, 100.0, DELTA }
		};
	}
	
	
	@Test(dataProvider="rangeForRandomValues")
	public void randomValidValueShouldBeInsideRange(
		Double min, Double max, Double delta
		) {		
		ValueGenerator< Double > gen1 =	new DoubleValueGenerator( min, max, delta );		
		assertThat( gen1.validValue( ValidValueOption.RANDOM_INSIDE_RANGE ) )
			.isGreaterThanOrEqualTo( min )
			.isLessThanOrEqualTo( max );
	}


	@DataProvider(name="createInvalidValues")
	public Object[][] createInvalidValues() {
		return new Object[][] {
				// RIGHT_BEFORE_MIN
				{ -10.0, 10.0, DELTA, InvalidValueOption.RIGHT_BEFORE_MIN, -10.0 - DELTA },				
				{ 10.0, 20.0, DELTA, InvalidValueOption.RIGHT_BEFORE_MIN, 10.0 - DELTA },
				{ -20.0, -10.0, DELTA, InvalidValueOption.RIGHT_BEFORE_MIN, -20.0 - DELTA },
				// RIGHT_AFTER_MAX
				{ -10.0, 10.0, DELTA, InvalidValueOption.RIGHT_AFTER_MAX, 10.0 + DELTA },				
				{ 10.0, 20.0, DELTA, InvalidValueOption.RIGHT_AFTER_MAX, 20.0 + DELTA },
				{ -20.0, -10.0, DELTA, InvalidValueOption.RIGHT_AFTER_MAX, -10.0 + DELTA }
		};
	}
	
	@Test(dataProvider="createInvalidValues")
	public void testInvalidOptions(
			Double min, Double max, Double delta,
			InvalidValueOption option, Double expected) {
		ValueGenerator< Double > gen1 = new DoubleValueGenerator( min, max, delta );		
		assertThat( gen1.invalidValue( option ) )
			.isEqualTo( expected, Delta.delta( delta ) );
	}

	
	@Test(dataProvider="rangeForRandomValues")
	public void randomInvalidValueShouldBeOutsideRange(
			Double min, Double max, Double delta) {		
		ValueGenerator< Double > gen1 =	new DoubleValueGenerator( min, max, delta );		
		
		Double valueBeforeMin = gen1.invalidValue( InvalidValueOption.RANDOM_BEFORE_MIN );
		assertThat( valueBeforeMin )
			.isLessThan( min );
		
		Double valueAfterMax = gen1.invalidValue( InvalidValueOption.RANDOM_AFTER_MAX );
		assertThat( valueAfterMax )
			.isGreaterThan( max );
	}	
	
	@Test
	public void calculatesTheDeltaAsTheGreatestFractionalFromMinAndMax() {
		Double min = 9.12;	// Greatest length
		Double max = 9.123;
		DoubleValueGenerator gen1 =	new DoubleValueGenerator( min, max );
		assertThat( gen1.delta() )
			.isEqualTo( .001, Delta.delta( 0.0001 ) );
	}
}