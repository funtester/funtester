package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.Element;

/**
 * Memo
 *
 * @author Thiago Delgado Pinto
 */
public abstract class Memo implements Element {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "memo" )
			|| value.equalsIgnoreCase( "box.memo" )
			|| value.equalsIgnoreCase( "textarea" )
			;
	}

}
