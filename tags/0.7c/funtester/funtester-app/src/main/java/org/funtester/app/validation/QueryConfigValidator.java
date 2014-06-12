package org.funtester.app.validation;

import org.funtester.app.i18n.Messages;
import org.funtester.common.util.Validator;
import org.funtester.core.software.QueryConfig;
import org.funtester.core.software.Software;
import org.funtester.core.util.InvalidValueException;

/**
 * Validator for a {@link QueryConfig}.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class QueryConfigValidator implements Validator< QueryConfig > {
	
	private final Software software;
	
	public QueryConfigValidator(Software software) {
		this.software = software;
	}

	@Override
	public void validate(QueryConfig obj) throws Exception {
		
		if ( null == obj.getDatabaseConfig() ) {
			final String msg = Messages.alt( "_QUERY_CONFIG_DATABASE_INVALID", "Please, select a database." );
			throw new InvalidValueException( msg, "database" );
		}
		
		checkRegEx( "name", "[\\p{L}0-9 \\'\\-_.]{1,50}", obj.getName() );
		
		// do not validate *command* for now
		
		// If there is an object equals to the current object and their Ids
		// are different, both have the same values and the object under
		// validation is invalid (cannot contains the same).
		
		// Same database configuration and ( command or name )
		final int index = software.indexOfQueryConfiguration( obj );
		if ( index < 0 ) { return; }
		final QueryConfig other = software.queryConfigurationAt( index );
		if ( obj.getId() != other.getId() ) {
			String msg = Messages.getString( "_QUERY_CONFIG_ALREADY_EXISTS" );
			throw new InvalidValueException( msg, "command" );
		}
	}
	
	private void checkRegEx(
			final String attrName,
			final String regex,
			final String value
			) throws InvalidValueException {
		if ( ! value.matches( regex ) ) {
			final String i18nKey = String.format( "_QUERY_CONFIG_%s_INVALID", attrName.toUpperCase() );
			final String i18nAltKey = attrName + " does not match the regex \"%s\".";
			final String msg = String.format( Messages.alt( i18nKey, i18nAltKey ), regex );
			throw new InvalidValueException( msg, attrName );
		}
	}

}
