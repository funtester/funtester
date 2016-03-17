package org.funtester.core.profile;

/**
 * Action type
 *
 * @author Thiago Delgado Pinto
 *
 */
public enum ActionType {

	PURE					// Just the Action
	, USE_CASE_CALL			// Action + UseCase
	, WIDGET				// Action + Widget
	, VALUE					// Action + Value
	, PROPERTY				// Action + Widget + Value
	, BUSINESS_RULE_CHECK	// Action + Widget -> Special action type

}
