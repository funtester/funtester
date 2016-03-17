package org.funtester.core.process.rule;

import static org.funtester.core.software.BusinessRuleType.EQUAL_TO;
import static org.funtester.core.software.BusinessRuleType.MAX_LENGTH;
import static org.funtester.core.software.BusinessRuleType.MAX_VALUE;
import static org.funtester.core.software.BusinessRuleType.MIN_LENGTH;
import static org.funtester.core.software.BusinessRuleType.MIN_VALUE;
import static org.funtester.core.software.BusinessRuleType.NOT_ONE_OF;
import static org.funtester.core.software.BusinessRuleType.ONE_OF;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.funtester.core.process.value.ValidValueOption;
import org.funtester.core.software.BusinessRuleType;

/**
 * Valid value option influence.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class ValidValueOptionInfluence {

	private Map< ValidValueOption, Set< BusinessRuleType > > influenceMap;
	
	public ValidValueOptionInfluence() {
		influenceMap = new HashMap< ValidValueOption, Set< BusinessRuleType > >();
		
		influenceMap.put( ValidValueOption.MIN,
				with( MIN_VALUE, MIN_LENGTH, EQUAL_TO, ONE_OF, NOT_ONE_OF ) );
		
		influenceMap.put( ValidValueOption.MAX,
				with( MAX_VALUE, MAX_LENGTH, EQUAL_TO, ONE_OF, NOT_ONE_OF ) );
		
		influenceMap.put( ValidValueOption.ZERO,
				with( MIN_VALUE, MAX_VALUE, MIN_LENGTH, MAX_LENGTH, EQUAL_TO, ONE_OF, NOT_ONE_OF ) );
		
		influenceMap.put( ValidValueOption.MEDIAN,
				with( MIN_VALUE, MAX_VALUE, MIN_LENGTH, MAX_LENGTH, EQUAL_TO, ONE_OF, NOT_ONE_OF ) );
		
		influenceMap.put( ValidValueOption.RIGHT_AFTER_MIN,
				with( MIN_VALUE, MIN_LENGTH, EQUAL_TO, ONE_OF, NOT_ONE_OF ) );

		influenceMap.put( ValidValueOption.RIGHT_BEFORE_MAX,
				with( MAX_VALUE, MAX_LENGTH, EQUAL_TO, ONE_OF, NOT_ONE_OF ) );
		
		influenceMap.put( ValidValueOption.RANDOM_INSIDE_RANGE,
				with( MIN_VALUE, MAX_VALUE, MIN_LENGTH, MAX_LENGTH, EQUAL_TO, ONE_OF, NOT_ONE_OF ) );
	}

	private Set< BusinessRuleType > with(BusinessRuleType ... influences) {
		Set< BusinessRuleType > rules = new HashSet< BusinessRuleType >();
		for ( BusinessRuleType businessRuleType : influences ) {
			rules.add( businessRuleType );
		} 
		return rules;
	}
	
	public Set< BusinessRuleType > of(final ValidValueOption option) {
		return influenceMap.get( option );
	}
}
