package org.funtester.app.validation;

import org.funtester.app.i18n.Messages;
import org.funtester.common.util.Validator;
import org.funtester.core.software.DocStep;
import org.funtester.core.util.InvalidValueException;

/**
 * Validator for a {@link DocStep}.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class DocStepValidator implements Validator< DocStep > {

	@Override
	public void validate(DocStep obj) throws Exception {
		
		final int minLength = 2;
		final int maxLength = 500;
		
		if ( null == obj.getSentence()
				|| obj.getSentence().length() < minLength
				|| obj.getSentence().length() > maxLength ) {
			
			String msg = String.format(
					Messages.getString( "_DOC_STEP_INFORM_SENTENCE" ),
					minLength, maxLength
					);
			throw new InvalidValueException( msg, "sentence" );
		}
		
		// Do NOT check for repeated steps (a step can repeat).
	}

}
