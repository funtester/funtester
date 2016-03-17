package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.ElementCodeGenerator;

/**
 * ContextMenu
 *
 * @author Thiago Delgado Pinto
 */
public abstract class ContextMenu implements ElementCodeGenerator {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "menu.context" )
			|| value.equalsIgnoreCase( "contextmenu" )
			|| value.equalsIgnoreCase( "popupmenu" )
			;
	}

}
