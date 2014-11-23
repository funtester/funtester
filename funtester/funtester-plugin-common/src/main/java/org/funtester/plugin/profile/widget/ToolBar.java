package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.ElementCodeGenerator;

/**
 * ToolBar
 *
 * @author Thiago Delgado Pinto
 */
public abstract class ToolBar implements ElementCodeGenerator {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "bar.tool" )
			|| value.equalsIgnoreCase( "toolbar" );
	}

}
