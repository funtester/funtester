package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.ElementCodeGenerator;

/**
 * Frame
 *
 * @author Thiago Delgado Pinto
 */
public abstract class Frame implements ElementCodeGenerator {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "frame" )
			|| value.equalsIgnoreCase( "window.frame" );
	}

}
