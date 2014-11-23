package org.funtester.plugin.profile.action;

import org.funtester.plugin.profile.Action;

/**
 *
 * @author Thiago Delgado Pinto
 */
public class AnswerWithText implements Action {

	@Override
	public boolean is(String value) {
		return value.equalsIgnoreCase( "answer.withText" )
			|| value.equalsIgnoreCase( "answer.with" )
			|| value.equalsIgnoreCase( "answer.text" )
			;
	}

}
