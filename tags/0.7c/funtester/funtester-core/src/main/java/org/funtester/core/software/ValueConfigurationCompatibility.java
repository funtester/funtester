package org.funtester.core.software;

import java.util.ArrayList;
import java.util.List;

/**
 * Useful information about the compatibility between a {@code BusinessRuleType}
 * and a {@code ValueConfigurationKind}.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class ValueConfigurationCompatibility {
	
	/**
	 * Return {@code true} whether a certain {@code BusinessRuleType} is
	 * compatible with a certain {@code ValueConfigurationKind}.
	 * 
	 * @param brType	the {@code BusinessRuleType}
	 * @param vcKind	the {@code ValueConfigurationKind}
	 * @return			{@code true} if compatible, {@code false} otherwise.
	 */
	public static boolean areCompatible(
			final BusinessRuleType brType,
			final ValueConfigurationKind vcKind
			) {
		switch ( vcKind ) {
			case SINGLE:
				return BusinessRuleType.MIN_VALUE.equals( brType ) 
						|| BusinessRuleType.MAX_VALUE.equals( brType )
						|| BusinessRuleType.MIN_LENGTH.equals( brType )
						|| BusinessRuleType.MAX_LENGTH.equals( brType )
						;
			case MULTI:
				return BusinessRuleType.ONE_OF.equals( brType )
						|| BusinessRuleType.NOT_ONE_OF.equals( brType )
						;
			case ELEMENT_BASED:
				return BusinessRuleType.MIN_VALUE.equals( brType )
						|| BusinessRuleType.MAX_VALUE.equals( brType )
						|| BusinessRuleType.MIN_LENGTH.equals( brType )
						|| BusinessRuleType.MAX_LENGTH.equals( brType )
						|| BusinessRuleType.EQUAL_TO.equals( brType )
						;
			case REGEX_BASED:
				return BusinessRuleType.REG_EX.equals( brType );
			case QUERY_BASED:
				return BusinessRuleType.MIN_VALUE.equals( brType )
						|| BusinessRuleType.MAX_VALUE.equals( brType )
						|| BusinessRuleType.MIN_LENGTH.equals( brType )
						|| BusinessRuleType.MAX_LENGTH.equals( brType )
						|| BusinessRuleType.ONE_OF.equals( brType )
						|| BusinessRuleType.NOT_ONE_OF.equals( brType )
						;
			default: return false;
		}
	}
	
	/**
	 * Return a list with only the compatible kinds
	 * ({@code ValueConfigurationKind}) for a certain business rule type
	 * ({@code BusinessRuleType}).
	 * 
	 * @param brType	the {@code BusinessRuleType}
	 * @return			a list of compatible kinds.
	 */
	public static List< ValueConfigurationKind > compatibleKinds(final BusinessRuleType brType) {
		List< ValueConfigurationKind > list = new ArrayList< ValueConfigurationKind >();
		for ( ValueConfigurationKind vcKind : ValueConfigurationKind.values() ) {
			if ( ! ValueConfigurationCompatibility.areCompatible( brType, vcKind ) ) {
				continue;
			}
			list.add( vcKind );
		}
		return list;
	}	

}
