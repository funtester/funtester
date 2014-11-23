package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.Element;

/**
 * MessageBoxWithCancelTryAgainContinue
 *
 * @author Thiago Delgado Pinto
 */
public abstract class MessageBoxWithCancelTryAgainContinue implements Element {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "messagebox.cancel-tryagain-continue" )
			|| value.equalsIgnoreCase( "box.dialog.cancel-tryagain-continue" )
			;
	}

}
