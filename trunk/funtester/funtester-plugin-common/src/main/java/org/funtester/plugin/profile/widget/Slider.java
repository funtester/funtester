package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.ElementCodeGenerator;

/**
 * Slider
 *
 * @author Thiago Delgado Pinto
 */
public abstract class Slider implements ElementCodeGenerator {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "slider" );
	}

}
