package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.ElementCodeGenerator;

/**
 * DirChooserWithSave
 *
 * @author Thiago Delgado Pinto
 */
public abstract class DirChooserWithSave implements ElementCodeGenerator {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "chooser.dir.save" )
			;
	}

}
