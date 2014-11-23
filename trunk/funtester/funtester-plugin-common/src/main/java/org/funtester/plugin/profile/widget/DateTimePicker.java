package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.Element;

/**
 * DateTimePicker
 *
 * @author Thiago Delgado Pinto
 */
public abstract class DateTimePicker implements Element {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "picker.datetime" )
			|| value.equalsIgnoreCase( "datetime.picker" )
			|| value.equalsIgnoreCase( "datetimepicker" )
			;
	}

}
