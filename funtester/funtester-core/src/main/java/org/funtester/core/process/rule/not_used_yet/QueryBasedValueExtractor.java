package org.funtester.core.process.rule.not_used_yet;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.funtester.common.util.ConnectionFactory;
import org.funtester.common.util.DatabaseDriverLoader;
import org.funtester.core.process.ResultSetCache;
import org.funtester.core.process.rule.DriverCache;
import org.funtester.core.software.DatabaseConfig;
import org.funtester.core.software.Element;
import org.funtester.core.software.ParameterConfig;
import org.funtester.core.software.QueryBasedVC;
import org.funtester.core.software.QueryConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Query-based value extractor
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class QueryBasedValueExtractor {
	
	private static final Logger logger = LoggerFactory.getLogger( QueryBasedValueExtractor.class );
	
	
	// Map from a driver class to a driver file 
	private final Map< String, String > availableDrivers;
	// Load a database driver dynamically
	private final DatabaseDriverLoader driverLoader;
	// Map from a driver class to a driver object
	private final Map< String, Driver > driverCache;
	// Map from a connection name to a connection object
	private final Map< String, Connection > connectionCache;
	// Map queries to their results
	// private final ResultSetCache resultSetCache;
	
	
	public QueryBasedValueExtractor(
			ResultSetCache resultSetCache,
			Map< String, String > availableDrivers
			) {
		//this.resultSetCache = resultSetCache;
		this.availableDrivers = availableDrivers;
		
		this.driverLoader = new DatabaseDriverLoader();
		
		this.driverCache = new LinkedHashMap< String, Driver >();
		this.connectionCache = new LinkedHashMap< String, Connection >();
	}
	
	/**
	 * Extract the values from a {@link QueryBasedVC}.
	 * 
	 * @param qvc					the query configuration.
	 * @param otherElementValues	the values from other elements.
	 * @return						a list of values.
	 * @throws Exception 
	 */
	public List< Object > extractValues(
			final QueryBasedVC qvc,
			final Map< Element, Object > otherElementValues
			) throws Exception {
		
		List< Object > values = new ArrayList< Object >();
		if ( null == qvc ) { return values; }
		
		QueryConfig qc = qvc.getQueryConfig();
		if ( null == qc ) { return values; }
		
		if ( null == qc.getDatabaseConfig() ) { return values; }
		
		// Prepare the query
		
		PreparedStatement stmt = prepareQuery( qc.getCommand(), qc.getDatabaseConfig() );
		
		// Extract the values from its parameters
		
		for ( ParameterConfig pc : qvc.getParameters() ) {
			
			
			// TODO <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
			
		}
		
		return null;
	}
	
	/**
	 * Prepare a database query.
	 * 
	 * @param command	the command to be prepared.
	 * @param dbc		the database configuration.
	 * @return			a prepared statement.
	 * @throws Exception 
	 */
	private PreparedStatement prepareQuery(
			final String command,
			final DatabaseConfig dbc
			) throws Exception {
		// Load the database driver
		
		final String driverClassName = dbc.getDriver();
		
		final String driverCacheKey = DriverCache.makeKey( driverClassName );
		
		Driver driver = driverCache.get( driverCacheKey );
		if ( null == driver ) {
			final String driverFileName = availableDrivers.get( driverClassName );
			if ( null == driverFileName ) {
				String msg = String.format( "None driver file found to the driver class \"%s\".", driverClassName ); // TODO i18n
				throw new Exception( msg );
			}
			driver = driverLoader.loadDriver( driverFileName, driverClassName );
			driverCache.put( driverCacheKey, driver );
		}
		
		// Load the database connection
		
		final String databaseCacheKey = generateDatabaseCacheKey(
				driverCacheKey, dbc.getName(), dbc.getPort() ); 
				
		Connection c = connectionCache.get( databaseCacheKey );
		if ( null == c ) {
			c = ConnectionFactory.createConnection( driver, dbc.toJdbcUrl(),
					dbc.getUser(), dbc.getPassword(), dbc.getDialect() );
			connectionCache.put( databaseCacheKey, c );
		}
		
		// Prepare the command statement
		
		return c.prepareStatement( command );
	}

	/**
	 * Generate a key for the database cache.
	 * 
	 * @param driverClassName
	 * @param databaseName
	 * @param databasePort
	 * @return
	 */
	private String generateDatabaseCacheKey(
			String driverClassName,
			String databaseName,
			int databasePort
			) {
		return ( new StringBuffer() )
				.append( driverClassName ).append( " - " )
				.append( databaseName ).append( " - " )
				.append( databasePort )
				.toString();
	}
	
	/*

	public List< Object > extractValues(
			final QueryBasedVC qvc,
			final Map< Element, Object > otherElementValues
			) throws FileNotFoundException, DriverNotAvailableException, DriverLoadException, SQLException {
		
		QueryConfig qc = qvc.getQueryConfig();
		DatabaseConfig dbc = qc.getDatabaseConfig();
		
		ConnectionFactory dbHelper = new ConnectionFactory( availableDrivers );
		Driver driver = dbHelper.createDriver( dbc );
		Connection c = dbHelper.createConnection( driver, dbc );
		
		final String COMMAND = qc.getCommand();
		logger.debug( "Command is " + COMMAND );
		
		PreparedStatement statement = c.prepareStatement( COMMAND );
		List< ParameterConfig > parameters = qvc.getParameters();
		logger.debug( "Parameters are " + parameters );
		
		List< Object > parameterValues = new ArrayList< Object >();
		int index = 1;
		for ( ParameterConfig parameter : parameters ) {
			
			Object value = configurationValues(
					parameter.getValueConfiguration(),
					otherElementValues
					);
			
			logger.debug( "### Value class	: " + value.getClass().getName() );
			logger.debug( "### Value			: " + value );
			logger.debug( "### Value is list	: " + (value instanceof List) );
			
			if ( value instanceof List ) {
				value = ((List< ? >) value).get( 0 );
				logger.debug( "### Value			: " + value );
			}
			
			if ( null == value ) {
				value = ""; // Need to be a value
			}
			
			parameterValues.add( value );
			
			try {
				// Get parameters metadata info
				final ParameterMetaData paramMetaData = statement.getParameterMetaData();
				
				if ( paramMetaData != null ) {
					final int MAX_PRECISION = paramMetaData.getPrecision( index );
					final String PARAM_CLASS_NAME = paramMetaData.getParameterClassName( index );
					
					// Truncates string values to not give an error in the query,
					// like DataTruncation.
					
					// TODO make this for other value types like date, time, double, etc.
					if ( PARAM_CLASS_NAME != null && value != null
							&& PARAM_CLASS_NAME.equals( String.class.getName() ) 
							&& value.getClass().getName().equals( String.class.getName() ) ) {
						String valueStr = (String) value;
						if ( valueStr.length() > MAX_PRECISION ) {
							value = valueStr.substring( 0, MAX_PRECISION );
						}
					}
				}
			} catch (SQLException e) {
				logger.debug( "Database does not support to get its metadata. DETAILS: " 
						+ e.getLocalizedMessage() );
				// Continue on error	
			}
			//
			// Set the parameter value in the query
			//
			statement.setObject( index, value );
			
			++index;
		}
		
		// VERY IMPORTANT: use the query parameters values as part of the key 
		final String key = makeCacheKey( dbc.getName(), COMMAND, parameterValues.toString() );
		final String targetColumn = qvc.getTargetColumn() != null ? qvc.getTargetColumn() : "";
		
		if ( targetColumn.isEmpty() ) {
			return new ArrayList< Object >(); // Empty values
		}
		
		logger.debug( "TARGET_COLUMN is: " + targetColumn );
		
		if ( ! resultSetCache.has( key ) ) {
			logger.debug( "Key not in cache: " + key );
			
			ResultSet rs = statement.executeQuery();
			final int COLUMN_COUNT = rs.getMetaData().getColumnCount();
			logger.debug( "Column count is: " + COLUMN_COUNT );
			
			List< Map< String, Object > > cacheValues = new ArrayList< Map< String, Object > >();
			List< Object > columnValues = new ArrayList< Object >();
			while ( rs.next() ) {
			
				// Create the map and add it to the cache				
				Map< String, Object > map = new TreeMap< String, Object >();
				for ( int i = 1; ( i <= COLUMN_COUNT ); ++i ) {
					Object value = rs.getObject( i ) ;
					String columnName = rs.getMetaData().getColumnName( i ).toUpperCase();
					logger.debug( "Column is: " + columnName + " and its value is: " + value );
					map.put( columnName, value );					
				}
				cacheValues.add( map );
				
				// Add the column value
				
				Object columnValue = null;
				try {
					columnValue = rs.getObject( targetColumn );
				} catch (SQLException e) {
					columnValue = "";
				}
				columnValues.add( columnValue );
			}
			// Add the cache values
			resultSetCache.put( key, cacheValues );
			
			return columnValues;
		}
		
		c.close();
		c = null;
		
		//
		// Exists in cache
		//
		
		logger.debug( "It has the key: " + key );
		logger.debug( "Target column is: " + targetColumn );
		
		List< Object > columnValues = new ArrayList< Object >();
		List< Map< String, Object > > cacheValues = resultSetCache.get( key );
		for ( Map< String, Object > map : cacheValues ) {
			Object value = map.get( targetColumn );
			logger.debug( "Existing value is: " + value );
			columnValues.add( value );
		}			
		return columnValues;
	}

	
	
	protected List< Object > configurationValues(
			final ValueConfiguration vc,
			final Map< Element, Object > otherElementsValues
			) throws FileNotFoundException, DriverNotAvailableException, DriverLoadException, SQLException {
		
		List< Object > values = new ArrayList< Object >();
		if ( null == vc ) {
			return values;
		}
		if ( vc instanceof SingleVC ) {
			
			values.add( ( (SingleVC) vc ).getValue() );
			
		} else if ( vc instanceof MultiVC ) {
			
			values.addAll( ( (MultiVC) vc ).getValues() );
			
		} else if ( vc instanceof QueryBasedVC ) {
			
			QueryBasedVC qvc = ( (QueryBasedVC) vc );
			values.addAll( extractValues( qvc, otherElementsValues ) );
			logger.debug( "# Query element value	: " + values );

		} else if ( vc instanceof ElementBasedVC ) {
			
			Element ee = ( (ElementBasedVC) vc ).getReferencedElement();
			values.add( otherElementsValues.get( ee ) );
			logger.debug( "# Referenced element		: " + ee.getName() );
			logger.debug( "# Referenced element value	: " + values );

		} else if ( vc instanceof RegExBasedVC ) {
			
			RegEx regex = ( (RegExBasedVC) vc ).getRegEx();
			values.add( regex.getExpression() );
			
		}
		return values;
	}

	private String makeCacheKey(final String part1, final String part2, final String part3) {
		return part1 + " :: " + part2 + " :: " + part3;
	}
	
	*/
}
