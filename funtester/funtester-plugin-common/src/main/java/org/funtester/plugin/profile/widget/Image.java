package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.Element;

/**
 * Image
 *
 * @author Thiago Delgado Pinto
 */
public abstract class Image implements Element {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "image" )
			|| value.equalsIgnoreCase( "img" );
	}

}
