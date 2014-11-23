package org.funtester.plugin.profile;

import java.util.Collection;
import java.util.HashSet;

/**
 * Element finder
 *
 * @author Thiago Delgado Pinto
 *
 */
public class ElementFinder {

	private final Collection< Element > elements;

	/**
	 * Creates the finder with a collection of elements.
	 *
	 * @param elements
	 */
	public ElementFinder(final Collection< Element > elements) {
		this.elements = elements;
	}

	/**
	 * Creates the finder with one or more elements.
	 *
	 * @param elements
	 */
	public ElementFinder(final Element ... elements) {
		this.elements = new HashSet< Element >();
		for ( Element e : elements ) {
			this.elements.add( e );
		}
	}

	/**
	 * Return the elements with the given name or null if not found.
	 * @param value
	 * @return
	 */
	public Element find( final String value ) {
		for ( Element e : elements ) {
			if ( e.is( value ) ) {
				return e;
			}
		}
		return null;
	}

}
