package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.Element;

/**
 * Calendar
 *
 * @author Thiago Delgado Pinto
 */
public abstract class Calendar implements Element {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "calendar" );
	}

}
