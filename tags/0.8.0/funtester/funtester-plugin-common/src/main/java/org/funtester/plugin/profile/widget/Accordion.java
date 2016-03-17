package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.ElementCodeGenerator;

/**
 * Accordion
 *
 * @author Thiago Delgado Pinto
 */
public abstract class Accordion implements ElementCodeGenerator {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "accordion" );
	}

}
