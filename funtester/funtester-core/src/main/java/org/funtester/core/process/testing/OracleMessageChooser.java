package org.funtester.core.process.testing;

import org.funtester.core.process.value.InvalidValueOption;
import org.funtester.core.software.BusinessRule;
import org.funtester.core.software.BusinessRuleType;
import org.funtester.core.software.Element;

/**
 * Chooses the right message for the oracle according to a invalid option and
 * the defined business rules.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class OracleMessageChooser {
	
	/**
	 * Analyzes the defined business rules and the desired invalid value option
	 * to choose the message for the oracle. 
	 * 
	 * @param ee		the editable element with business rules.
	 * @param option	the target invalid value option.
	 * @return			a message or a empty string there is no business rule
	 * 					defined according to the invalid option.
	 */
	public String choose(final Element ee, final InvalidValueOption option) {
		
		final String EMPTY_STRING = "";
		
		if ( ! ee.isEditable() ) return EMPTY_STRING;
		
		
		BusinessRule equalToBR = ee.businessRuleWithType( BusinessRuleType.EQUAL_TO );
		BusinessRule notOneOfBR = ee.businessRuleWithType( BusinessRuleType.NOT_ONE_OF );
		BusinessRule oneOfBR = ee.businessRuleWithType( BusinessRuleType.ONE_OF );
		BusinessRule regExBR = ee.businessRuleWithType( BusinessRuleType.REG_EX );
		BusinessRule minValueBR = ee.businessRuleWithType( BusinessRuleType.MIN_VALUE );
		BusinessRule maxValueBR = ee.businessRuleWithType( BusinessRuleType.MAX_VALUE );
		BusinessRule minLengthBR = ee.businessRuleWithType( BusinessRuleType.MIN_LENGTH );
		BusinessRule maxLengthBR = ee.businessRuleWithType( BusinessRuleType.MAX_LENGTH );
		
		// INVALID_FORMAT is compatible with REG_EX
		if ( InvalidValueOption.INVALID_FORMAT.equals( option ) ) {			
			return ( regExBR != null ) ? regExBR.getMessage() : EMPTY_STRING;
		}
				
		// Any other (RIGHT_BEFORE_MIN, RANDOM_BEFORE_MIN, RIGHT_AFTER_MAX,
		// or RANDOM_AFTER_MAX) is compatible with EQUAL_TO, ONE_OF, NOT_ONE_OF.
		// So if the rule is defined, use it.
		
		if ( equalToBR != null ) {
			return equalToBR.getMessage();
		}				
		if ( oneOfBR != null ) {
			return oneOfBR.getMessage();
		}
		if ( notOneOfBR != null ) {
			return notOneOfBR.getMessage();
		}	
		
		// Then verify the specific ones:
		
		// RIGHT_BEFORE_MIN or RANDOM_BEFORE_MIN with MIN_VALUE or MIN_LENGTH 
		if ( InvalidValueOption.RIGHT_BEFORE_MIN.equals( option )
				|| InvalidValueOption.RANDOM_BEFORE_MIN.equals( option ) ) {
			if ( minValueBR != null ) {
				return minValueBR.getMessage();
			}
			if ( minLengthBR != null ) {
				return minLengthBR.getMessage();
			}
		}
		
		// RIGHT_AFTER_MAX or RANDOM_AFTER_MAX with MAX_VALUE or MAX_LENGTH
		if ( InvalidValueOption.RIGHT_AFTER_MAX.equals( option )
				|| InvalidValueOption.RANDOM_AFTER_MAX.equals( option ) ) {			
			if ( maxValueBR != null ) {
				return maxValueBR.getMessage();
			}
			if ( maxLengthBR != null ) {
				return maxLengthBR.getMessage();
			}	
		}

		// None rule defined or none compatible with the invalid option, then
		// return a empty string
		return EMPTY_STRING;
	}

}