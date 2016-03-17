package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.ElementCodeGenerator;

/**
 * NavigatorBackButton
 *
 * @author Thiago Delgado Pinto
 */
public abstract class NavigatorBackButton implements ElementCodeGenerator {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "navigator.back" )
			;
	}

}
