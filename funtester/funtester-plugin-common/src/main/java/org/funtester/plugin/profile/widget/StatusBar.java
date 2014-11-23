package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.Element;

/**
 * StatusBar
 *
 * @author Thiago Delgado Pinto
 */
public abstract class StatusBar implements Element {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "bar.status" )
			|| value.equalsIgnoreCase( "statusbar" );
	}

}
