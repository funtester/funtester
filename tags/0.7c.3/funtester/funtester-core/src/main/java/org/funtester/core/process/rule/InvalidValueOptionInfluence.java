package org.funtester.core.process.rule;

import static org.funtester.core.software.BusinessRuleType.EQUAL_TO;
import static org.funtester.core.software.BusinessRuleType.MAX_LENGTH;
import static org.funtester.core.software.BusinessRuleType.MAX_VALUE;
import static org.funtester.core.software.BusinessRuleType.MIN_LENGTH;
import static org.funtester.core.software.BusinessRuleType.MIN_VALUE;
import static org.funtester.core.software.BusinessRuleType.NOT_ONE_OF;
import static org.funtester.core.software.BusinessRuleType.ONE_OF;
import static org.funtester.core.software.BusinessRuleType.REG_EX;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.funtester.core.process.value.InvalidValueOption;
import org.funtester.core.software.BusinessRuleType;

/**
 * Invalid value option influence.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class InvalidValueOptionInfluence {

	private Map< InvalidValueOption, Set< BusinessRuleType > > influenceMap;
	
	public InvalidValueOptionInfluence() {
		influenceMap = new HashMap< InvalidValueOption, Set< BusinessRuleType > >();
		
		influenceMap.put( InvalidValueOption.RIGHT_BEFORE_MIN,
				with( MIN_VALUE, MIN_LENGTH, EQUAL_TO, ONE_OF, NOT_ONE_OF ) );
		
		influenceMap.put( InvalidValueOption.RANDOM_BEFORE_MIN,
				with( MIN_VALUE, MIN_LENGTH, EQUAL_TO, ONE_OF, NOT_ONE_OF ) );
		
		influenceMap.put( InvalidValueOption.RIGHT_AFTER_MAX,
				with( MAX_VALUE, MAX_LENGTH, EQUAL_TO, ONE_OF, NOT_ONE_OF ) );
		
		influenceMap.put( InvalidValueOption.RANDOM_AFTER_MAX,
				with( MAX_VALUE, MAX_LENGTH, EQUAL_TO, ONE_OF, NOT_ONE_OF ) );
		
		influenceMap.put( InvalidValueOption.INVALID_FORMAT,
				with( REG_EX ) );
	}

	private Set< BusinessRuleType > with(BusinessRuleType ... influences) {
		Set< BusinessRuleType > rules = new HashSet< BusinessRuleType >();
		for ( BusinessRuleType businessRuleType : influences ) {
			rules.add( businessRuleType );
		} 
		return rules;
	}
	
	public Set< BusinessRuleType > of(final InvalidValueOption option) {
		return influenceMap.get( option );
	}
}
