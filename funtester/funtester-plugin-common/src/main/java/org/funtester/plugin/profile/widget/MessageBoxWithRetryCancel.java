package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.Element;

/**
 * MessageBoxWithRetryCancel
 *
 * @author Thiago Delgado Pinto
 */
public abstract class MessageBoxWithRetryCancel implements Element {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "messagebox.retry-cancel" )
			|| value.equalsIgnoreCase( "box.dialog.retry-cancel" )
			;
	}

}
