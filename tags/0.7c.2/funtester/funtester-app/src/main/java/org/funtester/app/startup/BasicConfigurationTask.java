package org.funtester.app.startup;

import java.io.File;

import org.funtester.app.common.DefaultFileExtensions;
import org.funtester.app.i18n.Messages;
import org.funtester.app.project.AppInfo;
import org.funtester.app.project.AppState;
import org.funtester.app.project.AppVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Task for defining the basic configurations for the application.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class BasicConfigurationTask implements Task {
	
	private static final Logger logger =
			LoggerFactory.getLogger( BasicConfigurationTask.class );

	private final StartupListener listener;

	public BasicConfigurationTask(StartupListener listener) {
		this.listener = listener;
	}

	@Override
	public String description() {
		return Messages.alt( "_CONFIGURATING", "Configurating..." );
	}

	@Override
	public boolean canContinueInCaseOfError() {
		return false;
	}

	@Override
	public void perform(String[] args, AppState appState)
			throws Exception {
		
		final String executionDirectory = System.getProperty( "user.dir" );
		final String translationsDirectory = translationDirectoryFor( executionDirectory );
		logger.debug( "Translation directory: " + translationsDirectory );
		
		final AppInfo appInfo = new AppInfo( "FunTester", AppVersion.CURRENT,
				"http://funtester.org", "http://funtester.org/download" );
		
		appState.setAppInfo( appInfo );
		appState.setConfigurationFile( "funtester." + DefaultFileExtensions.CONFIGURATION );
		appState.setExecutionDirectory( executionDirectory );
		appState.setTranslationsDirectory( translationsDirectory );
		appState.setLookAndFeelName( "Windows" );
		
		listener.versionRead( "v" + appInfo.getVersion() );
	}

	/**
	 * Return the translation directory according to the current directory.
	 * 
	 * @param currentDirectory	the current directory.
	 * @return
	 */
	private String translationDirectoryFor(final String currentDirectory) {
		
		// First try
		String translationsDirectory = currentDirectory + "/i18n";
		if ( ( new File( translationsDirectory ) ).exists() ) {
			return translationsDirectory;
		}
		
		// Second try
		translationsDirectory = "src/main/resources/i18n";	
		if ( ( new File( translationsDirectory ) ).exists() ) {
			return translationsDirectory;
		}
		
		// No more tries -> return the current directory
		return currentDirectory;
	}

}
