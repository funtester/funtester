package org.funtester.app.startup;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.funtester.app.i18n.Messages;
import org.funtester.app.project.AppConfiguration;
import org.funtester.app.project.AppState;
import org.funtester.app.repository.json.JsonProfileRepository;
import org.funtester.core.profile.Profile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Task for loading the existing profiles.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class LoadProfilesTask implements Task {
	
	private static final Logger logger = LoggerFactory.getLogger( LoadProfilesTask.class );
	
	@Override
	public String description() {
		return Messages.alt( "_PROFILE_LOADING", "Loading profiles..." );
	}

	@Override
	public boolean canContinueInCaseOfError() {
		return false;
	}

	@Override
	public void perform(final String args[], AppState appState)
			throws Exception {
		
		// Put the parameter
		
		Map< Profile, String > profileMap = new HashMap< Profile, String >();
		appState.setProfileMap( profileMap );
		
		// Get the configuration
		
		AppConfiguration cfg = appState.getConfiguration();
		if ( null == cfg ) {
			final String msg = Messages.alt( "_CONFIGURATION_READING_ERROR", "Error while reading the configuration." );
			throw new Exception( msg );
		}
		
		// Load the profiles
		
		String dirPath = cfg.getProfileDirectory();
		if ( null == dirPath ) {
			dirPath = AppConfiguration.DEFAULT.getProfileDirectory();
		}
		
		File dir = new File( dirPath );
		if ( ! dir.isDirectory() ) {
			final String msg = String.format( Messages.alt(
					"_PROFILE_DIRECTORY_NOT_FOUND",
					"Profile directory not found: %s" ), dir.getAbsolutePath() );
			throw new Exception( msg );
		}
		
		for ( final File file : dir.listFiles() ) {
			final String FILE_PATH = file.getPath();
			logger.debug( "Profile file found: \"" + FILE_PATH + "\"");
			Profile profile = new JsonProfileRepository( FILE_PATH ).first();
			profileMap.put( profile, FILE_PATH );
		}
	}

}
