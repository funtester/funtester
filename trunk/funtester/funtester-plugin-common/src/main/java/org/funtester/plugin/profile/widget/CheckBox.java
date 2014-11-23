package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.Element;

/**
 * CheckBox
 *
 * @author Thiago Delgado Pinto
 */
public abstract class CheckBox implements Element {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "checkbox" )
			|| value.equalsIgnoreCase( "box.check" )
			|| value.equalsIgnoreCase( "check" )
			;
	}

}
