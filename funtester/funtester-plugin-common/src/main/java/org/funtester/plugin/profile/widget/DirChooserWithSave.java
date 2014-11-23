package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.Element;

/**
 * DirChooserWithSave
 *
 * @author Thiago Delgado Pinto
 */
public abstract class DirChooserWithSave implements Element {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "chooser.dir.save" )
			;
	}

}
