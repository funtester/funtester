package org.funtester.common.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A very simple timer.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class SimpleTimer {
	
	/**
	 * Map with times
	 */
	private final Map< String, Long > timeMap =
			new LinkedHashMap< String, Long >();
	
	/**
	 * Start the timer for a certain description.
	 * 
	 * @param description
	 */
	public void start(final String description) {
		timeMap.put( description, System.currentTimeMillis() );
	}

	/**
	 * Stop the timer for a certain description.
	 * 
	 * @param description
	 */
	public void stop(final String description) {
		Long time = timeMap.get( description );
		timeMap.put( description, System.currentTimeMillis() - time );
	}
	
	/**
	 * Return the time for a certain description.
	 * 
	 * @param description
	 * @return
	 */
	public Long timeFor(final String description) {
		return timeMap.get( description );
	}
	
	/**
	 * Return {@code true} whether it has a timer with the given description.
	 * @param description
	 * @return
	 */
	public boolean has(final String description) {
		return timeMap.get( description ) != null;
	}
}
