package org.funtester.app.validation;

import org.funtester.app.i18n.Messages;
import org.funtester.common.util.Validator;
import org.funtester.core.software.AlternateFlow;
import org.funtester.core.software.Flow;
import org.funtester.core.software.FlowType;
import org.funtester.core.software.UseCase;
import org.funtester.core.util.InvalidValueException;

/**
 * Validator for a {@link Flow}.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class FlowValidator implements Validator< Flow > {

	@Override
	public void validate(Flow obj) throws Exception {
		UseCase useCase = obj.getUseCase();
		
		if ( null == useCase ) {
			String msg = Messages.alt( "_USE_CASE_UNDEFINED",
					"Use case not defined." );
			throw new InvalidValueException( msg, "useCase" );
		}
		
		if ( ! obj.type().equals( FlowType.BASIC ) ) {
			AlternateFlow af = (AlternateFlow) obj;
			
			if ( af.getDescription().isEmpty() ) {
				String msg = Messages.alt( "_FLOW_INFORM_DESCRIPTION",
						"Please inform the description." );
				throw new InvalidValueException( msg, "description" );
			}
			
			if ( af.getStarterFlow() == obj ) { // Same object (address)
				String msg = Messages.alt( "_FLOW_INVALID_STARTER_FLOW",
						"The starter flow cannot be the current flow." );
				throw new InvalidValueException( msg, "starterFlow" );
			}
		}
		
		// Check for repeated flows
		if ( useCase.containsFlow( obj ) && 0 == obj.getId() ) {
			String msg = Messages.alt( "_FLOW_ALREADY_EXISTS",
					"This flow already exists. Please change the description or other information and try again." );
			throw new InvalidValueException( msg, "description" );
		}
	}

}
