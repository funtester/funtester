package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.ElementCodeGenerator;

/**
 * MessageBoxWithOK
 *
 * @author Thiago Delgado Pinto
 */
public abstract class MessageBoxWithOK implements ElementCodeGenerator {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "messagebox.ok" )
			|| value.equalsIgnoreCase( "box.dialog.ok" )
			|| value.equalsIgnoreCase( "message" ) // legacy
			;
	}

}
