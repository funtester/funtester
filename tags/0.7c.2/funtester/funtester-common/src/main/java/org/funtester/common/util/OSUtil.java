package org.funtester.common.util;

/**
 * Operating System (OS) information.
 * 
 * @author Thiago Delgado Pinto
 * 
 * @see http://lopica.sourceforge.net/os.html
 *
 */
public final class OSUtil {
	
	private OSUtil() {}
	
	/**
	 * Return the current OS name.
	 * 
	 * @return
	 */
	public static String operatingSystemName() {
		return System.getProperty( "os.name" );
	}

	/**
	 * Return <code>true</code> whether the current OS is Windows and
	 * <code>false</code> otherwise.
	 *  
	 * @return
	 */
	public static boolean isWindows() {
		return isWindows( operatingSystemName() );
	}
	
	/**
	 * Return <code>true</code> whether the given OS name is Windows and
	 * <code>false</code> otherwise.
	 *  
	 * @param osName	the operating system name
	 * @return
	 */
	public static boolean isWindows(final String osName) {
		return ( osName.toLowerCase().indexOf( "win" ) >= 0 );
	}	

	/**
	 * Return <code>true</code> whether the current OS is Mac and
	 * <code>false</code> otherwise.
	 *  
	 * @return
	 */
	public static boolean isMac() {
		return isMac( operatingSystemName() );
	}
	
	/**
	 * Return <code>true</code> whether the given OS name is Mac and
	 * <code>false</code> otherwise.
	 *  
	 * @param osName	the operating system name
	 * @return
	 */
	public static boolean isMac(final String osName) {
		return ( osName.toLowerCase().indexOf( "mac" ) >= 0 );
	}

	/**
	 * Return <code>true</code> whether the current OS is Unix/Linux/FreeBSD
	 * and <code>false</code> otherwise.
	 *  
	 * @return
	 */
	public static boolean isUnix() {
		return isUnix( operatingSystemName() );
	}
	
	/**
	 * Return <code>true</code> whether the given OS name is a Unix
	 * (Unix/Linux/HP-UX/FreeBDS/Raspberry Pi)
	 * and <code>false</code> otherwise.
	 *  
	 * @param osName	the operating system name
	 * @return
	 */
	public static boolean isUnix(final String osName) {
		final String os = osName.toLowerCase();
		return os.indexOf( "nix" ) >= 0
			|| os.indexOf( "nux" ) >= 0 // Raspberry Pi is also a Linux
			|| os.indexOf( "hp" ) >= 0
			|| os.indexOf( "freebsd" ) >= 0
			;
	}	

	/**
	 * Return <code>true</code> whether the current OS is Solaris and
	 * <code>false</code> otherwise.
	 *  
	 * @return
	 */	
	public static boolean isSolaris() {
		return isSolaris( operatingSystemName() );
	}
	
	/**
	 * Return <code>true</code> whether the given OS name is Solaris and
	 * <code>false</code> otherwise.
	 *  
	 * @param osName	the operating system name
	 * @return
	 */	
	public static boolean isSolaris(final String osName) {
		return ( osName.toLowerCase().indexOf( "sunos" ) >= 0 );
	}
	
}
