package org.funtester.app.validation;

import org.funtester.app.i18n.Messages;
import org.funtester.common.util.Validator;
import org.funtester.core.software.OracleStep;
import org.funtester.core.util.InvalidValueException;

/**
 * Validator for an {@link OracleStep}.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class OracleStepValidator implements Validator< OracleStep > {

	@Override
	public void validate(OracleStep obj) throws Exception {

		if ( null == obj.getActionNickname() ) {
			final String msg = Messages.alt( "_ORACLE_STEP_INFORM_ACTION", "Please inform an action." );
			throw new InvalidValueException( msg, "actionNickname" );
		}

		final int elementCount = obj.getElements().size();
		if ( elementCount < 1 ) {
			final String msg = Messages.alt( "_ORACLE_STEP_INFORM_ELEMENT", "Please inform at least one element." );
			throw new InvalidValueException( msg, "elements" );
		}
		
		final int maxElements = obj.getActionNickname().maxElements();
		if ( elementCount > maxElements ) {
			final int difference = elementCount - maxElements;
			final String msg = Messages.alt( "_ORACLE_STEP_MAX_ELEMENTS",
					"This action allows up to %d element(s). Please remove %d element(s)." );
			final String formattedMsg = String.format( msg, maxElements, difference );
			throw new InvalidValueException( formattedMsg, "elements" );
		}
	}

}
