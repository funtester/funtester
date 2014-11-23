package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.Element;

/**
 * ToolBar
 *
 * @author Thiago Delgado Pinto
 */
public abstract class ToolBar implements Element {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "bar.tool" )
			|| value.equalsIgnoreCase( "toolbar" );
	}

}
