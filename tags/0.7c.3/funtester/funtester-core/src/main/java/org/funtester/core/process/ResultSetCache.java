package org.funtester.core.process;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Cache for {@link java.sql.ResultSet} values.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class ResultSetCache {
	
	private Map< String , List< Map< String, Object > > > values;
		
	
	public ResultSetCache() {
		values = new TreeMap< String, List< Map< String, Object > > >();
	}
	
	public static String keyFor(
			final String databaseName,
			final String queryCommand,
			final String parameterValues
			) {
		return ( new StringBuffer() )
			.append( databaseName ).append( " :: " )
			.append( queryCommand ).append( " :: " )
			.append( parameterValues )
			.toString();
	}

	public Map< String, List< Map< String, Object >>> getValues() {
		return values;
	}

	public void setValues(Map< String, List< Map< String, Object >>> values) {
		this.values = values;
	}
	
	public void put(String command, List< Map< String, Object > > lines) {
		values.put( command, lines );
	}
	
	public  List< Map< String, Object > > get(final String key) {
		return values.get( key );
	}

	public boolean has(String key) {
		return values.containsKey( key );
	}
	
	public void clear() {
		values.clear();
	}
}
