package org.funtester.app.validation;

import java.io.File;
import java.util.Collection;

import org.funtester.app.i18n.Messages;
import org.funtester.app.project.AppConfiguration;
import org.funtester.common.util.Validator;

/**
 * Validator for an {@link AppConfiguration}.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class AppConfigurationValidator implements Validator< AppConfiguration > {
	
	private final Collection< String > lookAndFeelNames;

	public AppConfigurationValidator(final Collection< String > lookAndFeelNames) {
		if ( null == lookAndFeelNames ) throw new IllegalArgumentException( "lookAndFellNames could not be null." );
		this.lookAndFeelNames = lookAndFeelNames;
	}

	@Override
	public void validate(final AppConfiguration obj) throws Exception {
		
		if ( ! lookAndFeelNames.contains( obj.getLookAndFeelName() ) ) {
			final String msg = String.format( Messages.alt(
					"_THEME_NOT_FOUND", "Theme not found: \"%s\"." ),
					obj.getLookAndFeelName() );
			throw new Exception( msg );
		}
		
		if ( ! isDirectory( obj.getVocabularyDirectory() ) ) {
			final String msg = String.format( Messages.alt(
					"_VOCABULARY_DIRECTORY_NOT_FOUND",
					"Vocabulary directory not found: \"%s\"." ),
					obj.getVocabularyDirectory() );
			throw new Exception( msg );
		}
		
		if ( ! isDirectory( obj.getProfileDirectory() ) ) {
			final String msg = String.format( Messages.alt(
					"_PROFILE_DIRECTORY_NOT_FOUND",
					"Profile directory not found: \"%s\"." ),
					obj.getProfileDirectory() );
			throw new Exception( msg );			
		}
	}
	
	private boolean isDirectory(final String dir) {
		return ( new File( dir ) ).isDirectory();
	}

}
