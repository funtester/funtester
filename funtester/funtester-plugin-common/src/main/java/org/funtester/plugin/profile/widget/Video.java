package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.Element;

/**
 * Video
 *
 * @author Thiago Delgado Pinto
 */
public abstract class Video implements Element {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "video" );
	}

}
