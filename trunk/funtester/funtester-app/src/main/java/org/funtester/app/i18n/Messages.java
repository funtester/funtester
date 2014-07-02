package org.funtester.app.i18n;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import org.funtester.common.util.FileUtil;
import org.funtester.common.util.FilePathUtil;
import org.funtester.common.util.TextFileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Retrieve messages for internationalization (i18n).
 * 
 * @author Thiago Delgado Pinto
 *
 */
public final class Messages {
	
	private static final Logger logger = LoggerFactory.getLogger( Messages.class );
	
	/**
	 * Folder + base name.
	 * 
	 * The file name will be created like this:
	 * <folder> / <base-name> + "_" + <locale> + ".properties
	 * 
	 * Example: "i18n/messages_pt_BR.properties"
	 * */
	public static final String BUNDLE_NAME = "i18n/messages";
	
	/** File with the keys and values not found in the bundle file */
	public static final String TRANSLATIONS_NOT_FOUND_BASE_FILE_NAME = "translations-not-found";
	
	/** Current locale */
	private static Locale currentLocale = Locale.getDefault();
	
	
	private Messages() { }

	/**
	 * Return the message for the given key.
	 * 
	 * @param key
	 * @return
	 */
	public static String getString(String key) {
		final String invalidValue = "!!" + key + "!!";
		try {
			if ( null == currentBundle() ) {
				return invalidValue;
			}
			return currentBundle().getString( key );
		} catch (Exception e) { // MissingResourceException
			logger.warn( e.getLocalizedMessage() );
			return invalidValue;
		}
	}
	
	/**
	 * Verify whether the current bundle contains the desired key.
	 * 
	 * @param key	the key to verify.
	 * @return		true if contains, false otherwise.
	 */
	public static boolean hasString(final String key) {
		return currentBundle().containsKey( key );
	}
	
	/**
	 * Return the message for the given key, or a alternative message whether
	 * the key is not found.
	 * 
	 * @param key				the value key.
	 * @param alternativeValue	the alternative value, to use if the key is not found.
	 * @return					a string.
	 */
	public static String alt(final String key, final String alternativeValue) {
		try {
			if ( hasString( key ) ) {
				return getString( key );
			} else {
				addToBundle( key, alternativeValue );
				return alternativeValue;
			}
		} catch (Exception e) {
			logger.warn( e.getLocalizedMessage() );
			return alternativeValue;
		}
	}
	
	/**
	 * Change the locale to the given locale.
	 * @param locale
	 */
	public static void changeLocaleTo(Locale locale) {
		currentLocale = locale;
		ResourceBundle.clearCache();
	}
	
	/**
	 * Change the locale to the default locale.
	 */
	public static void changeLocaleToDefault() {
		changeLocaleTo( Locale.getDefault() );
	}
	
	/**
	 * Return the current {@link ResouceBundle}.
	 * @return
	 */
	private static ResourceBundle currentBundle() { 
		return ResourceBundle.getBundle( BUNDLE_NAME, currentLocale );
	}
	
	private static void addToBundle(final String key, final String value) {
		addToNotFoundTranslationsFile( key, value );
	}
	
	/**
	 * Add a key and a value to the current bundle file.
	 * 
	 * @param key	the key to add.
	 * @param value	the value to add.
	 */
	private static void addToNotFoundTranslationsFile(final String key, final String value) {

		String currentDir = FilePathUtil.directoryWithSeparator(
				FileUtil.currentDirectoryAsString().replaceAll( "\\\\", "/" ) );
		
		String fileName = ( new StringBuilder() )
				.append( currentDir )
				.append( TRANSLATIONS_NOT_FOUND_BASE_FILE_NAME ).append( "_" )
				.append( currentLocale.getLanguage() ).append( "_" )
				.append( currentLocale.getCountry() )
				.append( ".txt" ).toString();
		
		String content = key + "=" + value;
		
		if ( ( new File( fileName ) ).exists() ) {
			String fileContent = "";
			
			try {
				fileContent = TextFileUtil.loadContent( fileName ).toString();
			} catch ( IOException e ) {
				
			}

			if ( fileContent.contains( key + "=" ) ) {
				return;
			}
		}
		
		
		logger.debug( String.format( "Adding \"%s\" to %s.", content, fileName ) );
		
		try {
			TextFileUtil.appendContent( System.getProperty( "line.separator" ) + content, fileName );
		} catch ( IOException e ) {
			logger.error( e.getLocalizedMessage(), e );
		}
	}
}
