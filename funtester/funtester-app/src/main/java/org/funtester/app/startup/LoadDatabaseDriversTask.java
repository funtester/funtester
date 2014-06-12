package org.funtester.app.startup;

import java.io.File;
import java.sql.Driver;
import java.util.Collection;
import java.util.Map;

import org.funtester.app.common.DefaultFileExtensions;
import org.funtester.app.i18n.Messages;
import org.funtester.app.project.AppConfiguration;
import org.funtester.app.project.AppState;
import org.funtester.common.util.DatabaseDriverLoader;
import org.funtester.core.process.rule.DriverCache;
import org.funtester.core.software.DatabaseDriverConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Task for loading the database drivers.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class LoadDatabaseDriversTask implements Task {
	
	private static final String FILE_PATTERN = ".*(." + DefaultFileExtensions.DATABASE_DRIVER + ")";
	
	private static Logger logger = LoggerFactory.getLogger( LoadDatabaseDriversTask.class );

	@Override
	public String description() {
		return Messages.alt( "_DATABASE_DRIVER_LOADING", "Loading database drivers..." );
	}

	@Override
	public boolean canContinueInCaseOfError() {
		return false;
	}

	@Override
	public void perform(final String args[], AppState appState)
			throws Exception {
		
		// Put the parameter
		
		DriverCache driverCache = new DriverCache();
		appState.setDriverCache( driverCache );
		
		// Get the driver templates
		
		Map< DatabaseDriverConfig, String > driverTemplateMap =
				appState.getDriverTemplateMap();
		
		if ( null == driverTemplateMap ) {
			final String msg = Messages.alt( "_CONFIGURATION_READING_ERROR", "Error while getting the configuration." );
			throw new Exception( msg );			
		}		
		
		// Get the driver directory from the application configuration
		
		AppConfiguration cfg = appState.getConfiguration();
		if ( null == cfg ) {
			final String msg = Messages.alt( "_CONFIGURATION_READING_ERROR", "Error while getting the configuration." );
			throw new Exception( msg );
		}
		
		String dirPath = cfg.getDatabaseDriverDirectory();
		if ( null == dirPath ) {
			dirPath = AppConfiguration.DEFAULT.getDatabaseDriverDirectory();
		}
		final File dir = new File( dirPath );
		if ( ! dir.isDirectory() ) {
			throw new Exception( "Not a directory: " + dir.getAbsolutePath() );
		}

		// Load the drivers
	
		DatabaseDriverLoader loader = new DatabaseDriverLoader();
		
		final Collection< DatabaseDriverConfig > templates =
				driverTemplateMap.keySet();
		
		for ( DatabaseDriverConfig driverCfg : templates ) {
			
			// Ignore empty file names
			if ( driverCfg.getDriverFile().trim().isEmpty() ) { continue; }
			
			final String fileName = driverCfg.getDriverFile();
			
			final String driverPath = dirPath.endsWith( File.separator )
					? dirPath + fileName
					: dirPath + File.separator + fileName;
			
			logger.debug( "Driver path is: " + driverPath );
			
			if ( ! ( new File( driverPath ) ).exists() ) {
				continue;
			}
			
			if ( ! driverPath.matches( FILE_PATTERN ) ) {
				continue;
			}

			logger.debug( "Recognized \"" + driverCfg.getName() + "\" driver: " + driverPath );
			
			final Driver driver = loader.loadDriver( driverPath, driverCfg.getDriverClass() );
			//
			// Currently supports just one driver per class name
			//
			final String driverKey = DriverCache.makeKey( driverCfg.getDriverClass() );
			
			logger.debug( "Driver loaded successfully: " + driverKey );
			
			driverCache.put( driverKey, driver );
		}
		
		/*
		Map< String, String > drivers = new HashMap< String, String >();
		 
		for ( File f : dir.listFiles() ) {
			
			final String fileName = f.getName();
			if ( ! fileName.matches( FILE_PATTERN ) ) { continue; }
			logger.debug( "Detected driver file: " + fileName );
			
			
			
			// Using database type to find the JAR name
			for ( DatabaseConfig dbc : DatabaseConfigTemplate.ALL ) {
				
				if ( fileName.toLowerCase().contains( dbc.getType().toLowerCase() ) ) {
					
					final String driverPath = driverDir.endsWith( File.separator ) ?
							driverDir + fileName : driverDir + File.separator + fileName;
					
					logger.debug( "Recognized \"" + dbc.getType() + "\" driver: " + driverPath );
					
					drivers.put( dbc.getType(), driverPath );
				}
			}
		}
		*/
		
	}
}
