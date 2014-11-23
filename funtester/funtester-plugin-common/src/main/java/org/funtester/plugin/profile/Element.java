package org.funtester.plugin.profile;

/**
 * Element
 *
 * @author Thiago Delgado Pinto
 */
public interface Element {

	/**
	 * Return {@code true} weather the element has the given name.
	 *
	 * @see		file unified.fp
	 * @param	value
	 * @return
	 */
	boolean is(final String value);

	String code();

}
