package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.ElementCodeGenerator;

/**
 * FileChooserWithOpen
 *
 * @author Thiago Delgado Pinto
 */
public abstract class FileChooserWithOpen implements ElementCodeGenerator {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "chooser.file.open" )
			;
	}

}
