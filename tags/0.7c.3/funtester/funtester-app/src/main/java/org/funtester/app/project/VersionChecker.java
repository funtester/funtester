package org.funtester.app.project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Check for new project versions.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class VersionChecker {

	/**
	 * This is a simple text file containing three lines, each one with a
	 * piece of the version, in the following order: major, minor, release. 
	 */
	public static final String versionCheckURL = "http://funtester.org/last-version.txt";
	
	/**
	 * Return the newer project version.
	 * 
	 * @return a {@code Version} object.
	 * @throws VersionCheckException
	 */
	public Version newerVersion() throws VersionCheckException {
		return readVersionFromURL( versionCheckURL );
	}
	
	/**
	 * Read a version from a URL. The URL should return three lines separated
	 * by "\n", "\r" or "\r\n". Each line should have a piece of the version,
	 * in the following order: major, minor, release.
	 * 
	 * @param aURL	the URL to be read.
	 * @return		a {@code Version} object.
	 * @throws VersionCheckException
	 */
	public Version readVersionFromURL(final String aURL)
			throws VersionCheckException {
		BufferedReader reader = null;
		try {
			
			URL url = new URL( aURL );
			reader = new BufferedReader( new InputStreamReader( url.openStream() ) );
			
			String major = reader.readLine();
			String minor = reader.readLine();
			String release = reader.readLine();
			
			return new Version( major, minor, release );
			
		} catch (MalformedURLException mue) {
			throw new VersionCheckException( "Malformed URL: " + mue.getLocalizedMessage() );
		} catch (IOException ioe) {
			throw new VersionCheckException( "Error while trying to access or reading the content.", ioe );
		} catch (Exception e) {
			throw new VersionCheckException( e.getLocalizedMessage() );
		} finally {
			if ( reader != null ) {
				// Close the reader and ignore exceptions
				try { reader.close(); } catch ( IOException e ) { }
			}
		}
	}
	
	
	/**
	public static void main(String[] args) {
		VersionChecker c = new VersionChecker();
		try {
			System.out.println( c.currentVersion() );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	**/

}
