package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.ElementCodeGenerator;

/**
 * Calendar
 *
 * @author Thiago Delgado Pinto
 */
public abstract class Calendar implements ElementCodeGenerator {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "calendar" );
	}

}
