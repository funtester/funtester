package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.Element;

/**
 * FileChooserWithOpen
 *
 * @author Thiago Delgado Pinto
 */
public abstract class FileChooserWithOpen implements Element {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "chooser.file.open" )
			;
	}

}
