package org.funtester.app.validation;

import org.funtester.app.i18n.Messages;
import org.funtester.common.util.Validator;
import org.funtester.core.software.Software;
import org.funtester.core.software.UseCase;
import org.funtester.core.util.InvalidValueException;

/**
 * Validator for a {@link UseCase}.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class UseCaseValidator implements Validator< UseCase > {
	
	private final Software software;
	
	public UseCaseValidator(final Software software) {
		this.software = software;
	}

	@Override
	public void validate(UseCase obj) throws Exception {
		if ( null == obj ) throw new IllegalArgumentException( "usecase is null" );
		
		final int nameMinLength = 2;
		final int nameMaxLength = 50;
		final String nameRegEx = "[\\p{L}0-9 \\'.\\-,_]{" + nameMinLength + "," + nameMaxLength + "}";
		
		if ( ! obj.getName().matches( nameRegEx ) ) {
			String msg = String.format(
					Messages.getString( "_USE_CASE_NAME_INVALID" ),
					nameMinLength, nameMaxLength, nameRegEx );
			throw new InvalidValueException( msg, "name" );
		}
		
		final boolean isNew = ( 0 == obj.getId() );
		if ( isNew && software.containsUseCase( obj ) ) {
			throw new InvalidValueException(
					Messages.getString( "_USE_CASE_ALREADY_EXISTS" ), "name" );
		}
	}

}
