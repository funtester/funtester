package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.Element;

/**
 * Link
 *
 * @author Thiago Delgado Pinto
 */
public abstract class Link implements Element {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "link" );
	}

}
