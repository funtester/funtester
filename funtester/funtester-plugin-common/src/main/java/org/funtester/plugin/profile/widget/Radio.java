package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.ElementCodeGenerator;

/**
 * Radio
 *
 * @author Thiago Delgado Pinto
 */
public abstract class Radio implements ElementCodeGenerator {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "radio" )
			|| value.equalsIgnoreCase( "button.radio" )
			|| value.equalsIgnoreCase( "radiobutton" )
			;
	}

}
