package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.ElementCodeGenerator;

/**
 * MessageBoxWithAbortRetryIgnore
 *
 * @author Thiago Delgado Pinto
 */
public abstract class MessageBoxWithAbortRetryIgnore implements ElementCodeGenerator {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "messagebox.abort-retry-ignore" )
			|| value.equalsIgnoreCase( "box.dialog.abort-retry-ignore" )
			;
	}

}
