package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.Element;

/**
 * ToolTip
 *
 * @author Thiago Delgado Pinto
 */
public abstract class ToolTip implements Element {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "tooltip" );
	}

}
