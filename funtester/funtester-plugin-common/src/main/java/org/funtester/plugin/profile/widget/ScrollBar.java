package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.Element;

/**
 * ScrollBar
 *
 * @author Thiago Delgado Pinto
 */
public abstract class ScrollBar implements Element {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "bar.scroll" )
			|| value.equalsIgnoreCase( "scrollbar" )
			;
	}

}
