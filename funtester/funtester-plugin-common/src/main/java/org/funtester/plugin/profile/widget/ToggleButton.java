package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.Element;

/**
 * ToggleButton
 *
 * @author Thiago Delgado Pinto
 */
public abstract class ToggleButton implements Element {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "button.toggle" )
			|| value.equalsIgnoreCase( "toggle" );
	}

}
