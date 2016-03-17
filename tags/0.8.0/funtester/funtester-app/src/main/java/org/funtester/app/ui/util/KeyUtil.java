package org.funtester.app.ui.util;

import javax.swing.KeyStroke;

/**
 * Key related utilities.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class KeyUtil {
	
	private KeyUtil() {}

	/**
	 * Gives the description of a {@link KeyStroke}.
	 * 
	 * @param ks	the key stroke.
	 * @return		the description.
	 */
	public final static String keyDescription(final KeyStroke ks) {
		if ( null == ks ) {
			return "";
		}
		String acceleratorKey = ks.toString();
		if ( acceleratorKey.startsWith( "pressed" ) ) {
			acceleratorKey = acceleratorKey.replace( "pressed ", "" );
		} else {
			acceleratorKey = acceleratorKey.replace( "pressed ", "+ " ).toUpperCase();
		}
		return acceleratorKey;
	}

}
