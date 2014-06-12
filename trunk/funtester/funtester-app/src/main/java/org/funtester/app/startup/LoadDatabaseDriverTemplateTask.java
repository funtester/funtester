package org.funtester.app.startup;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import org.funtester.app.common.DefaultFileExtensions;
import org.funtester.app.i18n.Messages;
import org.funtester.app.project.AppConfiguration;
import org.funtester.app.project.AppState;
import org.funtester.app.repository.DatabaseDriverTemplateRepository;
import org.funtester.app.repository.json.JsonDatabaseDriverTemplateRepository;
import org.funtester.core.software.DatabaseDriverConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Task for loading the database driver templates.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class LoadDatabaseDriverTemplateTask implements Task {
	
	private static final Logger logger = LoggerFactory.getLogger( LoadDatabaseDriverTemplateTask.class ); 
	private static final String FILE_PATTERN = ".*(." + DefaultFileExtensions.DATABASE_DRIVER_TEMPLATE + ")";

	@Override
	public String description() {
		return Messages.alt( "_JDBC_TEMPLATE_LOADING", "Loading JDBC drivers templates..." );
	}

	@Override
	public boolean canContinueInCaseOfError() {
		return true;
	}

	@Override
	public void perform(String[] args, AppState appState)
			throws Exception {
		
		// Put the parameter
		
		Map< DatabaseDriverConfig, String > driverTemplateMap =
				new LinkedHashMap< DatabaseDriverConfig, String >();
		
		appState.setDriverTemplateMap( driverTemplateMap );

		// Get the plug-in directory from the application configuration
		
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

		// Load the templates
		
		for ( File f : dir.listFiles() ) {
			
			final String fileName = f.getName();
			if ( ! fileName.matches( FILE_PATTERN ) ) { continue; }
			
			final String templatePath = dirPath.endsWith( File.separator ) ?
					dirPath + fileName : dirPath + File.separator + fileName;
			
			logger.debug( "Detected template path: " + templatePath );
			
			try {
				DatabaseDriverTemplateRepository repo = new JsonDatabaseDriverTemplateRepository( templatePath );
				DatabaseDriverConfig template = repo.first();
				driverTemplateMap.put( template, fileName );
			} catch (Exception e) {
				// Just log the error if it could not load the plugin
				logger.error( e.getLocalizedMessage() );
			}			
		}
		
	}

}
