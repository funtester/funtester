package org.funtester.app.startup;

import java.io.File;

import org.funtester.app.i18n.Messages;
import org.funtester.app.project.AppConfiguration;
import org.funtester.app.project.AppState;
import org.funtester.app.repository.AppConfigurationRepository;
import org.funtester.app.repository.json.JsonAppConfigurationRepository;

/**
 * Task for loading the configuration file.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class LoadConfigurationTask implements Task {
	
	@Override
	public String description() {
		return Messages.alt( "_CONFIGURATION_READING", "Reading configuration..." );
	}

	@Override
	public boolean canContinueInCaseOfError() {
		return true;
	}

	@Override
	public void perform(final String args[], AppState appState)
			throws Exception {
		
		AppConfiguration cfg = AppConfiguration.DEFAULT;
		
		try {
			
			final String fileName = appState.getConfigurationFile();
			if ( null == fileName ) {
				throw new Exception( String.format( Messages.alt(
						"_PARAMETER_NOT_FOUND",
						"Expected parameter not found: \"%s\"." ),
						"configuration file" ) );
			}

			File file = new File( fileName );
			if ( ! file.canRead() ) {
				throw new Exception( String.format( Messages.alt(
						"_CONFIGURATION_FILE_NOT_FOUND",
						"Configuration file not found: \"%s\"." ),
						file.getAbsolutePath() ) );
			}
			AppConfigurationRepository repository =
					new JsonAppConfigurationRepository( fileName );
			
			cfg = repository.first();
			
		} catch (Exception e) {
			
			final String msg = Messages.alt( "_CONFIGURATION_READING_ERROR",
					"Error while reading the configuration." );
			
			throw new Exception( msg + " " + e.getLocalizedMessage() );
			
		} finally { // Use the configuration
			appState.setConfiguration( cfg );
			appState.setLookAndFeelName( cfg.getLookAndFeelName() );
		}
	}

}
