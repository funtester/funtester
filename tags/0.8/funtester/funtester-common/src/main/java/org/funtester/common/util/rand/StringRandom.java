package org.funtester.common.util.rand;

/**
 * Generates a random string.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class StringRandom {
	
	private final LongRandom longRandom = new LongRandom();
	private final String AVAILABLE_CHARACTERS;
		
	public StringRandom() {
		this.AVAILABLE_CHARACTERS = generateAvailableCharacters();		
	}

	private String generateAvailableCharacters() {
		final int PRINTABLE_ASCII_MIN = 32;
		final int PRINTABLE_ASCII_MAX = 126; // 127 is DEL		
		StringBuffer sbA = new StringBuffer();
		for ( byte i = PRINTABLE_ASCII_MIN; i <= PRINTABLE_ASCII_MAX; ++i ) {
			char c = (char) i;
			if ( '"' == c || '\\' == c ) {
				continue; // Ignore
			}
			sbA.append( c );
		}		
		return sbA.toString();		
	}
	
	/**
	 * Returns a random string with a length.
	 * 
	 * @param length	the length.
	 * @return			a random string.
	 */
	public String exactly(final int length) {
		if ( length < 0 )	throw new IllegalArgumentException( "length should be >= 0" );
		if ( 0 == length ) {
			return "";
		}
		final long LEN = AVAILABLE_CHARACTERS.length();
		StringBuffer sb = new StringBuffer();
		for ( int i = 0; ( i < length ); ++i ) {
			int index = longRandom.between( 0L, LEN - 1 ).intValue();
			assert index >= 0 && index < LEN;
			sb.append( this.AVAILABLE_CHARACTERS.charAt( index ) );			
		}
		return sb.toString();
	}
	
	/**
	 * Returns a random string between a minimum and a maximum length.
	 * 
	 * @param aMin	the minimum length.
	 * @param aMax	the maximum length.
	 * @return		a random string.
	 */
	public String between(final int aMin, final int aMax) {
		if ( aMin < 0 )	throw new IllegalArgumentException( "aMin should be >= 0" );
		if ( aMax < 0 )	throw new IllegalArgumentException( "aMax should be >= 0" );
		if ( aMax < aMin ) throw new IllegalArgumentException( "aMax cannot be lesser than aMin" );
		if ( 0 == aMin && 0 == aMax ) return "";
		// Generating a random length
		final int LENGTH = longRandom.between( new Long( aMin ) , new Long( aMax ) ).intValue(); 		
		return exactly( LENGTH );
	}

}
