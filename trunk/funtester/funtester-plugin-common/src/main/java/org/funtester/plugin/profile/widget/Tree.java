package org.funtester.plugin.profile.widget;

import org.funtester.plugin.profile.Element;

/**
 * Tree
 *
 * @author Thiago Delgado Pinto
 */
public abstract class Tree implements Element {

	public boolean is(final String value) {
		return value.equalsIgnoreCase( "tree" );
	}

}
