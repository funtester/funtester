package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.ElementCodeGenerator;

/**
 * MessageBoxWithOKCancel
 *
 * @author Thiago Delgado Pinto
 */
public abstract class MessageBoxWithOKCancel implements ElementCodeGenerator {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "messagebox.ok-cancel" )
			|| value.equalsIgnoreCase( "box.dialog.ok-cancel" )
			;
	}

}
