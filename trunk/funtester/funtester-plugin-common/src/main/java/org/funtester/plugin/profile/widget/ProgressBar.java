package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.ElementCodeGenerator;

/**
 * ProgressBar
 *
 * @author Thiago Delgado Pinto
 */
public abstract class ProgressBar implements ElementCodeGenerator {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "bar.progress" )
			|| value.equalsIgnoreCase( "progressbar" )
			;
	}

}
