package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.Element;

/**
 * Accordion
 *
 * @author Thiago Delgado Pinto
 */
public abstract class Accordion implements Element {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "accordion" );
	}

}
