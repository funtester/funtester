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
	 * Return a relative path for an absolute path.
	 * This method does NOT touch the file system.
	 * 
	 * @param absolutePath	the absolute path to convert.
	 * @param referenceDir	the directory for reference.
	 * @return				a relative path.
	 */
	public static String toRelativePath(
			final String absolutePath,
			final String referenceDir
			) {
		
		// Transform "\" to "/"
		String absPath = absolutePath.replaceAll( "\\\\", "/" );
		String refDir = referenceDir.replaceAll( "\\\\", "/" );
		
		// Add a "/" in the reference directory, if needed
		if ( ! refDir.endsWith( "/" ) ) {
			refDir += "/"; 
		}
		
		// If both are directories, and they are equal, then return "/"
		if ( absPath.equalsIgnoreCase( refDir ) ) {
			return "/";
		}
		
		// Reference directory is in the absolute path
		int indexOfRefDirInAbsPath = absPath.toLowerCase().indexOf( refDir.toLowerCase() ); 
		if ( indexOfRefDirInAbsPath >= 0 ) {
			return absPath.substring( indexOfRefDirInAbsPath + refDir.length() );
		}
		
		// Part of the reference directory is expected to be in the absolute path
		
		String splittedRefDir [] = refDir.split( "/" );
		String splittedAbsPath [] = absPath.split( "/" );
		
		final int refLen = splittedRefDir.length;
		final int absLen = splittedAbsPath.length;
		int lastEqualIndex = -1;
		
		// Save the last index where the paths are equal
		
		for ( int i = 0; i < absLen && i < refLen; ++i ) {

			if ( splittedAbsPath[ i ].equalsIgnoreCase( splittedRefDir[ i ] ) ) {
				lastEqualIndex = i;
			} else {
				break;
			}
		}
		
		// The paths have nothing in common, so return the original absolute path
		if ( lastEqualIndex < 0 ) {
			return absolutePath;
		}

		// They have a common root
		StringBuilder path = new StringBuilder();
		boolean addedRelPath = false;
		
		// Add a ../ for each directory of the relative path after the common root
		for ( int ri = lastEqualIndex + 1; ri < refLen; ++ri ) {
			path.append( "../" );
			addedRelPath = true;
		}
		
		// Add the pieces of the absolute path after the common root
		for ( int ai = lastEqualIndex + 1; ai < absLen; ++ai ) {
			if ( ai > lastEqualIndex + 1 
					|| ( ( ai == lastEqualIndex + 1 ) && ! addedRelPath ) ) {
				path.append( "/" );
			}
			path.append( splittedAbsPath[ ai ] );
		}
		
		return path.toString();
	}
	
	
	/**
	 * Return an absolute path from a relative path.
	 * This method does NOT touch the file system.
	 * 
	 * @param relativePath	the relative path.
	 * @param currentDir	the current directory.
	 * @return				an absolute path.
	 */
	public static String toAbsolutePath(
			final String relativePath,
			final String currentDir
			) {
		
		String path = relativePath.replaceAll( "\\\\", "/" );
		String currDir = currentDir.replaceAll( "\\\\", "/" );
		
		if ( path.isEmpty() || path.equals( "." ) || path.equals( "./" ) ) {
			return currentDir;
		}
		
		
		if ( ! path.contains( ".." ) ) {
			final boolean b1 = currDir.endsWith( "/" );
			final boolean b2 = path.startsWith( "/" );
			
			if ( b1 && b2 ) {
				return currDir + path.substring( 1 );
			} else if ( b1 || b2 ) {
				return currDir + path;
			} else {
				return currDir + "/" + path;
			}
		} 
		
		if ( ! currDir.endsWith( "/" ) ) { currDir += "/"; }
		
		
		int dirIdx = currDir.length() - 1 - "/".length();
		
		int pathIdx = "../".length() - 1;
		
		while ( pathIdx >= 0 ) {
			dirIdx = currDir.lastIndexOf( "/", dirIdx - 1 );
			
			pathIdx = path.indexOf( "../", pathIdx + 1 );
		}

		return 
				directoryWithSeparator( currDir.substring( 0, dirIdx ) ) +
				path.substring(	path.lastIndexOf( "../" ) + "../".length() )
				;
		
		/*
		
		
		
		// count occurrences of "../"
		int count = path.length() - path.replace( "../", "" ).length();
		System.out.println( "count: " + count);
		
		int lastIdx = currDir.length() - 1 - "/".length();
		int x = 1;
		while ( x < count && lastIdx >= 0 ) {
			lastIdx = currDir.lastIndexOf( "/", lastIdx - 1 );
			x++;
		}
		
		return currDir.substring( 0, lastIdx ) +
				path.substring( path.lastIndexOf( "../" ) + "../".length() );
		
		*/
		
		
		
		/* OLD
		int pathIndex = path.lastIndexOf( ".." );
		int dirIndex = curDir.lastIndexOf( "/" );
		while ( pathIndex >= 0 && dirIndex > 0 ) {
			dirIndex = curDir.substring( 0, dirIndex > 0 ? dirIndex - 1 : dirIndex ).lastIndexOf( "/" );
			pathIndex = path.substring( 0, pathIndex > 0 ? pathIndex - 1 : pathIndex ).lastIndexOf( ".." );
		}
		*/
		
		/*
		
		String tmpPath = path;
		String tmpDir = currDir;
		int upDirIndex, slashIndex;
		while ( ( upDirIndex = tmpPath.indexOf( ".." ) ) >= 0
				&&  ( slashIndex = tmpDir.lastIndexOf( "/" ) ) >= 0 ) {
			//System.out.println( "tmpPath=" + tmpPath + ", " + "tmpDir=" + tmpDir );
			tmpPath = tmpPath.substring( upDirIndex + 3 ); // 3 == "../".length()
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
