package org.funtester.common.util;

import java.security.InvalidParameterException;
import java.util.List;

/**
 * List utilities.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public final class ListUtil {
	
	private ListUtil() {}

	/**
	 * Convert a integer list to a array.
	 * 
	 * @param intList	List to convert
	 * @return			Array
	 */
	public static int[] toArray(List< Integer > intList) {
		final int SIZE = intList.size();
		int list[] = new int[ SIZE ];
		for ( int i = 0; ( i < SIZE ); ++i ) {
			list[ i ] = intList.get( i );
		}
		return list;
	}
	
	/**
	 * Replaces a list item with a list.
	 * 
	 * @param <T>			Item type.
	 * @param sourceList	Source list.
	 * @param item			Item to be replaced.
	 * @param itemList		List to replace the item.
	 * @return				true if successfully replaced, false otherwise.
	 * @throws Exception
	 */
	public static < T > boolean replace(
			List< T > sourceList,
			T item,
			List< T > itemList
			) {
		if ( null == sourceList || null == item || null == itemList ) {
			String arg = (null == sourceList) ? "sourceList" : ( null == item ) ? "item" : "itemList";
			throw new InvalidParameterException( "'" + arg + "' cannot be null." );
		}		
		int index = sourceList.indexOf( item );
		if ( index < 0  ) {
			throw new InvalidParameterException( "'item' not found in the 'sourceList'." );
		}
		if ( itemList.isEmpty() ) {
			throw new InvalidParameterException( "Cannot replace the item with a empty list." );
		}		
		if ( sourceList.remove( item ) ) {
			return sourceList.addAll( index, itemList );
		}
		return false; // CAN'T REMOVE	
	}
	
	
}