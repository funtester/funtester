package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.ElementCodeGenerator;

/**
 * Button
 *
 * @author Thiago Delgado Pinto
 */
public abstract class Button implements ElementCodeGenerator {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "button" );
	}

}
