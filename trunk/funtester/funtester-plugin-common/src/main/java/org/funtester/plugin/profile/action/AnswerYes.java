package org.funtester.plugin.profile.action;

import org.funtester.plugin.profile.Action;

/**
 *
 * @author Thiago Delgado Pinto
 */
public class AnswerYes implements Action {

	@Override
	public boolean is(String value) {
		return value.equalsIgnoreCase( "answer.yes" );
	}

}
