package org.funtester.core.profile;

/**
 * Who triggers an action.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public enum Trigger {
	SYSTEM,
	ACTOR;
		
	/**
	 * Returns a Trigger from its orginal value.
	 *  
	 * @param ordinalValue	Integer value.
	 * @return				Trigger.
	 */
	public static Trigger fromOrdinal(int ordinalValue) {
		return ( 0 == ordinalValue ) ? SYSTEM : ACTOR;
	}
}
