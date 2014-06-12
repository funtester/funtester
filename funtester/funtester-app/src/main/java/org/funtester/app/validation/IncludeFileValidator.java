package org.funtester.app.validation;

import org.funtester.app.i18n.Messages;
import org.funtester.common.util.Validator;
import org.funtester.core.software.IncludeFile;
import org.funtester.core.util.InvalidValueException;

/**
 * Validator for an {@link IncludeFile}.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class IncludeFileValidator implements Validator< IncludeFile > {

	@Override
	public void validate(IncludeFile obj) throws Exception {
		if ( null == obj ) throw new NullPointerException( "includefile cannot be null." );
		
		final int minLength = 1;
		final int maxLength = 500;
		
		final String nameRegEx = "[\\p{L}0-9 _.\\-/]{" + minLength + "," + maxLength + "}";
		
		if ( ! obj.getName().matches( nameRegEx ) ) {
			 String msg = String.format(
					Messages.getString( "_INCLUDE_FILE_NAME_INVALID" ),
					minLength, maxLength, nameRegEx
					);
			throw new InvalidValueException( msg, "name" );
		}
	}

}
