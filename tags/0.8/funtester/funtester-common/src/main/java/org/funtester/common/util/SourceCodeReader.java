package org.funtester.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Read a source code command line. This is useful to get the line where a
 * test broke.  
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class SourceCodeReader {
	
	private static final CharSequence SEMICOLON = ";";
	
	/**
	 * Read a command starting at the desired line.
	 * 
	 * @param stream	the input stream.
	 * @param line		the desired line to start reading.
	 * @return			the command line content.
	 * @throws IOException
	 */
	public String readCommandStartingAtLine(
			InputStream stream,
			final int line
			) throws IOException {
		BufferedReader br = new BufferedReader( new InputStreamReader( stream ) );
		// Read the lines before the desired line
		int i = 1;
		while ( br.ready() && i < line ) {
			br.readLine(); // Ignore content			
			++i;
		}
		// Now read the desired line until to find a semicolon
		String command = "";
		while ( br.ready() && ! command.contains( SEMICOLON ) ) {
			command += br.readLine();
		}
		return command;
	}

}
