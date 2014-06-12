package org.funtester.core.util.convertion;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

/**
 * Converts a {@link Object} to other formats.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public final class ObjectConverter {
	
	private ObjectConverter() {}

	//
	// java.lang.*
	//	
	
	public static String toString(Object o) {
		if ( null == o ) {
			return null;
		}
		return o.toString();
	}

	public static Integer toInt(Object o) {
		if ( o instanceof Number ) {
			return ( (Number) o ).intValue();
		} else if ( o instanceof String ) {
			return Integer.parseInt( o.toString() );
		}
		return null;
	}

	public static Long toLong(Object o) {
		if ( o instanceof Number ) {
			return ( (Number) o ).longValue();
		} else if ( o instanceof String ) {
			return Long.parseLong( o.toString() );
		}
		return null;
	}

	public static Double toDouble(Object o) {
		if ( o instanceof Number ) {
			return ( (Number) o ).doubleValue();
		} else if ( o instanceof String ) {
			return Double.parseDouble( o.toString() );
		}
		return null;
	}
	
	//
	// joda-time
	//
	
	public static DateTime toDateTime(Object o) {
		// Compared with null in this case because Joda-Time accepts a Object time in the constructor
		if ( o != null ) {
			if ( o instanceof DateTime ) {
				return (DateTime) o;
			} else if ( o instanceof String ) {
				return DateTime.parse( o.toString() );
			} else { // Tries to construct with the Object type
				return new DateTime( o );
			}
		}
		return null;
	}
	
	public static LocalDate toLocalDate(Object o) {
		// Compared with null in this case because Joda-Time accepts a Object time in the constructor
		if ( o != null ) {
			if ( o instanceof LocalDate ) {
				return (LocalDate) o;
			} else if ( o instanceof String ) {
				return LocalDate.parse( o.toString() );
			} else { // Tries to construct with the Object type
				return new LocalDate( o );
			}
		}
		return null;
	}
	
	public static LocalTime toLocalTime(Object o) {
		// Compared with null in this case because Joda-Time accepts a Object time in the constructor
		if ( o != null ) {
			if ( o instanceof LocalTime ) {
				return (LocalTime) o;
			} else if ( o instanceof String ) {
				return LocalTime.parse( o.toString() );
			} else { // Tries to construct with the Object type
				return new LocalTime( o );
			}
		}
		return null;
	}

	public static Boolean toBoolean(Object o) {
		if ( o != null ) {
			if ( o instanceof Boolean ) {
				return (Boolean) o;
			} else {
				return Boolean.parseBoolean( o.toString() );
			}
		}
		return null;
	}
	
}