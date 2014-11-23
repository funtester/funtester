package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.ElementCodeGenerator;

/**
 * Spinner
 *
 * @author Thiago Delgado Pinto
 */
public abstract class Spinner implements ElementCodeGenerator {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "spinner" );
	}

}
