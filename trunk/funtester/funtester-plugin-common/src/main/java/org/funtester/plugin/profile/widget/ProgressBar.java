package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.Element;

/**
 * ProgressBar
 *
 * @author Thiago Delgado Pinto
 */
public abstract class ProgressBar implements Element {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "bar.progress" )
			|| value.equalsIgnoreCase( "progressbar" )
			;
	}

}
