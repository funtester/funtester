package org.funtester.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.funtester.common.util.criteria.Criteria;

/**
 * Collection filter
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class CollectionFilter {

	/**
	 * Filter a collection with the given criteria.
	 * 
	 * @param items		the items to be filtered.
	 * @param criteria	the criteria used to filter the items.
	 * @return			a collection with the filtered items.
	 */
	public <T> List< T > filter(
			final Collection< T > items,
			final Criteria< T > criteria
			) {
		List< T > filtered = new ArrayList< T >();
		for ( T obj : items ) {
			if ( criteria.matches( obj ) ) {
				filtered.add( obj );
			}
		}
		return filtered;
	}

	/**
	 * Find the first item that matches the given criteria.
	 * 
	 * @param items		the items.
	 * @param criteria	the criteria.
	 * @return			the item or null if not found.
	 */
	public <T> T first(
			final Collection< T > items,
			final Criteria< T > criteria
			) {
		for ( T obj : items ) {
			if ( criteria.matches( obj ) ) {
				return obj;
			}
		}
		return null;
	}

}
