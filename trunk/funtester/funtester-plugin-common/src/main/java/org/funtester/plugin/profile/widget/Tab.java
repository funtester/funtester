package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.Element;

/**
 * Tab
 *
 * @author Thiago Delgado Pinto
 */
public abstract class Tab implements Element {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "tab" );
	}

}
