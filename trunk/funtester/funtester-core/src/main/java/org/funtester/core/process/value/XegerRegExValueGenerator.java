package org.funtester.core.process.value;

import nl.flotsam.xeger.Xeger;

import org.funtester.common.util.rand.StringRandom;

/**
 * A {@link RegExValueGenerator} that uses the {@link nl.flotsam.xeger.Xeger}
 * library.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class XegerRegExValueGenerator implements RegExValueGenerator {
	
	private final String expression;

	public XegerRegExValueGenerator(final String expression) {
		this.expression = expression;
	}
	
	public boolean hasAvailableValuesOutOfTheRange() {
		return true; // The negated expression is always a possibility 
	}

	public String validValue(final ValidValueOption option) {
		// Ignores the option		
		return generate( expression );
	}

	public String invalidValue(final InvalidValueOption option) {
		// Ignores the option
		
		//return generate( negate( expression ) ); // Xeger has a bug: "end-of-string expected at position XXX

		// Return a random string containing a lot of characters
		// return generate( "[a-zA-z0-9\\.\\-\\_/\\+\\-\\*\\&\\$\\#\\@\\!\\=\\~\\^\\:]*" ); // << Problems with \
		
		// Returns a random string with a random size between 0 and 1025 characters
		return ( new StringRandom() ).between( 0, 1025 );
	}

	public static String negate(final String expression) {
		//
		// ATTENTION:	Xeger has a BUG and negated expressions should be
		//				used this way as a workaround.
		//		
		//return "[^" + expression;
		
		if ( expression.startsWith("[") && expression.endsWith("]") ) {
			return "[^" + expression.substring( 1 );
		}
		return String.format( "[^%s]", expression );
	}
	
	public static String generate(String expression) {			
		Xeger xeger = new Xeger( expression );
		return xeger.generate();
	}
}
