package org.funtester.core.process.rule;

import java.sql.Driver;
import java.util.Map;
import java.util.TreeMap;

/**
 * A simple cache for {@link Driver}s.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class DriverCache {
	
	private final Map< String, Driver > cache;
	
	public DriverCache() {
		cache = new TreeMap< String, Driver >();
	}
	
	/**
	 * Make a key for the database cache. Currently supports one Driver per
	 * class name.
	 *
	 * @param driverClassName
	 * @return
	 */
	public static String makeKey(final String driverClassName
			) {
		return driverClassName;
	}
	
	public void put(String key, Driver d) {
		cache.put( key, d );
	}
	
	public Driver get(final String key) {
		return cache.get( key );
	}

}
