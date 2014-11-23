package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.Element;

/**
 * MenuBar
 *
 * @author Thiago Delgado Pinto
 */
public abstract class MenuBar implements Element {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "bar.menu" )
			|| value.equalsIgnoreCase( "menubar" );
	}

}
