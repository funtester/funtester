package org.funtester.app;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;

import org.funtester.common.util.TextFileUtil;

/**
 * Copyright remover
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class CopyrightRemover {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		final String dirPath = System.getProperty( "user.dir" );

		process( dirPath );
	}
	
	
	private static void process(final String dirPath) {
		
		File dir = new File( dirPath );
		if ( ! dir.exists() ) {
			System.out.println( "Not exists." );
			return;
		}

		FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.matches( ".*(.java)" );
			}
		};
		
		String files[] = dir.list( filter );
		if ( null == files || files.length < 1 ) {
			System.out.println( "No Java files at " + dirPath );
		}
		else {
			analyzeFiles( dirPath, files );
		}
		
		FileFilter dirFilter = new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.isDirectory();
			}
		};
		
		File [] subdirs = dir.listFiles( dirFilter );
		for ( File subdir : subdirs ) {
			process( subdir.getPath() );
		}
	}
	
	private static void analyzeFiles(String dirPath, String[] files) {
		try {
			
			for ( String fileName : files ) {
				final String path = dirPath + File.separator + fileName;
				
				System.out.println( "File is " + path );
				StringBuffer sb = TextFileUtil.loadContent( path );
				int packageIndex = sb.indexOf( "package" );
				if ( packageIndex <= 0 ) { continue; }
				
				//String subStr = sb.substring( 0, packageIndex - 1 );
				//System.out.println( "Header: " + subStr );
				
				sb.replace( 0, packageIndex, "" );
				
				// Overwrite
				TextFileUtil.saveContent( sb.toString(), path );
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
