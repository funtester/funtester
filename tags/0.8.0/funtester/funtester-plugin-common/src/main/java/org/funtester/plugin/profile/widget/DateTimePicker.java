package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.ElementCodeGenerator;

/**
 * DateTimePicker
 *
 * @author Thiago Delgado Pinto
 */
public abstract class DateTimePicker implements ElementCodeGenerator {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "picker.datetime" )
			|| value.equalsIgnoreCase( "datetime.picker" )
			|| value.equalsIgnoreCase( "datetimepicker" )
			;
	}

}
