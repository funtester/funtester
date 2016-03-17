package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.ElementCodeGenerator;

/**
 * ScrollBar
 *
 * @author Thiago Delgado Pinto
 */
public abstract class ScrollBar implements ElementCodeGenerator {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "bar.scroll" )
			|| value.equalsIgnoreCase( "scrollbar" )
			;
	}

}
