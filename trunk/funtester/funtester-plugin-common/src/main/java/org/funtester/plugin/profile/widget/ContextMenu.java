package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.Element;

/**
 * ContextMenu
 *
 * @author Thiago Delgado Pinto
 */
public abstract class ContextMenu implements Element {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "menu.context" )
			|| value.equalsIgnoreCase( "contextmenu" )
			|| value.equalsIgnoreCase( "popupmenu" )
			;
	}

}
