package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.Element;

/**
 * Spinner
 *
 * @author Thiago Delgado Pinto
 */
public abstract class Spinner implements Element {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "spinner" );
	}

}
