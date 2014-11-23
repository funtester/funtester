package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.Element;

/**
 * Label
 *
 * @author Thiago Delgado Pinto
 */
public abstract class Label implements Element {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "label" );
	}

}
