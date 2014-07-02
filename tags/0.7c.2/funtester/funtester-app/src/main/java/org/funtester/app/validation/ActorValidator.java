package org.funtester.app.validation;

import org.funtester.app.i18n.Messages;
import org.funtester.common.util.Validator;
import org.funtester.core.software.Actor;
import org.funtester.core.util.InvalidValueException;

/**
 * Validator for an {@link Actor}.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class ActorValidator implements Validator< Actor >{

	@Override
	public void validate(Actor obj) throws Exception {
		
		if ( null == obj ) throw new NullPointerException( "Actor cannot be null" );
		
		final int nameMinLength = 2;
		final int nameMaxLength = 50;
		final String nameRegEx = "[\\p{L}0-9 \\'.\\-]{" + nameMinLength + "," + nameMaxLength + "}";
		
		if ( ! obj.getName().matches( nameRegEx ) ) {
			String msg = String.format(
					Messages.getString( "_ACTOR_NAME_INVALID" ),
					nameMinLength, nameMaxLength, nameRegEx );
			throw new InvalidValueException( msg, "name" );
		}
		
		final int descriptionMinLength = 0;
		final int descriptionMaxLength = 300;
		final String descriptionRegEx = "[\\p{L}0-9 \\',.\\-]{" + descriptionMinLength + "," + descriptionMaxLength + "}";
		
		if ( ! obj.getDescription().matches( descriptionRegEx ) ) {
			String msg = String.format(
					Messages.getString( "_ACTOR_DESCRIPTION_INVALID" ),
					descriptionMinLength, descriptionMaxLength, descriptionRegEx );
			throw new InvalidValueException( msg, "description" );
		}		
	}

}
