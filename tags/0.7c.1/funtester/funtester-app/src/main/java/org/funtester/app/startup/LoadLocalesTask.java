package org.funtester.app.startup;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import org.funtester.app.i18n.Messages;
import org.funtester.app.project.AppState;

/**
 * Task for loading the available locales.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class LoadLocalesTask implements Task {
	
	//private static final String FILE_PATTERN =
	//		"(messages)_?[a-z]{0,3}_?[A-Za-z]{0,3}(." + DefaultFileExtensions.LOCALE + ")";
	
	//private static Logger logger = LoggerFactory.getLogger( LoadLocalesTask.class );

	@Override
	public String description() {
		return Messages.alt( "_LOCALE_LOADING", "Loading idioms..." );
	}

	@Override
	public boolean canContinueInCaseOfError() {
		return true;
	}

	@Override
	public void perform(final String args[], AppState appState)
			throws Exception {
		
		// Put the parameter
		
		Map< Locale, String > localesMap = new LinkedHashMap< Locale, String >();
		appState.setLocalesMap( localesMap );
		
		// ---------------------------------------------------------------------
		//
		// Currently the property files are stored with the JAR file.
		//
		// It could be better to deploy these files outside the JAR, in an
		// "i18n" folder and replace this code with the core below the dashed
		// lines.
		//
		// 
		//
		localesMap.put( Locale.US, "" );
		localesMap.put( new Locale( "pt", "BR" ), "" );
		// ---------------------------------------------------------------------
		
		/*
		// Verify the i18n directory
		
		String dirPath = appState.getTranslationsDirectory();
		if ( null == dirPath ) {
			dirPath = AppState.DEFAULT_TRANSLATION_DIRECTORY;
		}
		
		final File dir = new File( appState.getTranslationsDirectory() );
		if ( ! dir.isDirectory() ) {
			throw new Exception( "Not a translation directory: " + dir.getAbsolutePath() );
		}
		
		for ( File f : dir.listFiles() ) {
			final String NAME = f.getName();
			if ( ! NAME.matches( FILE_PATTERN ) ) {
				logger.debug( "File name: \"" + NAME + "\" does not match." );
				continue;
			}
			logger.debug( "File name: \"" + NAME + "\" matches." );
			final String [] splittedName = NAME.split( "_" );
			Locale l;
			if ( splittedName.length >= 2 ) {
				final String language = splittedName[ 1 ];
				if ( splittedName.length <= 3 ) {
					final String country = splittedName[ 2 ].split( "\\." )[ 0 ];
					l = new Locale( language, country );
				} else {
					l = new Locale( language );
				}
			} else {
				l = Locale.getDefault();
			}			
			localesMap.put( l, NAME );
			logger.debug( "Locale added: " + l );
		}
		*/
		
	}
}
