package org.funtester.app.ui.common;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.funtester.app.i18n.Messages;

/**
 * An utility class to generate translations for enum values.
 * 
 * @author Thiago Delgado Pinto
 * 
 * @see {@link Messages}
 *
 */
public final class EnumTranslation {
	
	private EnumTranslation() {}

	/**
	 * Create a list of strings containing the translation of each enumerated
	 * value. 
	 * 
	 * @param clazz	the enumerated class.
	 * @return		a list of strings.
	 */
	public static <T extends Enum< T >> List< String > createList(
			final Class< T > clazz) {
		List< String > list = new ArrayList< String >();
		final Set< T > set = EnumSet.allOf( clazz );
		for ( T obj : set ) {
			list.add( translationForItem( clazz, obj ) );
		}
		return list;
	}

	/**
	 * Translate a enumerated value.
	 * 
	 * @param clazz	the enumerated class.
	 * @param obj	the enumerated value.
	 * @return		the translated value.
	 */
	public static <T extends Enum< T >> String translationForItem(
			final Class< T > clazz,
			final T obj) {
		return Messages.getString( clazz.getSimpleName() + "." + obj.name() );
	}		

}
