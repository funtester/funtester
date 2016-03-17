package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.ElementCodeGenerator;

/**
 * ListBox
 *
 * @author Thiago Delgado Pinto
 */
public abstract class ListBox implements ElementCodeGenerator {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "listbox" )
			|| value.equalsIgnoreCase( "box.list" )
			|| value.equalsIgnoreCase( "list" )
			;
	}

}
