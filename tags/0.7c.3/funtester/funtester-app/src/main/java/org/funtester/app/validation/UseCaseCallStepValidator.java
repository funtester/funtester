package org.funtester.app.validation;

import org.funtester.app.i18n.Messages;
import org.funtester.common.util.Validator;
import org.funtester.core.software.UseCaseCallStep;
import org.funtester.core.util.InvalidValueException;

/**
 * Validator for an {@link UseCaseCallStep}.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class UseCaseCallStepValidator implements Validator< UseCaseCallStep > {

	@Override
	public void validate(UseCaseCallStep obj) throws Exception {
		
		if ( null == obj.getFlow() ) {
			final String msg = Messages.getString( "_USE_CASE_CALL_STEP_NO_FLOW" );
			throw new InvalidValueException( msg, "flow" );
		}
		
		if ( null == obj.getActionNickname() ) {
			final String msg = Messages.getString( "_USE_CASE_CALL_STEP_NO_ACTION_NICKNAME" );
			throw new InvalidValueException( msg, "actionNickname" );
		}
		
		if ( null == obj.getUseCase() ) {
			final String msg = Messages.getString( "_USE_CASE_CALL_STEP_NO_USE_CASE" );
			throw new InvalidValueException( msg, "useCase" );
		}
		
		// obj.getPostcondition() not validated in this version
	}

}
