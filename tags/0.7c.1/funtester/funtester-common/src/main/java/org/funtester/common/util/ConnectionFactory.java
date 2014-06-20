package org.funtester.common.util;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Create database connections.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class ConnectionFactory {
	
	/**
	 * Create a {@link Connection} from a given driver.
	 * 
	 * @param driver	the driver to create the connection.
	 * @param jdbcURL	the JDBC URL of the database.
	 * @param user		the user.
	 * @param password	the password.
	 * @param dialect	the database dialect.
	 * @return			a {@link Connection} object.
	 * @throws SQLException
	 */
	public static java.sql.Connection createConnection(
			final Driver driver,
			final String jdbcURL,
			final String user,
			final String password,
			final String dialect
			) throws SQLException  {
		
		final Properties info = new Properties();
		info.setProperty( "user", user );
		info.setProperty( "password", password );
		info.setProperty( "dialect", dialect );
		// MySQL option to get the metadata for showing the table columns
		info.setProperty( "generateSimpleParameterMetadata", "true" );
		
		return driver.connect( jdbcURL, info );
	}

}
