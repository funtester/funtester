package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.Element;

/**
 * Radio
 *
 * @author Thiago Delgado Pinto
 */
public abstract class Radio implements Element {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "radio" )
			|| value.equalsIgnoreCase( "button.radio" )
			|| value.equalsIgnoreCase( "radiobutton" )
			;
	}

}
