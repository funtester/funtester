package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.ElementCodeGenerator;

/**
 * Panel
 *
 * @author Thiago Delgado Pinto
 */
public abstract class Panel implements ElementCodeGenerator {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "panel" );
	}

}
