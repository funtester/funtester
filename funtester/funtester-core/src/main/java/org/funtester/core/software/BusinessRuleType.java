package org.funtester.core.software;

/**
 * Kinds of restrictions that can be defined in a business rule.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public enum BusinessRuleType {
	
	MIN_VALUE,	// Minimum value
	MAX_VALUE,	// Maximum value
	MIN_LENGTH,	// Minimum value length
	MAX_LENGTH,	// Maximum value length
	REQUIRED,	// Required value
	REG_EX,		// Regular expression used to validate the value or format
	EQUAL_TO,	// Equal to Element value
	ONE_OF,		// One of a list of manually defined or queried values
	NOT_ONE_OF	// Not one of a list of manually defined or queried values

	// NOTES:
	//
	// - Of course that a REGEX could express MIN_VALUE, MAX_VALUE, MIN_LENGTH
	//   and MAX_LENGTH but its easier for a user, in many cases, to define
	//   these values and their messages separately.
	//
	// - NOT_ONE_OF could be used to define a rule to detect a violation of a
	//   unique value constraint. For instance, it could be a query that returns
	//	 all the existent values of a certain unique column of a database. So
	//	 the value is correct if is not found in this list.	
}
