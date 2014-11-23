package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.ElementCodeGenerator;

/**
 * Tab
 *
 * @author Thiago Delgado Pinto
 */
public abstract class Tab implements ElementCodeGenerator {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "tab" );
	}

}
