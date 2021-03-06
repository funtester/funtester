package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.ElementCodeGenerator;

/**
 * TimePicker
 *
 * @author Thiago Delgado Pinto
 */
public abstract class TimePicker implements ElementCodeGenerator {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "picker.time" )
			|| value.equalsIgnoreCase( "time.picker" )
			|| value.equalsIgnoreCase( "timepicker" )
			;
	}

}
