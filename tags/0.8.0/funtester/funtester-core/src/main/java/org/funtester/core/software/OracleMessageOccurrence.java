package org.funtester.core.software;

/**
 * Oracle message occurrence
 * 
 * @author Thiago Delgado Pinto
 *
 */
public enum OracleMessageOccurrence {
	
	/** Just one message is displayed, for only the first element */
	ONCE_FOR_THE_FIRST_ELEMENT,
	
	/** Just one message is displayed, for many elements */
	ONCE_FOR_MANY_ELEMENTS,
	
	/** One message is displayed for each element (very unusual) */
	FOR_EACH_ELEMENT
	;

}
