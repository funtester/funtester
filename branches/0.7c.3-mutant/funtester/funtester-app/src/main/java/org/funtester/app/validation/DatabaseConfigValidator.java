package org.funtester.app.validation;

import org.funtester.app.i18n.Messages;
import org.funtester.common.util.Validator;
import org.funtester.core.software.DatabaseConfig;
import org.funtester.core.software.Software;
import org.funtester.core.util.InvalidValueException;

/**
 * Validator for a {@link DatabaseConfig}.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class DatabaseConfigValidator implements Validator< DatabaseConfig > {

	private final Software software;

	public DatabaseConfigValidator(Software software) {
		this.software = software;
	}

	@Override
	public void validate(DatabaseConfig obj) throws Exception {
		checkRegEx( "name", "[\\p{L}0-9 \\-_.]{1,50}", obj.getName() );
		checkRegEx( "driver", "[\\p{L}0-9\\-_.]{1,50}", obj.getDriver() );
		checkRegEx( "dialect", "[\\p{L}0-9\\-_.]{0,50}", obj.getDialect() ); // optional
		checkRegEx( "type", "[\\p{L}0-9\\-_.]{1,50}", obj.getType() );
		checkRegEx( "host", "[\\p{L}0-9\\-_.:/\\\\]{1,300}", obj.getHost() );
		checkRegEx( "port", "[0-9]{0,5}", new Integer( obj.getPort() ).toString() ); // optional
		checkRegEx( "path", "[\\p{L}0-9 \\-_.:/\\\\]{1,500}", obj.getPath() );
		checkRegEx( "user", ".{0,50}", obj.getUser() ); // optional
		checkRegEx( "password", ".{0,50}", obj.getPassword() ); // optional
		
		// If there is an object equals to the current object and their Ids
		// are different, both have the same values and the object under
		// validation is invalid (cannot contains the same).
		final int index = software.indexOfDatabaseConfiguration( obj );
		if ( index < 0 ) { return; }
		final DatabaseConfig other = software.databaseConfigurationAt( index );
		if ( other.getId() != obj.getId() ) {
			final String MSG = Messages.getString( "_DATABASE_CONFIG_ALREADY_EXISTS" );
			throw new InvalidValueException( MSG, "path" );
		}
	}
	
	private void checkRegEx(
			final String attrName,
			final String regex,
			final String value
			) throws InvalidValueException {
		if ( ! value.matches( regex ) ) {
			final String i18nKey = String.format( "_DATABASE_CONFIG_%s_INVALID", attrName.toUpperCase() );
			final String i18nAltMsg = attrName + " does not match the regex \"%s\".";
			final String msg = String.format( Messages.alt( i18nKey, i18nAltMsg ), regex );
			throw new InvalidValueException( msg, attrName );
		}
	}

}
