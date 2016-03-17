package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.ElementCodeGenerator;

/**
 * Window
 *
 * @author Thiago Delgado Pinto
 */
public abstract class Window implements ElementCodeGenerator {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "window" );
	}

}
