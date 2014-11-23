package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.Element;

/**
 * Menu
 *
 * @author Thiago Delgado Pinto
 */
public abstract class Menu implements Element {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "menu" )
			|| value.equalsIgnoreCase( "menu.item" )
			|| value.equalsIgnoreCase( "menuitem" )
			;
	}

}
