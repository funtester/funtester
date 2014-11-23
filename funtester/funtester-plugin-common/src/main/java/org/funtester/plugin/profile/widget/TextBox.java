package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.Element;

/**
 * TextBox
 *
 * @author Thiago Delgado Pinto
 */
public abstract class TextBox implements Element {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "textbox" )
			|| value.equalsIgnoreCase( "box.text" )
			|| value.equalsIgnoreCase( "text" )
			|| value.equalsIgnoreCase( "edit" )
			|| value.equalsIgnoreCase( "input" )
			;
	}

}
