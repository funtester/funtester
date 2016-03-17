package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.ElementCodeGenerator;

/**
 * Dialog
 *
 * @author Thiago Delgado Pinto
 */
public abstract class Dialog implements ElementCodeGenerator {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "dialog" )
			|| value.equalsIgnoreCase( "window.dialog" );
	}

}
