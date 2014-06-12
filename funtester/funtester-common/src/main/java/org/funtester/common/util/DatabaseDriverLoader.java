package org.funtester.common.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Driver;

/**
 * Load database drivers.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class DatabaseDriverLoader {
	
	/**
	 * The loader needs to be an attribute: it has to be kept alive after
	 * loading the drivers. */
	private final JarFileLoader loader = new JarFileLoader();
	
	public DatabaseDriverLoader() {
	}
	
	/**
	 * Load a JAR file with the given file name and create a instance of the
	 * driver class.
	 * 
	 * @param driverFileName
	 * @param driverClassName
	 * @return
	 * @throws FileNotFoundException
	 * @throws DriverLoadException
	 */
	public java.sql.Driver loadDriver(
			final String driverFileName,
			final String driverClassName
			)
		throws FileNotFoundException, DriverLoadException {
		
		final File f = new File( driverFileName );
		if ( ! f.exists() ) {
			throw new FileNotFoundException( driverFileName );
		}

		try {
			loader.addJarFile( f.getAbsolutePath() );
			final Class< ? > clazz = loader.loadClass( driverClassName );
			return (Driver) clazz.newInstance();
		} catch (Exception e) {
			throw new DriverLoadException( driverClassName );
		}
	}

}
