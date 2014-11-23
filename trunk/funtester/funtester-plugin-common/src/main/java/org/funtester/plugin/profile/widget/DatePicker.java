package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.Element;

/**
 * DatePicker
 *
 * @author Thiago Delgado Pinto
 */
public abstract class DatePicker implements Element {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "picker.date" )
			|| value.equalsIgnoreCase( "date.picker" )
			|| value.equalsIgnoreCase( "datepicker" )
			;
	}

}
