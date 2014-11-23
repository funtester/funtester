package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.Element;

/**
 * MessageBoxWithCancel
 *
 * @author Thiago Delgado Pinto
 */
public abstract class MessageBoxWithCancel implements Element {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "messagebox.cancel" )
			|| value.equalsIgnoreCase( "box.dialog.cancel" )
			;
	}

}
