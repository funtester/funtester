package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.Element;

/**
 * Panel
 *
 * @author Thiago Delgado Pinto
 */
public abstract class Panel implements Element {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "panel" );
	}

}
