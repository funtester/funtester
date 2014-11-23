package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.Element;

/**
 * FileChooserWithSave
 *
 * @author Thiago Delgado Pinto
 */
public abstract class FileChooserWithSave implements Element {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "chooser.file.save" )
			;
	}

}
