package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.Element;

/**
 * MessageBoxWithYesNo
 *
 * @author Thiago Delgado Pinto
 */
public abstract class MessageBoxWithYesNo implements Element {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "messagebox.yes-no" )
			|| value.equalsIgnoreCase( "box.dialog.yes-no" );
	}

}
