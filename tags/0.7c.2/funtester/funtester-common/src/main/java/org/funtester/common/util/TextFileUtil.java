package org.funtester.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Text file utilities.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public final class TextFileUtil {

	private TextFileUtil() { }


	/**
	 * Save a string content to a file.
	 *  
	 * @param fileName	File name.
	 * @param content	Content to save.
	 * @throws IOException
	 */
	public static void saveContent(final String content, final String fileName)
			throws IOException {
		File file = new File( fileName );
		file.delete();
		OutputStreamWriter writer = createWriter( fileName, false );
		try {
			writer.write( content );
		} finally {
			writer.close();
		}
	}
	
	/**
	 * Add a content at the end of the file.
	 * 
	 * @param content	the content to add.
	 * @param fileName	the file name.
	 * @throws IOException
	 */
	public static void appendContent(final String content, final String fileName)
			throws IOException {
		
		OutputStreamWriter writer = createWriter( fileName, true );
		try {
			writer.append( content );
		} finally {
			writer.close();
		}
	}

	/**
	 * Load a text file content.
	 * 
	 * @param fileName	the file to be read.
	 * @return			a {@link StringBuffer} containing the file content.
	 * @throws IOException
	 */
	public static StringBuffer loadContent(final String fileName) throws IOException {
		InputStream is = new FileInputStream( fileName );
		BufferedReader reader = new BufferedReader( new InputStreamReader( is, "UTF-8" ) );
		
		final String lineSeparator = System.getProperty( "line.separator" );
		
		StringBuffer sb = new StringBuffer();
		try {
			String line;
			while ( reader.ready() && ( line = reader.readLine() ) != null ) {
				sb.append( line );
				sb.append(  lineSeparator );
			}
		} finally {
			reader.close();
		}
		return sb;
	}
	
	/**
	 * Create a {@link OutputStreamWriter} for a file.
	 * 
	 * @param fileName	the name of the file.
	 * @param append	whether the writer should write at the end of the file.
	 * @return
	 * @throws IOException
	 */
	private static OutputStreamWriter createWriter(
			final String fileName,
			final boolean append
			) throws IOException {
		
		FileOutputStream out = new FileOutputStream( fileName, append );
		return new OutputStreamWriter( out, "UTF-8" );
	}
}
