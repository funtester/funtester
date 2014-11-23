package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.Element;

/**
 * AddressBar
 *
 * @author Thiago Delgado Pinto
 */
public abstract class AddressBar implements Element {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "bar.address" )
			|| value.equalsIgnoreCase( "addressbar" );
	}

}
