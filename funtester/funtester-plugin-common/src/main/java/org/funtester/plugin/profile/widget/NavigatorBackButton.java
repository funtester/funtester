package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.Element;

/**
 * NavigatorBackButton
 *
 * @author Thiago Delgado Pinto
 */
public abstract class NavigatorBackButton implements Element {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "navigator.back" )
			;
	}

}
