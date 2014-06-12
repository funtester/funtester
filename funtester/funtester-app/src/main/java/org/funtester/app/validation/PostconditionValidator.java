package org.funtester.app.validation;

import org.funtester.app.i18n.Messages;
import org.funtester.common.util.Validator;
import org.funtester.core.software.Postcondition;
import org.funtester.core.util.InvalidValueException;

/**
 * Validator for a {@link Postcondition}.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class PostconditionValidator implements Validator< Postcondition > {

	@Override
	public void validate(Postcondition obj) throws Exception {
		
		final int minLength = 2;
		final int maxLength = 50;
		
		final String descriptionRegEx = "[\\p{L}0-9 \\'.\\-_]{" + minLength + "," + maxLength + "}";
		
		if ( ! obj.getDescription().matches( descriptionRegEx ) ) {
			
			String msg = String.format(
					Messages.getString( "_POSTCONDITION_DESCRIPTION_INVALID" ),
					minLength, maxLength, descriptionRegEx );
					
			throw new InvalidValueException( msg, "description" );
		}
		
		if ( null == obj.getOwnerFlow() ) {
			final String msg = Messages.getString( "_POSTCONDITION_OWNER_FLOW_INVALID" );
			throw new InvalidValueException( msg, "owner" );
		}
	}

}
