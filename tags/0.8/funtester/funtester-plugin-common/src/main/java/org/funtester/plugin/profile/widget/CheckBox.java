package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.ElementCodeGenerator;

/**
 * CheckBox
 *
 * @author Thiago Delgado Pinto
 */
public abstract class CheckBox implements ElementCodeGenerator {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "checkbox" )
			|| value.equalsIgnoreCase( "box.check" )
			|| value.equalsIgnoreCase( "check" )
			;
	}

}
