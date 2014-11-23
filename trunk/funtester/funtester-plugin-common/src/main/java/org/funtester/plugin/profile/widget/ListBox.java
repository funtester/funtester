package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.Element;

/**
 * ListBox
 *
 * @author Thiago Delgado Pinto
 */
public abstract class ListBox implements Element {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "listbox" )
			|| value.equalsIgnoreCase( "box.list" )
			|| value.equalsIgnoreCase( "list" )
			;
	}

}
