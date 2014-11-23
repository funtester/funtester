package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.Element;

/**
 * Grid
 *
 * @author Thiago Delgado Pinto
 */
public abstract class Grid implements Element {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "grid" )
			|| value.equalsIgnoreCase( "table" )
			;
	}

}
