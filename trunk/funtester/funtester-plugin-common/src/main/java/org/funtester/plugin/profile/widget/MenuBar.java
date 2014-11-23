package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.ElementCodeGenerator;

/**
 * MenuBar
 *
 * @author Thiago Delgado Pinto
 */
public abstract class MenuBar implements ElementCodeGenerator {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "bar.menu" )
			|| value.equalsIgnoreCase( "menubar" );
	}

}
