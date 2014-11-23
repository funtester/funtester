package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.Element;

/**
 * MessageBoxWithOKCancel
 *
 * @author Thiago Delgado Pinto
 */
public abstract class MessageBoxWithOKCancel implements Element {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "messagebox.ok-cancel" )
			|| value.equalsIgnoreCase( "box.dialog.ok-cancel" )
			;
	}

}
