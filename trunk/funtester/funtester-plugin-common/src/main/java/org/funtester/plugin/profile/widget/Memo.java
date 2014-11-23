package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.ElementCodeGenerator;

/**
 * Memo
 *
 * @author Thiago Delgado Pinto
 */
public abstract class Memo implements ElementCodeGenerator {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "memo" )
			|| value.equalsIgnoreCase( "box.memo" )
			|| value.equalsIgnoreCase( "textarea" )
			;
	}

}
