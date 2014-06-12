package org.funtester.core.software;

import org.funtester.core.util.convertion.ObjectConverter;

/**
 * Converter for a {@link ValueType}.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class ValueTypeBasedConverter {

	public static Object convert(final ValueType valueType, final Object value)
			throws Exception {
		if ( null == valueType || null == value ) { return null; }
		Object converted = null;
		switch ( valueType ) {
			case INTEGER	: converted = ObjectConverter.toInt( value ); break;
			case DOUBLE		: converted = ObjectConverter.toDouble( value ); break;
			case DATE		: converted = ObjectConverter.toLocalDate( value ); break;
			case TIME		: converted = ObjectConverter.toLocalTime( value ); break;
			case DATETIME	: converted = ObjectConverter.toDateTime( value ); break;
			case BOOLEAN	: converted = ObjectConverter.toBoolean( value ); break;
			default			: converted = value.toString();
		}
		return converted;
	}
}
