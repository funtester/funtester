package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.ElementCodeGenerator;

/**
 * Link
 *
 * @author Thiago Delgado Pinto
 */
public abstract class Link implements ElementCodeGenerator {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "link" );
	}

}
