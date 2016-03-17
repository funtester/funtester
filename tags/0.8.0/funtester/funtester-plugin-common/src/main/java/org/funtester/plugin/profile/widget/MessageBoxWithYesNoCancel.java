package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.ElementCodeGenerator;

/**
 * MessageBoxWithYesNoCancel
 *
 * @author Thiago Delgado Pinto
 */
public abstract class MessageBoxWithYesNoCancel implements ElementCodeGenerator {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "messagebox.yes-no-cancel" )
			|| value.equalsIgnoreCase( "box.dialog.yes-no-cancel" )
			;
	}

}
