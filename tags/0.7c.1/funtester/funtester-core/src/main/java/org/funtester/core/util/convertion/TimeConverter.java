package org.funtester.core.util.convertion;

import org.joda.time.LocalTime;

/**
 * Time converter
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class TimeConverter {
	
	/**
	 * Convert from millis to hours, minutes and seconds, in a string separated
	 * by ":".
	 * 
	 * <br />TODO be more flexible about the time formatting.
	 * 
	 * @param millis	the millis to be converted.
	 * @return			the formatted time in hh:mm:ss.			
	 */
	public static String toHMS(final long millis) {
		final int hours = millisToHours( millis );
		final int minutes = millisToMinutes( millis );
		final int seconds = millisToSeconds( millis );
		return ( (hours > 0) ? leadingZero( hours ) + ":" : "" )
			+ ( (minutes > 0)? leadingZero( minutes ) + ":" : "" )
			+ leadingZero( seconds );	
	}
	
	private static String leadingZero(final int value) {
		if ( value >= 0 && value <= 9 ) return "0" + value;
		return String.valueOf( value );
	}
	
	public static LocalTime fromMillis(final long ms) {
		int millis = (int) ms % 1000;
		int sec = (int) ms / 1000;
		int seconds = sec % 60;
		int min = sec / 60;								
		int minutes = min % 60;
		int hours = min / 60;
		return new LocalTime( hours, minutes, seconds, millis );
	}
	
	public static long toMillis(LocalTime value) {
		return value.getMillisOfSecond()
			+ secToMs( value.getSecondOfMinute() )
			+ secToMs( minToSec( value.getMinuteOfHour() ) )
			+ secToMs( minToSec( hToMin( value.getHourOfDay() ) ) );
	}
	
	
	public static int millisToSeconds(final long ms) {
		int sec = (int) ms / 1000;
		return sec % 60;
	}
	
	public static int millisToMinutes(final long ms) {
		int sec = (int) ms / 1000;	
		int min = sec / 60;								
		return min % 60;
	}
	
	public static int millisToHours(final long ms) {
		int sec = (int) ms / 1000;
		int min = sec / 60;								
		return min / 60;		
	}
	
	private static long secToMs(long seconds) {
		return seconds * 1000;
	}
	
	private static long minToSec(long minutes) {
		return minutes * 60;
	}
	
	private static long hToMin(long hours) {
		return hours * 60;
	}
}
