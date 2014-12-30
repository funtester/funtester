package org.funtester.app.validation;

import java.util.regex.Pattern;

import org.funtester.app.i18n.Messages;
import org.funtester.common.util.Validator;
import org.funtester.core.software.RegEx;
import org.funtester.core.software.Software;
import org.funtester.core.util.InvalidValueException;

/**
 * Validator for a {@link RegEx}.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class RegExValidator implements Validator< RegEx > {
	
	private final Software software;
	
	public RegExValidator(Software software) {
		this.software = software;
	}

	@Override
	public void validate(RegEx obj) throws Exception {
		
		final String nameRegEx = "[\\p{L}0-9 \\'.\\-_]{2,50}";
		
		if ( ! obj.getName().matches( nameRegEx ) ) {
			String msg = String.format(
					Messages.alt( "_REGEX_NAME_INVALID", "Name does not match the regex \"%s\"." ),
					nameRegEx );
			throw new InvalidValueException( msg, "name" );
		}
		
		final int expressionMinLength = 1;
		final int expressionMaxLength = 1000;
		
		if ( null == obj.getExpression()
				|| obj.getExpression().length() < expressionMinLength
				|| obj.getExpression().length() > expressionMaxLength
				) {
			String msg = String.format(
					Messages.getString( "_REGEX_EXPRESSION_INCORRECT_SIZE" ),
					expressionMinLength, expressionMaxLength );
			throw new InvalidValueException( msg, "expression" );
		}
			
		try {
			Pattern.compile( obj.getExpression() );
		} catch (Exception e) {
			String msg = String.format(
					Messages.getString( "_REGEX_EXPRESSION_INVALID" ),
					e.getLocalizedMessage() );
			throw new InvalidValueException( msg, "expression" );
		}
		
		boolean nameAlreadyExists = false;
		long otherId = obj.getId(); // Same id
		
		for ( RegEx re : software.getRegularExpressions() ) {
			if ( re.getName().equalsIgnoreCase( obj.getName() ) ) {
				nameAlreadyExists = true;
				otherId = re.getId();
				break;
			}
		}
		
		//System.out.println( "otherId = " + otherId + " obj.id = " + obj.getId() );
		
		final boolean isNew = ( 0 == obj.getId() );
		final boolean isOldButDifferent = ( ! isNew && otherId != obj.getId() );
		
		if ( nameAlreadyExists && ( isNew || isOldButDifferent ) ) {
			String msg = Messages.getString( "_REGEX_ALREADY_EXISTS" );
			throw new InvalidValueException( msg, "name" );
		}
	}

}
