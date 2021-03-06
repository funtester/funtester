package org.funtester.core.profile;

/**
 * The possible kinds of a step. This is useful to categorize actions and
 * define the kind of steps, so that the actions can be filtered accordingly.
 *
 * @author Thiago Delgado Pinto
 *
 */
public enum StepKind {

	DOC
	, ACTION

	/** legacy ones **/

	, USE_CASE_CALL
	, ORACLE
}
