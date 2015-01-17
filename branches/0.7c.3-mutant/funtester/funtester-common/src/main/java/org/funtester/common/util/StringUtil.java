package org.funtester.common.util;

import java.util.regex.Matcher;

/**
 * String utilities.
 *
 * @author Thiago Delgado Pinto
 *
 */
public final class StringUtil {

	private StringUtil() {}

	/**
	 * Convert C-style formatted text to a regular expression.
	 * <p>
	 * Example: "Quantity is %d and price is %f."
	 * becomes	"(Quantity is ).*.( and price is ).*.(.)"
	 * </p>
	 * <p>
	 * Currently supports %d, %f and %s.
	 * </p>
	 *
	 * @param text	the formatted text.
	 * @return		the regex.
	 */
	public static String convertFormattingToRegEx(final String text) {

		String regEx = normalizeCarriageReturn( text )
				.replaceAll( "(\\()", Matcher.quoteReplacement( "\\\\(" ) )
				.replaceAll( "(\\))", Matcher.quoteReplacement( "\\\\)" ) )
				.replaceAll( "(%d)", ").*.(" )
				.replaceAll( "(%f)", ").*.(" )
				.replaceAll( "(%s)", ").*.(" )
				;

		return "(" + regEx + ")";
	}


	/**
	 * Apply OR operation using regular expression in the strings.
	 * <p>
	 * Example: call with "One", "Two" and "Three" becomes "(One|Two|Three)"
	 * </p>
	 *
	 * @param strings	the strings to be or'ed.
	 * @return			the regex.
	 */
	public static String applyOrOperationWithRegEx(final String ... strings){
		boolean firstOne = true;
		StringBuffer sb = new StringBuffer();
		sb.append( "(" );
		for ( String s : strings ) {
			if ( firstOne ) {
				firstOne = false;
				sb.append( s );
			} else {
				sb.append( "|" ).append( s );
			}
		}
		sb.append( ")" );
		return sb.toString();
	}

	/**
	 * Gets the argument after the supplied one.
	 *
	 * @param args		the program arguments.
	 * @param targetArg	the target argument.
	 * @return			the argument after the target.
	 */
	public static String getArgumentAfter(
			final String[] args, final String targetArg) {
		boolean returnNext = false;
		for ( String a : args ) {
			if ( returnNext ) {
				return a;
			}
			if ( a.equalsIgnoreCase( targetArg ) ) {
				returnNext = true;
			}
		}
		return null;
	}

	/**
	 * Remove all invalid characters for code name.
	 *
	 * @param text	text to remove invalid characters.
	 * @return		a valid code name.
	 */
	public static String removeAllInvalidCharactersForCodeName(
			final String text) {
		return text.replaceAll( "[^A-Za-z0-9_]", "" );
	}

	/**
	 * Builds a name for code purposes (like class name or method name).
	 *
	 * @param name	the name to be converted (ie: use case name)
	 * @return		the converted name.
	 */
	public static String buildNameForCode(final String name) {
		// Break the words to put the first letter of each in uppercase
		String [] strings = name.trim().split( " " );
		StringBuilder sb = new StringBuilder();
		for ( String s : strings ) {
			// Put the first letter in uppercase and the rest in lowercase
			String newText = ( s.length() > 1 )
				? s.substring( 0, 1 ).toUpperCase() + s.substring( 1 ).toLowerCase()
				: s;
			sb.append( newText );
		}
		return removeAllInvalidCharactersForCodeName( sb.toString() );
	}

	/**
	 * Put just the first character in lowercase. Does not change the other ones.
	 *
	 * @param name	the name to be converted;
	 * @return		the converted name.
	 */
	public static String lowerCaseFirstCharacter(final String name) {
		String newName = name.trim();
		if ( newName.isEmpty() ) return "";
		if ( newName.length() == 1 ) {
			return newName.toLowerCase();
		}
		return newName.substring( 0, 1 ).toLowerCase()
			+ newName.substring( 1 );
	}

	/**
	 * Replace \n by \\n.
	 *
	 * @param text	the text to be normalized.
	 * @return		the normalized text.
	 */
	public static String normalizeCarriageReturn(final String text) {
		return text.replaceAll( "\n", "\\\\n" );
	}

	/**
	 * Return the sum of all strings' length.
	 *
	 * @param strings	the strings to get the length.
	 * @return			the sum.
	 */
	public static int sumLength(String ... strings) {
		int len = 0;
		for ( String s : strings ) {
			len += s.length();
		}
		return len;
	}
}
