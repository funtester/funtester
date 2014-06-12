package org.funtester.tests.core.process.value;

import static org.fest.assertions.Assertions.assertThat;

import org.funtester.core.process.value.ValidValueOption;
import org.funtester.core.process.value.XegerRegExValueGenerator;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class XegerRegExValueGeneratorTest {

	private XegerRegExValueGenerator gen; // Under test
	
	@DataProvider(name="createValues")
	public Object[][] createValues() {
		return new Object[][] {
			{ "[^A]" }, 											// negate
			{ "[0-9]" },											// digit
			{ "[A-Za-z]" },											// alpha
			{ "[A-Za-z0-9]" },										// alphanumeric
			{ "((fun)( )(tester))|(funtester)" },					// parenthesis, space, or
			{ "A[0-9]{2,10}" },										// digit, quantification
			{ "[a-z]+" },											// plus
			// SAMPLES
			{ "((1|2)[0-9]|3[0-1])\\/([0-1][0-2])\\/([0-9]{4})" }	// date
		};
	}
	
	@Test(dataProvider="createValues")
	public void test(String expression) {
		gen = new XegerRegExValueGenerator( expression );
		final ValidValueOption doesNotMatter = ValidValueOption.ZERO;
		String value = gen.validValue( doesNotMatter );
		//System.out.println( "expression: " + expression + " value: " + value );
		assertThat( value ).matches( expression );
	}
}
