package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.Element;

/**
 * NavigatorForwardButton
 *
 * @author Thiago Delgado Pinto
 */
public abstract class NavigatorForwardButton implements Element {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "navigator.forward" )
			;
	}

}
