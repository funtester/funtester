package org.funtester.core.process.rule;

import java.sql.Connection;
import java.util.Map;
import java.util.TreeMap;

/**
 * A simple cache for {@link Connection}s.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class ConnectionCache {
	
	private final Map< String, Connection > cache;
	
	public ConnectionCache() {
		cache = new TreeMap< String, Connection >();
	}
	
	/**
	 * Generate a key for the database cache.
	 * 
	 * @param driverClassName
	 * @param databaseName
	 * @param databasePort
	 * @return
	 */
	public static String keyFor(
			String driverClassName,
			String databaseName,
			int databasePort
			) {
		return ( new StringBuffer() )
				.append( driverClassName ).append( " :: " )
				.append( databaseName ).append( " :: " )
				.append( databasePort )
				.toString();
	}
	
	public void put(String key, Connection c) {
		cache.put( key, c );
	}
	
	public Connection get(final String key) {
		return cache.get( key );
	}

}
