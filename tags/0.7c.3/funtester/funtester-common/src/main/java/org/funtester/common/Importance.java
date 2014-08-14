package org.funtester.common;

/**
 * Importance values
 * 
 * @author Thiago Delgado Pinto
 *
 */
public enum Importance {

	VERY_LOW,
	LOW,
	MEDIUM,
	HIGH,
	VERY_HIGH;
	
	/**
	 * Return {@code true} whether the importance value is higher than another
	 * value.
	 * @param other
	 * @return
	 */
	public boolean higherThan(Importance other) {
		return ordinal() > other.ordinal();
	}
}
