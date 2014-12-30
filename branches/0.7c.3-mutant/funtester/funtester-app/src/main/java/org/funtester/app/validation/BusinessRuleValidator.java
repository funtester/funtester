package org.funtester.app.validation;

import java.util.Collection;

import org.funtester.app.i18n.Messages;
import org.funtester.app.ui.common.EnumTranslation;
import org.funtester.common.util.Validator;
import org.funtester.core.software.BusinessRule;
import org.funtester.core.software.BusinessRuleType;
import org.funtester.core.util.InvalidValueException;

/**
 * Validator for a {@link BusinessRule}.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class BusinessRuleValidator implements Validator< BusinessRule > {
	
	private final ValueConfigurationValidator vcValidator;
	private final Collection< BusinessRule > otherBusinessRules;
	
	public BusinessRuleValidator(
			ValueConfigurationValidator validator,
			Collection< BusinessRule > otherBusinessRules
			) {
		this.vcValidator = validator;
		this.otherBusinessRules = otherBusinessRules;
	}

	@Override
	public void validate(final BusinessRule obj) throws Exception {
		
		final int messageMinLength = 1;
		final int messageMaxLength = 500;
		// No regex needed for the message
		
		if ( null == obj.getMessage()
				|| obj.getMessage().length() < messageMinLength
				|| obj.getMessage().length() > messageMaxLength ) {
			
			String msg = String.format(
					Messages.getString( "_BUSINESS_RULE_MESSAGE_INVALID" ),
					messageMinLength, messageMaxLength
					);
			throw new InvalidValueException( msg, "message" );
		}
		
		if ( null == obj.getType() ) {
			String msg = Messages.alt( "_BUSINESS_RULE_INFORM_TYPE",
					"Please inform the type." );
			throw new InvalidValueException( msg, "type" );
		}
		
		if ( obj.getType() != BusinessRuleType.REQUIRED ) {
			
			if ( null == obj.getValueConfiguration() ) {
				String msg = Messages.alt( "_BUSINESS_RULE_INFORM_VALUE_CONFIGURATION",
						"Please choose a value configuration" );
				throw new InvalidValueException( msg, "businessRuleType" );
			}
			
			vcValidator.validate( obj.getValueConfiguration() );
		}
		
		// Verify for business rules with the same type
		
		boolean typeAlreadyExists = false;
		long otherId = obj.getId(); // Same id
		
		for ( BusinessRule br : otherBusinessRules ) {
			if ( br.getType().equals( obj.getType() ) ) {
				typeAlreadyExists = true;
				otherId = br.getId();
				break;
			}
		}		
		//System.out.println( "otherId = " + otherId + " obj.id = " + obj.getId() );
		final boolean isNew = ( 0 == obj.getId() );
		final boolean isOldButDifferent = ( ! isNew && otherId != obj.getId() );
		
		if ( typeAlreadyExists && ( isNew || isOldButDifferent ) ) {
			String msg = String.format( Messages.alt(
					"_BUSINESS_RULE_VALUE_CONFIGURATION_ALREADY_EXISTS",
					"A value configuration with the business rule type \"%s\" already exists. Please choose another one." ),
				EnumTranslation.translationForItem( BusinessRuleType.class, obj.getType() ) );
			throw new InvalidValueException( msg, "businessRuleType" );
		}
		
	}

}
