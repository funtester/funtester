package org.funtester.common.util;


/**
 * File path utilities (does NOT touch the file system).
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class FilePathUtil {
	
	private FilePathUtil() {}
	
	/**
	 * Return a relative file path for an absolute file path.
	 * This method does NOT touch the file system.
	 * 
	 * @param absoluteFilePath	the absolute file path to convert.
	 * @param referenceDir		the directory for reference.
	 * @return					a relative file path.
	 */
	public static String toRelativePath(
			final String absoluteFilePath,
			final String referenceDir
			) {
		
		// Down
		
		int index = absoluteFilePath.lastIndexOf( referenceDir );
		if ( index >= 0 ) {
			return absoluteFilePath.substring( index + 1 + referenceDir.length() );
		}
		
		// Up
		
		String path = absoluteFilePath.replaceAll( "\\\\", "/" );
		String refDir = referenceDir.replaceAll( "\\\\", "/" );
		
		int lastSeparatorIndex = path.lastIndexOf( "/" );
		String pathWithoutTheFile = ( lastSeparatorIndex >= 0 )
				? path.substring( 0, lastSeparatorIndex ) : "";
		
		int partOfRefDirIndex = refDir.indexOf( pathWithoutTheFile );
		String restOfRefDir = refDir.substring( partOfRefDirIndex + pathWithoutTheFile.length() );
		int count = restOfRefDir.split( "/" ).length - 1;
		StringBuilder sb = new StringBuilder();
		for ( int i = 0; i < count; ++i ) {
			sb.append( "../" );
		}
		
		String file = path.substring( lastSeparatorIndex + 1 );
		return makeFileName( sb.toString(), file );
	}
	
	/**
	 * Return an absolute file path from a relative file path.
	 * This method does NOT touch the file system.
	 * 
	 * @param relativePath	the relative file path.
	 * @param currentDir	the current directory.
	 * @return				an absolute file path.
	 */
	public static String toAbsolutePath(
			final String relativePath,
			final String currentDir
			) {
		String curDir = currentDir.replaceAll( "\\\\", "/" );
		String path = relativePath.replaceAll( "\\\\", "/" );
		
		if ( ! path.contains( ".." ) ) {
			final boolean b1 = curDir.endsWith( "/" );
			final boolean b2 = path.startsWith( "/" );
			
			if ( b1 && b2 ) {
				return curDir + path.substring( 1 );
			} else if ( b1 || b2 ) {
				return curDir + path;
			} else {
				return curDir + "/" + path;
			}
		} 
		
		if ( ! curDir.endsWith( "/" ) ) { curDir += "/"; }
		
		/*
		int pathIndex = path.lastIndexOf( ".." );
		int dirIndex = curDir.lastIndexOf( "/" );
		while ( pathIndex >= 0 && dirIndex > 0 ) {
			dirIndex = curDir.substring( 0, dirIndex > 0 ? dirIndex - 1 : dirIndex ).lastIndexOf( "/" );
			pathIndex = path.substring( 0, pathIndex > 0 ? pathIndex - 1 : pathIndex ).lastIndexOf( ".." );
		}
		*/
		String tmpPath = path;
		String tmpDir = curDir.substring( 0, curDir.lastIndexOf( "/" ) );
		int relIndex, slashIndex;
		while ( ( relIndex = tmpPath.indexOf( ".." ) ) >= 0
				&&  ( slashIndex = tmpDir.lastIndexOf( "/" ) ) >= 0 ) {
			//System.out.println( "tmpPath=" + tmpPath + ", " + "tmpDir=" + tmpDir );
			tmpPath = tmpPath.substring( relIndex + 3 ); // 3 == "../".length()
			tmpDir = tmpDir.substring( 0, slashIndex );
		}
		//System.out.println( "--" );
		return tmpDir + "/" + tmpPath;
		
		/*
		String file = path.substring( path.lastIndexOf( "/" ) ); // with "/"
		return curDir.substring( 0, dirIndex > 0 ? dirIndex : 0  ) + file;
		*/
	}
	
	/**
	 * Return a given directory with the directory separator.
	 * This method does NOT touch the file system.
	 * 
	 * @param directory	the directory to analyze
	 * @return
	 */
	public static String directoryWithSeparator(final String directory) {
		
		if ( directory.endsWith( "/" ) || directory.endsWith( "\\" ) ) {
			return directory;
		}

		// Use the existing "/", if it exists
		if ( directory.contains( "/" ) ) {
			return directory + "/";
		}

		// Use the existing "\", if it exists
		if ( directory.contains( "\\" ) ) {
			return directory + "\\";
		}
		
		return directory + "/"; // default to "/"
	}
	
	/**
	 * Return a file path without a separator.
	 * This method does NOT touch the file system.
	 * 
	 * @param file
	 * @return
	 */
	public static String fileWithoutSeparator(final String file) {
		
		int index = file.lastIndexOf( "/" );
		if ( index >= 0 ) {
			return file.substring( index + 1 );
		}
		
		index = file.lastIndexOf( "\\" );
		if ( index >= 0 ) {
			return file.substring( index + 1 );
		}

		return file;
	}
	
	/**
	 * Make a file name.
	 * This method does NOT touch the file system.
	 * 
	 * @param directory	Directory
	 * @param fileName	File name
	 * @return			The composed file name (directory + file name)
	 */
	public static String makeFileName(String directory, String fileName) {
		return directoryWithSeparator( directory ) + fileWithoutSeparator( fileName );
	}
	
	/**
	 * Return the file extension.
	 * This method does NOT touch the file system.
	 * 
	 * @param fileName	the file name.
	 * @return
	 */
	public static String fileExtension(final String fileName) {
		final int index = fileName.lastIndexOf( "." );
		if ( index < 0 ) { return ""; }
		return fileName.substring( index );
	}
	
	/**
	 * Return a file name with a new extension.
	 * This method does NOT touch the file system.
	 * 
	 * @param fileName		the current file name.
	 * @param newExtension	the new extension (with or without a dot).
	 * @return
	 */
	public static String changeFileExtension(
			final String fileName,
			final String newExtension
			) {
		final int newExtIndex = newExtension.lastIndexOf( "." );
		// Make a extension with dot (".")
		final String ext = ( newExtIndex < 0 ) ? "." + newExtension : newExtension.substring( newExtIndex );
		
		final int oldExtIndex = fileName.lastIndexOf( "." );
		if ( oldExtIndex <= 0 ) { return ""; }
		return fileName.substring( 0, oldExtIndex ) + ext;
	}

}
