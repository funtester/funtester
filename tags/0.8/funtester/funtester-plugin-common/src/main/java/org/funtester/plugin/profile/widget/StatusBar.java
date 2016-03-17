package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.ElementCodeGenerator;

/**
 * StatusBar
 *
 * @author Thiago Delgado Pinto
 */
public abstract class StatusBar implements ElementCodeGenerator {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "bar.status" )
			|| value.equalsIgnoreCase( "statusbar" );
	}

}
