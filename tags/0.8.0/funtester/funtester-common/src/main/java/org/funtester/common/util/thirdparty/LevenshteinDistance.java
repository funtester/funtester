package org.funtester.common.util.thirdparty;

/**
 * Levenshtein distance algorithm.
 * 
 * @see http://rosettacode.org/wiki/Levenshtein_distance#Java
 * 
 */
public class LevenshteinDistance {

	public static int compute(String s1, String s2) {
		s1 = s1.toLowerCase();
		s2 = s2.toLowerCase();

		int[] costs = new int[ s2.length() + 1 ];
		for ( int i = 0; i <= s1.length(); i++ ) {
			int lastValue = i;
			for ( int j = 0; j <= s2.length(); j++ ) {
				if ( i == 0 )
					costs[ j ] = j;
				else {
					if ( j > 0 ) {
						int newValue = costs[ j - 1 ];
						if ( s1.charAt( i - 1 ) != s2.charAt( j - 1 ) )
							newValue = Math
									.min( Math.min( newValue, lastValue ),
											costs[ j ] ) + 1;
						costs[ j - 1 ] = lastValue;
						lastValue = newValue;
					}
				}
			}
			if ( i > 0 )
				costs[ s2.length() ] = lastValue;
		}
		return costs[ s2.length() ];
	}
	
	/**
	 * Returns the distance in percentage, in relation to the greater string.
	 * The lower the better.
	 * 
	 * Example: "hello" and "halo" distance is 2. "hello" length is 5, then
	 * 			2/5 = 0,4 -> 40%
	 * 
	 * @author Thiago Delgado Pinto
	 */
	public static float inPercentage(final String s1, final String s2) {
		int distance = compute( s1, s2 );
		int s1Length = s1.length();
		int s2Length = s2.length();
		int max = ( s2Length > s1Length ) ? s2Length : s1Length;
		return ( max > 0 ) ? distance / max : 0;
	}

}
