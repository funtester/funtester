package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.ElementCodeGenerator;

/**
 * SplitButton
 *
 * @author Thiago Delgado Pinto
 */
public abstract class SplitButton implements ElementCodeGenerator {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "button.split" )
			|| value.equalsIgnoreCase( "split" );
	}

}
