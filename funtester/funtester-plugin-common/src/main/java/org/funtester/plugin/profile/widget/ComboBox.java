package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.Element;

/**
 * ComboBox
 *
 * @author Thiago Delgado Pinto
 */
public abstract class ComboBox implements Element {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "combobox" )
			|| value.equalsIgnoreCase( "box.combo" )
			|| value.equalsIgnoreCase( "combo" )
			;
	}

}
