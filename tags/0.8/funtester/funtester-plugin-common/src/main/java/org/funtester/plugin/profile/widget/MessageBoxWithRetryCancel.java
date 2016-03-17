package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.ElementCodeGenerator;

/**
 * MessageBoxWithRetryCancel
 *
 * @author Thiago Delgado Pinto
 */
public abstract class MessageBoxWithRetryCancel implements ElementCodeGenerator {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "messagebox.retry-cancel" )
			|| value.equalsIgnoreCase( "box.dialog.retry-cancel" )
			;
	}

}
