package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.ElementCodeGenerator;

/**
 * Menu
 *
 * @author Thiago Delgado Pinto
 */
public abstract class Menu implements ElementCodeGenerator {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "menu" )
			|| value.equalsIgnoreCase( "menu.item" )
			|| value.equalsIgnoreCase( "menuitem" )
			;
	}

}
