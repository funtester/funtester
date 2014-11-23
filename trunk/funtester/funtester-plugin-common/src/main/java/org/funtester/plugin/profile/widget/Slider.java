package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.Element;

/**
 * Slider
 *
 * @author Thiago Delgado Pinto
 */
public abstract class Slider implements Element {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "slider" );
	}

}
