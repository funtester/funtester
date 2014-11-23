package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.ElementCodeGenerator;

/**
 * MessageBoxWithCancelTryAgainContinue
 *
 * @author Thiago Delgado Pinto
 */
public abstract class MessageBoxWithCancelTryAgainContinue implements ElementCodeGenerator {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "messagebox.cancel-tryagain-continue" )
			|| value.equalsIgnoreCase( "box.dialog.cancel-tryagain-continue" )
			;
	}

}
