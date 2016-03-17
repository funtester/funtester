package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.ElementCodeGenerator;

/**
 * NavigatorForwardButton
 *
 * @author Thiago Delgado Pinto
 */
public abstract class NavigatorForwardButton implements ElementCodeGenerator {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "navigator.forward" )
			;
	}

}
