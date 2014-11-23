package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.Element;

/**
 * Dialog
 *
 * @author Thiago Delgado Pinto
 */
public abstract class Dialog implements Element {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "dialog" )
			|| value.equalsIgnoreCase( "window.dialog" );
	}

}
