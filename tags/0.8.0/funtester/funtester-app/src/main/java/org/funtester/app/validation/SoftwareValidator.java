package org.funtester.app.validation;

import org.funtester.app.i18n.Messages;
import org.funtester.common.util.Validator;
import org.funtester.core.software.Software;
import org.funtester.core.util.InvalidValueException;

/**
 * Validator for a {@link Software}.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class SoftwareValidator implements Validator< Software > {

	@Override
	public void validate(final Software obj) throws Exception {
		if ( null == obj ) throw new IllegalArgumentException( "software cannot be null" );
		
		final int nameMinLength = 1;
		final int nameMaxLength = 50;
		final String nameRegEx = "[\\p{L}0-9 \\'.\\-_]{" + nameMinLength + "," + nameMaxLength + "}";
		
		if ( ! obj.getName().matches( nameRegEx ) ) {
			String msg = String.format(
					Messages.getString( "_SOFTWARE_NAME_INVALID" ),
					nameMinLength, nameMaxLength, nameRegEx );
			
			throw new InvalidValueException( msg, "name" );
		}
		
		if ( null == obj.getVocabulary() ) {
			String msg = Messages.getString( "_SOFTWARE_VOCABULARY_NULL" );
			throw new InvalidValueException( msg, "vocabulary" );
		}
	}

}
