package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.ElementCodeGenerator;

/**
 * ComboBoxWithDropDown
 *
 * @author Thiago Delgado Pinto
 */
public abstract class ComboBoxWithDropDown implements ElementCodeGenerator {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "combobox.dropdown" )
			|| value.equalsIgnoreCase( "box.combo.dropDown" )
			|| value.equalsIgnoreCase( "dropdown" )
			;
	}

}
