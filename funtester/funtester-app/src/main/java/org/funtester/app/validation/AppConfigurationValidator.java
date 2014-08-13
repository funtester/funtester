package org.funtester.app.validation;

import java.io.File;
import java.util.Collection;

import org.funtester.app.i18n.Messages;
import org.funtester.app.project.AppConfiguration;
import org.funtester.app.project.Directories;
import org.funtester.common.util.FilePathUtil;
import org.funtester.common.util.Validator;
import org.funtester.core.util.InvalidValueException;

/**
 * Validator for an {@link AppConfiguration}.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class AppConfigurationValidator implements Validator< AppConfiguration > {
	
	private final Collection< String > lookAndFeelNames;
	private final String currentDir;

	public AppConfigurationValidator(
			final Collection< String > lookAndFeelNames,
			final String currentDir
			) {
		this.lookAndFeelNames = lookAndFeelNames;
		this.currentDir = currentDir;
	}

	@Override
	public void validate(final AppConfiguration obj) throws Exception {
		
		if ( ! lookAndFeelNames.contains( obj.getLookAndFeelName() ) ) {
			final String msg = String.format( Messages.alt(
					"_THEME_NOT_FOUND", "Theme not found: \"%s\"." ),
					obj.getLookAndFeelName() );
			throw new Exception( msg );
		}
		
		Directories d = obj.getDirectories();
		
		if ( ! isDirectory( d.getVocabulary() ) ) {
			final String msg = String.format( Messages.alt(
					"_VOCABULARY_DIRECTORY_NOT_FOUND",
					"Vocabulary directory not found: \"%s\"." ),
					d.getVocabulary() );
			throw new InvalidValueException( msg, "vocabulary" );
		}
		
		if ( ! isDirectory( d.getProfile() ) ) {
			final String msg = String.format( Messages.alt(
					"_PROFILE_DIRECTORY_NOT_FOUND",
					"Profile directory not found: \"%s\"." ),
					d.getProfile() );
			throw new InvalidValueException( msg, "profile" );
		}
		
		if ( ! isDirectory( d.getDatabaseDriver() ) ) {
			final String msg = String.format( Messages.alt(
					"_DATABASE_DRIVER_DIRECTORY_NOT_FOUND",
					"Database driver directory not found: \"%s\"." ),
					d.getDatabaseDriver() );
			throw new InvalidValueException( msg, "databaseDriver" );
		}
		
		if ( ! isDirectory( d.getPlugin() ) ) {
			final String msg = String.format( Messages.alt(
					"_PLUGIN_DIRECTORY_NOT_FOUND",
					"Plug-in directory not found: \"%s\"." ),
					d.getPlugin() );
			throw new InvalidValueException( msg, "plugin" );
		}
		
		if ( ! isDirectory( d.getManual() ) ) {
			final String msg = String.format( Messages.alt(
					"_MANUAL_DIRECTORY_NOT_FOUND",
					"Manual directory not found: \"%s\"." ),
					d.getManual() );
			throw new InvalidValueException( msg, "manual" );
		}
	}
	
	private boolean isDirectory(final String dir) {
		final String absolutePath = FilePathUtil.toAbsolutePath( dir, currentDir );
		return ( new File( absolutePath ) ).isDirectory();
	}

}
