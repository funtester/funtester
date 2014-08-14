package org.funtester.app.startup;

import java.io.File;

import org.funtester.app.common.DefaultFileExtensions;
import org.funtester.app.i18n.Messages;
import org.funtester.app.project.AppConfiguration;
import org.funtester.app.project.AppState;
import org.funtester.app.project.Directories;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Task for loading the existing profiles.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class LoadManualsTask implements Task {
	
	private static final Logger logger = LoggerFactory.getLogger( LoadManualsTask.class );
	
	public static final String MANUAL_PATTERN = ".*(." + DefaultFileExtensions.MANUAL + ")";
	
	public static String manualPatternForLanguage(final String language) {
		return ".*(" + language.toLowerCase() + ").*(."+ DefaultFileExtensions.MANUAL + ")";
	}
	
	public static String manualPatternForLanguageAndCountry(
			final String language,
			final String country
			) {
		return ".*(" + language.toLowerCase() +
				( country.isEmpty() ? "" : "_" + country.toLowerCase() ) +
				").*(."+ DefaultFileExtensions.MANUAL + ")";
	}
	
	@Override
	public String description() {
		return Messages.alt( "_MANUAL_LOADING", "Loading manuals..." );
	}

	@Override
	public boolean canContinueInCaseOfError() {
		return false;
	}

	@Override
	public void perform(final String args[], AppState appState)
			throws Exception {
		
		// Get the configuration
		
		AppConfiguration cfg = appState.getConfiguration();
		if ( null == cfg || null == cfg.getDirectories() ) {
			final String msg = Messages.alt( "_CONFIGURATION_READING_ERROR", "Error while reading the configuration." );
			throw new Exception( msg );
		}
		
		// Load the manuals
		
		String dirPath = cfg.getDirectories().getManual();
		if ( null == dirPath ) {
			dirPath = Directories.DEFAULT.getManual();
		}
		
		File dir = new File( dirPath );
		if ( ! dir.isDirectory() ) {
			final String msg = String.format( Messages.alt(
					"_MANUAL_DIRECTORY_NOT_FOUND",
					"Manual directory not found: %s" ), dir.getAbsolutePath() );
			throw new Exception( msg );
		}
		
		appState.clearManuals();
		for ( final File file : dir.listFiles() ) {
			if ( file.isDirectory() ) { continue; }
			
			logger.debug( "Manual file : \"" + file.getPath() + "\"");
			
			final String filePath = file.getPath();
			if ( ! filePath.matches( MANUAL_PATTERN ) ) {
				continue;
			}
			
			logger.debug( "Manual file found: \"" + filePath + "\"");
			appState.addManual( filePath );
		}
	}

}
