package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.ElementCodeGenerator;

/**
 * AddressBar
 *
 * @author Thiago Delgado Pinto
 */
public abstract class AddressBar implements ElementCodeGenerator {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "bar.address" )
			|| value.equalsIgnoreCase( "addressbar" );
	}

}
