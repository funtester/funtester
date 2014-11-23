package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.Element;

/**
 * Window
 *
 * @author Thiago Delgado Pinto
 */
public abstract class Window implements Element {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "window" );
	}

}
