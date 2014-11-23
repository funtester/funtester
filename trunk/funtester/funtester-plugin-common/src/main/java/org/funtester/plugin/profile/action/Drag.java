package org.funtester.plugin.profile.action;

import org.funtester.plugin.profile.Action;

/**
 *
 * @author Thiago Delgado Pinto
 */
public class Drag implements Action {

	@Override
	public boolean is(String value) {
		return value.equalsIgnoreCase( "drag" );
	}

}
