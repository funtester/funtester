package org.funtester.app.validation;

import org.funtester.app.i18n.Messages;
import org.funtester.common.util.Validator;
import org.funtester.core.software.Element;
import org.funtester.core.util.InvalidValueException;

/**
 * Validator for an {@link Element}.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class ElementValidator implements Validator< Element > {

	@Override
	public void validate(final Element obj) throws Exception {
		
		if ( null == obj.getUseCase() ) { // Should never happen
			String msg = Messages.alt( "_ELEMENT_UNDEFINED_USE_CASE", "The use case in undefined." );
			throw new InvalidValueException( msg, "useCase" );
		}
		
		final int nameMinLength = 2;
		final int nameMaxLength = 300;
		final String nameRegEx = "[\\p{L}0-9 \\'.\\-_]{" + nameMinLength + "," + nameMaxLength + "}";
		
		if ( null == obj.getName() || ! obj.getName().matches( nameRegEx ) ) {
			String msg = String.format(
					Messages.getString( "_ELEMENT_NAME_INVALID" ),
					nameMinLength, nameMaxLength, nameRegEx );
			throw new InvalidValueException( msg, "name" );
		}
		
		final int internalNameMinLength = 1;
		final int internalNameMaxLength = 300;
		final String internalNameRegEx = "[\\p{L}_][\\p{L}0-9_]{" + ( internalNameMinLength - 1 ) + "," + internalNameMaxLength + "}";
		
		if ( null == obj.getInternalName() || ! obj.getInternalName().matches( internalNameRegEx ) ) {
			String msg = String.format(
					Messages.getString( "_ELEMENT_INFORM_INTERNAL_NAME" ),
					internalNameMinLength, internalNameMaxLength, internalNameRegEx );
			throw new InvalidValueException( msg, "internalName" );
		}
		
		if ( null == obj.getType() ) {
			String msg = Messages.alt( "_ELEMENT_INFORM_TYPE", "Please inform the type." );
			throw new InvalidValueException( msg, "type" );
		}
		
		if ( ! obj.isEditable() ) { return; }
		// From now one, only the editable fields
		
		if ( null == obj.getValueType() ) {
			String msg = Messages.alt( "_ELEMENT_INFORM_VALUE_TYPE", "Please inform the value type." );
			throw new InvalidValueException( msg, "valueType" );
		}
		
		// TODO validate the business rules (?)
	}

}
