package org.funtester.common.util;

/**
 * Useful methods to compare object's equality.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public final class EqUtil {
	
	private enum CompareResult { BOTH_ARE_NULL, DIFFERENT, MAY_BE_EQUAL };
	
	private EqUtil() {}
	
	/**
	 * Compare if two objects are equal.
	 * 
	 * @param a	the first object to be compared.
	 * @param b	the second object to be compared.
	 * @return	true if they are considered equal, false otherwise.
	 */
	public static boolean equals(final Object a, final Object b) {
		final CompareResult r = preCompare( a, b );
		switch ( r ) {
			case BOTH_ARE_NULL	: return true;
			case MAY_BE_EQUAL	: return a.equals( b );
			default				: return false;
		}
	}

	/**
	 * Compare if two strings are equal, ignoring the case sensitive option.
	 * 
	 * @param a	the first string to be compared.
	 * @param b	the second string to be compared.
	 * @return	true if they are considered equal, false otherwise.
	 */	
	public static boolean equalsIgnoreCase(final String a, final String b) {
		final CompareResult r = preCompare( a, b );
		switch ( r ) {
			case BOTH_ARE_NULL	: return true;
			case MAY_BE_EQUAL	: return a.equalsIgnoreCase( b );
			default				: return false;
		}
	}
	
	public static boolean equalsAdresses(final Object a, final Object b) {
		final CompareResult r = preCompare( a, b );
		switch ( r ) {
			case BOTH_ARE_NULL	: return true;
			case MAY_BE_EQUAL	: return a == b; // Compare addresses
			default				: return false;
		}
	}
	
	private static CompareResult preCompare(final Object a, final Object b) {
		// If one is null and another is not, then they are different
		if ( null == a ^ null == b ) {
			return CompareResult.DIFFERENT;
		// If both are null, return this result
		} else if ( null == a && null == b ) {
			return CompareResult.BOTH_ARE_NULL;
		}	
		// Then they may be equal
		return CompareResult.MAY_BE_EQUAL;
	}
	
}
