package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.ElementCodeGenerator;

/**
 * Tree
 *
 * @author Thiago Delgado Pinto
 */
public abstract class Tree implements ElementCodeGenerator {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "tree" );
	}

}
