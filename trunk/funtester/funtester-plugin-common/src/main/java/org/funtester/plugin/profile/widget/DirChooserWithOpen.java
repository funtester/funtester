package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.ElementCodeGenerator;

/**
 * DirChooserWithOpen
 *
 * @author Thiago Delgado Pinto
 */
public abstract class DirChooserWithOpen implements ElementCodeGenerator {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "chooser.dir.open" )
			;
	}

}
