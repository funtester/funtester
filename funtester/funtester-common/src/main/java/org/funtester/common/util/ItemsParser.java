package org.funtester.common.util;

import java.util.Collection;

/**
 * Allow to parse values separated by a character and add them to a list
 * and get list items as a text separated by a character.  
 * 
 * @author Thiago Delgado Pinto
 */
public class ItemsParser {
	
	public static final String DEFAULT_SEPARATOR = ",";

	/**
	 * Parse values separated by a character and add them to a list.
	 * @param text A text to be parsed.
	 * @param separator	Items separator.
	 * @param items Where to place each item.
	 */
	public static void addFromParse(String text, String separator, Collection< String > items) {
		items.clear();
		String newItems[] = text.split( separator );
		for ( String item : newItems ) {
			String newItem = item.trim();
			if ( ! newItem.isEmpty() ) {
				items.add( newItem );
			}
		}
	}
	
	
	/**
	 * Get list items as a text separated by a character.
	 * @param separator	Items separator.
	 * @param items	The items to separate.
	 * @return A string
	 */
	public static String textFromItems(String separator, Collection< ? > items) {
		String text = ""; //$NON-NLS-1$
		for ( Object item : items ) {
			if ( null == item ) {
				continue;
			}
			text += item.toString() + separator;
		}
		if ( text.isEmpty() ) {
			return ""; //$NON-NLS-1$
		}
		int lastSeparator = text.lastIndexOf( separator );
		return text.substring( 0, lastSeparator );
	}
}
