package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.ElementCodeGenerator;

/**
 * Label
 *
 * @author Thiago Delgado Pinto
 */
public abstract class Label implements ElementCodeGenerator {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "label" );
	}

}
