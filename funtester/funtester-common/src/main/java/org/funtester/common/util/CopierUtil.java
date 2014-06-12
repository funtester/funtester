package org.funtester.common.util;

import java.util.Collection;

/**
 * Copier Utilities
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class CopierUtil {
	
	/**
	 * Copy a collection.
	 * 
	 * @param <T>	the type of the collection.
	 * @param from	the collection to be copied.
	 * @param to	the collection to copy.
	 */	
	@SuppressWarnings("unchecked")
	public static < T extends Copier< ? super T > > boolean copyCollection(
			Collection< T > from,
			Collection< T > to
			) {
		if ( null == from || null == to ) return false;
		to.clear();
		for ( T obj : from ) {
			to.add( (T) obj.newCopy() );
		}
		return true;
	}
}
