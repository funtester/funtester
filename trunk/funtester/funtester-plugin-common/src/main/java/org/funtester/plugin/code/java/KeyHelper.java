package org.funtester.plugin.code.java;

/**
 * Key helper
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class KeyHelper {
	
	private static final String KEY_SEPARATOR = ", ";
	private static final String VK = "VK_%s";
	
	private KeyHelper() {}

	public static String toVirtualKeySequence(final String ...keys) {
		StringBuffer sb = new StringBuffer();
		boolean useSeparator = false;
		for (String key : keys) {
			if ( useSeparator ) {
				sb.append( KEY_SEPARATOR );
			}
			//System.out.println( "*************************** vkey is " + toVirtualKey( key ));
			sb.append( toVirtualKey( key ) );
			useSeparator = true;
		}
		return sb.toString();
	}
	
	public static String toVirtualKey(final String key) {	
		if ( isControl( key ) ) { return "controlOrCommandKey()"; // return "VK_CONTROL"; // << MacOS support
		} else if ( isEscape( key ) ) { return "VK_ESCAPE";
		} else if ( isReturn( key ) ) { return "VK_ENTER";				
		}
		// Shift, Alt, Tab, Letter, Function
		return String.format( VK, key.trim().toUpperCase() );
	}

	public static boolean isLetter(final String key) {
		return key.toUpperCase().matches( "[A-Z]" );
	}

	public static boolean isControl(final String key) {
		return key.equalsIgnoreCase( "CONTROL" )
			|| key.equalsIgnoreCase( "CTRL" )
			|| key.equalsIgnoreCase( "COMMAND" )
			|| key.equalsIgnoreCase( "CMD" );
	}
	
	public static boolean isEscape(final String key) {
		return key.equalsIgnoreCase( "ESCAPE" )
			|| key.equalsIgnoreCase( "ESC" );
	}
	
	public static boolean isReturn(final String key) {
		return key.equalsIgnoreCase( "RETURN" )
			|| key.equalsIgnoreCase( "ENTER" );
	}
	
	public static boolean isShift(final String key) {
		return key.equalsIgnoreCase( "SHIFT" );
	}
	
	public static boolean isAlt(final String key) {
		return key.equalsIgnoreCase( "ALT" );
	}	
	
	public static boolean isTab(final String key) {
		return key.equalsIgnoreCase( "TAB" );
	}
	
	public static boolean isFunction(final String key) {
		return key.toUpperCase().matches( "(F([1-9]|1[0-2]))" );
	}
	
	/*
	public static void main(String [] args) {
		String key;
		for ( int i = 1; i <= 12; ++i ) {
			key = "F" + i;
			System.out.println( key.toUpperCase().matches( "(F([1-9]|1[0-2]))" ) );
		}
	}
	*/
}
