package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.ElementCodeGenerator;

/**
 * Video
 *
 * @author Thiago Delgado Pinto
 */
public abstract class Video implements ElementCodeGenerator {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "video" );
	}

}
