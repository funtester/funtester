package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.ElementCodeGenerator;

/**
 * FileChooserWithSave
 *
 * @author Thiago Delgado Pinto
 */
public abstract class FileChooserWithSave implements ElementCodeGenerator {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "chooser.file.save" )
			;
	}

}
