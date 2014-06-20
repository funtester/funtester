package org.funtester.app.validation;

import org.funtester.app.i18n.Messages;
import org.funtester.common.util.Validator;
import org.funtester.core.software.Precondition;
import org.funtester.core.util.InvalidValueException;

/**
 * A validator for a {@code Precondition}.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class PreconditionValidator implements Validator< Precondition > {

	public void validate(final Precondition p) throws Exception {
		
		final int minLength = 2;
		final int maxLength = 50;
		
		final String descriptionRegEx = "[\\p{L}0-9 \\'.\\-_]{" + minLength + "," + maxLength + "}";
		
		if ( null == p.getDescription() || p.getDescription().matches( descriptionRegEx ) ) {
			
			String msg = String.format(
					Messages.getString( "_PRECONDITION_DESCRIPTION_INVALID" ),
					minLength, maxLength, descriptionRegEx );
			
			throw new InvalidValueException( msg, "description" );
		}
	}
}
