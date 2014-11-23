package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.Element;

/**
 * Button
 *
 * @author Thiago Delgado Pinto
 */
public abstract class Button implements Element {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "button" );
	}

}
