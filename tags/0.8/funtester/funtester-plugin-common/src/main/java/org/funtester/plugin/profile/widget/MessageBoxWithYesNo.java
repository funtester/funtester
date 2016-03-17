package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.ElementCodeGenerator;

/**
 * MessageBoxWithYesNo
 *
 * @author Thiago Delgado Pinto
 */
public abstract class MessageBoxWithYesNo implements ElementCodeGenerator {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "messagebox.yes-no" )
			|| value.equalsIgnoreCase( "box.dialog.yes-no" );
	}

}
