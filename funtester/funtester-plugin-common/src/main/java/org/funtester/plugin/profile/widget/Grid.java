package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.ElementCodeGenerator;

/**
 * Grid
 *
 * @author Thiago Delgado Pinto
 */
public abstract class Grid implements ElementCodeGenerator {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "grid" )
			|| value.equalsIgnoreCase( "table" )
			;
	}

}
