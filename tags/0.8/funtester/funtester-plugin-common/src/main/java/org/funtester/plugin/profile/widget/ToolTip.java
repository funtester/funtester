package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.ElementCodeGenerator;

/**
 * ToolTip
 *
 * @author Thiago Delgado Pinto
 */
public abstract class ToolTip implements ElementCodeGenerator {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "tooltip" );
	}

}
