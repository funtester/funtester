package org.funtester.plugin.profile;

import java.util.Collection;
import java.util.HashSet;

/**
 * Element finder
 *
 * @author Thiago Delgado Pinto
 *
 */
public class Finder {

	private final Collection< Recognizable > recognizables;

	/**
	 * Creates the finder with a collection of recognizables.
	 *
	 * @param recognizables
	 */
	public Finder(final Collection< Recognizable > recognizables) {
		this.recognizables = recognizables;
	}

	/**
	 * Creates the finder with one or more recognizables.
	 *
	 * @param recognizables
	 */
	public Finder(final Recognizable ... recognizables) {
		this.recognizables = new HashSet< Recognizable >();
		for ( Recognizable r : recognizables ) {
			this.recognizables.add( r );
		}
	}

	/**
	 * Return the recognizables with the given name or null if not found.
	 * @param value
	 * @return
	 */
	public Recognizable find( final String value ) {
		for ( Recognizable e : recognizables ) {
			if ( e.is( value ) ) {
				return e;
			}
		}
		return null;
	}

}
