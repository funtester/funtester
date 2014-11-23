package org.funtester.plugin.profile.action;

import org.funtester.plugin.profile.Action;

/**
 *
 * @author Thiago Delgado Pinto
 */
public class DoubleClick implements Action {

	@Override
	public boolean is(String value) {
		return value.equalsIgnoreCase( "click.double" )
			|| value.equalsIgnoreCase( "double.click" )
			|| value.equalsIgnoreCase( "doubleClick" )
			|| value.equalsIgnoreCase( "dblClick" )
			;
	}

}
